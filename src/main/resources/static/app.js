const STORAGE_KEYS = {
    selectedTeam: "fmhq.selectedTeam",
    purchasedPlayers: "fmhq.purchasedPlayers",
    lineupAssignments: "fmhq.lineupAssignments"
};

const LINEUP_SLOTS = [
    { key: "gk", label: "Goalkeeper", position: "GOALKEEPER" },
    { key: "lb", label: "Left Back", position: "DEFENDER" },
    { key: "cb1", label: "Center Back 1", position: "DEFENDER" },
    { key: "cb2", label: "Center Back 2", position: "DEFENDER" },
    { key: "rb", label: "Right Back", position: "DEFENDER" },
    { key: "cm1", label: "Central Midfielder 1", position: "MIDFIELDER" },
    { key: "cm2", label: "Central Midfielder 2", position: "MIDFIELDER" },
    { key: "cam", label: "Attacking Midfielder", position: "MIDFIELDER" },
    { key: "lw", label: "Left Wing", position: "FORWARD" },
    { key: "st", label: "Striker", position: "FORWARD" },
    { key: "rw", label: "Right Wing", position: "FORWARD" }
];

document.addEventListener("DOMContentLoaded", () => {
    const page = document.body.dataset.page;
    if (page === "home") {
        initHomePage();
    } else if (page === "choose-team") {
        initChooseTeamPage();
    } else if (page === "transfer-market") {
        initTransferMarketPage();
    } else if (page === "lineup-builder") {
        initLineupBuilderPage();
    }
});

function initHomePage() {
    const selectedTeam = readSelectedTeam();
    const purchasedPlayers = readPurchasedPlayers();
    const lineupAssignments = readLineupAssignments();

    document.getElementById("home-selected-team").textContent = selectedTeam?.name ?? "선택 전";
    document.getElementById("home-player-count").textContent = String(purchasedPlayers.length);

    const progressItems = [
        `선택된 팀: ${selectedTeam?.name ?? "아직 없음"}`,
        `구매한 선수 수: ${purchasedPlayers.length}명`,
        `포지션 배치 완료 슬롯: ${countAssignedSlots(lineupAssignments)} / 11`,
        "홈에서 모든 페이지로 이동할 수 있습니다."
    ];

    document.getElementById("progress-overview").innerHTML = progressItems
        .map(item => `<li>${item}</li>`)
        .join("");
}

async function initChooseTeamPage() {
    const selectedTeam = readSelectedTeam();
    document.getElementById("selected-team-banner").textContent = selectedTeam?.name ?? "선택 전";

    const response = await fetch("/api/teams/epl");
    const data = await response.json();
    document.getElementById("choose-team-season").textContent = data.season;

    const grid = document.getElementById("team-selection-grid");
    grid.innerHTML = data.teams.map(team => `
        <article class="team-card">
            <div>
                <div class="badge-row">
                    <span class="chip">${team.shortName}</span>
                    <span class="chip">#${team.order}</span>
                </div>
                <h2>${team.name}</h2>
                <p>선택 후 선수 구매 페이지로 이어집니다.</p>
            </div>
            <button class="primary-button" type="button" data-team='${escapeJson(team)}'>이 팀 선택</button>
        </article>
    `).join("");

    grid.querySelectorAll("button[data-team]").forEach(button => {
        button.addEventListener("click", () => {
            const team = JSON.parse(button.dataset.team);
            saveSelectedTeam(team);
            clearPurchasedPlayers();
            clearLineupAssignments();
            window.location.href = "/transfer-market.html";
        });
    });
}

async function initTransferMarketPage() {
    const selectedTeam = requireSelectedTeam("/choose-team.html");
    document.getElementById("market-selected-team").textContent = selectedTeam.name;

    const clearButton = document.getElementById("clear-purchases-button");
    clearButton.addEventListener("click", () => {
        clearPurchasedPlayers();
        clearLineupAssignments();
        renderPurchasedPlayers();
        syncMarketButtons();
    });

    const response = await fetch("/api/players/epl-market");
    const data = await response.json();
    const grid = document.getElementById("market-player-grid");

    grid.innerHTML = data.players.map(player => `
        <article class="market-card fifa-card">
            <div class="player-portrait" data-wiki-title="${player.wikiTitle}" data-player-name="${player.name}">
                <div class="portrait-fallback">${player.name.charAt(0)}</div>
            </div>
            <div class="card-content">
                <div class="player-meta-top">
                    <div>
                        <div class="badge-row">
                            <span class="chip">${player.club}</span>
                            <span class="chip">${player.position}</span>
                        </div>
                        <h2>${player.name}</h2>
                    </div>
                    <span class="overall-badge">${player.overall}</span>
                </div>
                <span class="price-tag">£${player.price}M</span>
                <div class="fifa-lines">
                    <div class="fifa-line">
                        ${renderStat("ATT", player.attack)}
                        ${renderStat("DEF", player.defense)}
                        ${renderStat("STA", player.stamina)}
                    </div>
                    <div class="fifa-line">
                        ${renderStat("PAS", player.passing)}
                        ${renderStat("FIN", player.finishing)}
                        ${renderStat("OVR", player.overall)}
                    </div>
                </div>
                <button class="primary-button market-buy-button" type="button" data-player='${escapeJson(player)}'>구매</button>
            </div>
        </article>
    `).join("");

    grid.querySelectorAll(".market-buy-button").forEach(button => {
        button.addEventListener("click", () => {
            const player = JSON.parse(button.dataset.player);
            addPurchasedPlayer(player);
            renderPurchasedPlayers();
            syncMarketButtons();
        });
    });

    renderPurchasedPlayers();
    syncMarketButtons();
    hydratePlayerPortraits();
}

