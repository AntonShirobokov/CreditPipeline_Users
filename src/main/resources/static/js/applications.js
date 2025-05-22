async function fetchCsrfToken() {
    const response = await fetch('/csrf');
    if (!response.ok) throw new Error("Не удалось получить CSRF токен");
    return await response.json();
}

function formatAmount(amount) {
    return amount.toLocaleString('ru-RU') + ' ₽';
}

function createApplicationCard(app) {
    return `
        <div class="app-card">
            <div class="app-info">
                <div class="app-date">${app.dateOfCreation}</div>
                <div class="app-amount">${formatAmount(app.amount)}</div>
                <div class="app-purpose">${app.purpose || 'Цель не указана'}</div>
            </div>
            <div class="app-status ${getStatusClass(app.status)}">${app.status}</div>
        </div>
        `;
}

function getStatusClass(status) {
    return {
        'Одобрено': 'status-approved',
        'Отказано': 'status-declined',
        'В рассмотрении': 'status-pending',
        'Ошибка оформления': 'status-error'
    }[status] || 'status-default';
}

function createEmptyState() {
    return `
            <div class="empty-state">
                <p>У вас пока нет заявок.</p>
                <a href="/credit" class="btn-new-app">Оформить первую заявку</a>
            </div>
        `;
}

async function loadApplications() {
    try {
        const csrf = await fetchCsrfToken();
        const response = await fetch('/api/getAllApplications', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                [csrf.headerName]: csrf.token
            }
        });

        const container = document.getElementById('applications-container');

        if (!response.ok) {
            container.innerHTML = `<p class="error-msg">Ошибка при загрузке заявок</p>`;
            return;
        }

        const data = await response.json();
        if (!data.length) {
            container.innerHTML = createEmptyState();
        } else {
            container.innerHTML = data.map(createApplicationCard).join('');
        }

    } catch (err) {
        console.error(err);
        document.getElementById('applications-container').innerHTML =
            `<p class="error-msg">Ошибка при загрузке данных</p>`;
    }
}

window.addEventListener('DOMContentLoaded', loadApplications);