function renderStat(label, value) {
    return `
        <div class="fifa-stat">
            <strong>${value}</strong>
            <span>${label}</span>
        </div>
    `;
}

function initLineupBuilderPage() {
    const selectedTeam = requireSelectedTeam("/choose-team.html");
    const purchasedPlayers = readPurchasedPlayers();

    document.getElementById("lineup-selected-team").textContent = selectedTeam.name;
    document.getElementById("lineup-player-count").textContent = String(purchasedPlayers.length);

    const squadList = document.getElementById("lineup-squad-list");
    if (purchasedPlayers.length === 0) {
        squadList.innerHTML = `<div class="warn-box">먼저 선수 구매 페이지에서 선수를 사야 합니다.</div>`;
    } else {
        squadList.innerHTML = purchasedPlayers.map(player => `
            <div class="compact-item">
                <div class="compact-item-header">
                    <strong>${player.name}</strong>
                    <span class="chip">${player.position}</span>
                </div>
                <span>${player.club} · OVR ${player.overall} · £${player.price}M</span>
            </div>
        `).join("");
    }

    renderLineupSlots();

    document.getElementById("auto-fill-button").addEventListener("click", autoFillLineup);
    document.getElementById("save-lineup-button").addEventListener("click", saveLineupAssignments);
}

function renderPurchasedPlayers() {
    const purchasedPlayers = readPurchasedPlayers();
    document.getElementById("market-player-count").textContent = String(purchasedPlayers.length);
    const list = document.getElementById("purchased-player-list");

    if (purchasedPlayers.length === 0) {
        list.innerHTML = `<div class="warn-box">아직 구매한 선수가 없습니다.</div>`;
        return;
    }

    list.innerHTML = purchasedPlayers.map(player => `
        <div class="compact-item">
            <div class="compact-item-header">
                <strong>${player.name}</strong>
                <button class="ghost-button remove-player-button" type="button" data-player-id="${player.playerId}">제거</button>
            </div>
            <span>${player.club} · ${player.position} · £${player.price}M</span>
        </div>
    `).join("");

    list.querySelectorAll(".remove-player-button").forEach(button => {
        button.addEventListener("click", () => {
            removePurchasedPlayer(Number(button.dataset.playerId));
            clearLineupAssignments();
            renderPurchasedPlayers();
            syncMarketButtons();
        });
    });
}

function syncMarketButtons() {
    const purchasedIds = new Set(readPurchasedPlayers().map(player => player.playerId));
    document.querySelectorAll(".market-buy-button").forEach(button => {
        const player = JSON.parse(button.dataset.player);
        const alreadyPurchased = purchasedIds.has(player.playerId);
        button.disabled = alreadyPurchased;
        button.textContent = alreadyPurchased ? "구매 완료" : "구매";
    });
}

function renderLineupSlots() {
    const purchasedPlayers = readPurchasedPlayers();
    const assignments = readLineupAssignments();
    const container = document.getElementById("lineup-slots");

    if (purchasedPlayers.length < 11) {
        container.innerHTML = `<div class="warn-box">포지션 배치를 하려면 최소 11명의 선수를 구매해야 합니다. 현재 ${purchasedPlayers.length}명입니다.</div>`;
        document.getElementById("assigned-slot-count").textContent = `${countAssignedSlots(assignments)} / 11`;
        return;
    }

    container.innerHTML = LINEUP_SLOTS.map(slot => `
        <div class="lineup-slot">
            <div class="badge-row">
                <span class="chip">${slot.position}</span>
            </div>
            <h3>${slot.label}</h3>
            <select data-slot-key="${slot.key}">
                <option value="">선수 선택</option>
                ${purchasedPlayers.map(player => `
                    <option value="${player.playerId}" ${String(assignments[slot.key] ?? "") === String(player.playerId) ? "selected" : ""}>
                        ${player.name} (${player.position} / ${player.club})
                    </option>
                `).join("")}
            </select>
        </div>
    `).join("");

    container.querySelectorAll("select[data-slot-key]").forEach(select => {
        select.addEventListener("change", () => {
            const nextAssignments = readLineupAssignments();
            if (select.value) {
                nextAssignments[select.dataset.slotKey] = Number(select.value);
            } else {
                delete nextAssignments[select.dataset.slotKey];
            }
            saveLineupAssignmentsToStorage(nextAssignments);
            document.getElementById("assigned-slot-count").textContent = `${countAssignedSlots(nextAssignments)} / 11`;
        });
    });

    document.getElementById("assigned-slot-count").textContent = `${countAssignedSlots(assignments)} / 11`;
}

function autoFillLineup() {
    const purchasedPlayers = readPurchasedPlayers();
    if (purchasedPlayers.length < 11) {
        return;
    }

    const used = new Set();
    const assignments = {};

    LINEUP_SLOTS.forEach(slot => {
        const candidate = purchasedPlayers.find(player => player.position === slot.position && !used.has(player.playerId))
            || purchasedPlayers.find(player => !used.has(player.playerId));

        if (candidate) {
            assignments[slot.key] = candidate.playerId;
            used.add(candidate.playerId);
        }
    });

    saveLineupAssignmentsToStorage(assignments);
    renderLineupSlots();
}

function saveLineupAssignments() {
    const assignments = readLineupAssignments();
    const message = document.getElementById("lineup-save-message");

    if (countAssignedSlots(assignments) < 11) {
        message.textContent = "아직 모든 슬롯이 채워지지 않았습니다.";
        return;
    }

    message.textContent = "포지션 배치가 저장되었습니다.";
}

function readSelectedTeam() {
    return readJson(STORAGE_KEYS.selectedTeam, null);
}

function saveSelectedTeam(team) {
    localStorage.setItem(STORAGE_KEYS.selectedTeam, JSON.stringify(team));
}

function readPurchasedPlayers() {
    return readJson(STORAGE_KEYS.purchasedPlayers, []);
}

function addPurchasedPlayer(player) {
    const players = readPurchasedPlayers();
    if (!players.some(item => item.playerId === player.playerId)) {
        players.push(player);
        localStorage.setItem(STORAGE_KEYS.purchasedPlayers, JSON.stringify(players));
    }
}

function removePurchasedPlayer(playerId) {
    const filtered = readPurchasedPlayers().filter(player => player.playerId !== playerId);
    localStorage.setItem(STORAGE_KEYS.purchasedPlayers, JSON.stringify(filtered));
}

function clearPurchasedPlayers() {
    localStorage.removeItem(STORAGE_KEYS.purchasedPlayers);
}

function readLineupAssignments() {
    return readJson(STORAGE_KEYS.lineupAssignments, {});
}

function saveLineupAssignmentsToStorage(assignments) {
    localStorage.setItem(STORAGE_KEYS.lineupAssignments, JSON.stringify(assignments));
}

function clearLineupAssignments() {
    localStorage.removeItem(STORAGE_KEYS.lineupAssignments);
}

function requireSelectedTeam(redirectPath) {
    const selectedTeam = readSelectedTeam();
    if (!selectedTeam) {
        window.location.href = redirectPath;
        throw new Error("선택된 팀이 없습니다.");
    }
    return selectedTeam;
}

function countAssignedSlots(assignments) {
    return Object.values(assignments).filter(Boolean).length;
}

function readJson(key, fallback) {
    try {
        const raw = localStorage.getItem(key);
        return raw ? JSON.parse(raw) : fallback;
    } catch (error) {
        return fallback;
    }
}

function escapeJson(data) {
    return JSON.stringify(data).replaceAll("'", "&apos;");
}

async function hydratePlayerPortraits() {
    const portraits = Array.from(document.querySelectorAll(".player-portrait[data-wiki-title]"));
    await Promise.all(portraits.map(async portrait => {
        const wikiTitle = portrait.dataset.wikiTitle;
        const imageUrl = await fetchWikipediaThumbnail(wikiTitle);
        if (!imageUrl) {
            return;
        }

        portrait.innerHTML = `<img src="${imageUrl}" alt="${portrait.dataset.playerName}">`;
    }));
}

async function fetchWikipediaThumbnail(title) {
    try {
        const response = await fetch(`https://en.wikipedia.org/w/api.php?action=query&format=json&formatversion=2&prop=pageimages&piprop=thumbnail&pithumbsize=500&titles=${encodeURIComponent(title)}&origin=*`);
        const data = await response.json();
        return data?.query?.pages?.[0]?.thumbnail?.source ?? null;
    } catch (error) {
        return null;
    }
}
