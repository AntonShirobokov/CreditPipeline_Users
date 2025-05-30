async function fetchCsrfToken() {
    const response = await fetch('/csrf');
    if (!response.ok) throw new Error("Не удалось получить CSRF токен");
    return await response.json();
}

function formatAmount(amount) {
    return amount.toLocaleString('ru-RU') + ' ₽';
}

function formatDateTime(dateTimeStr) {
    const date = new Date(dateTimeStr);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Месяцы с 0, поэтому +1
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}`;
}

function createApplicationCard(app) {
    return `
        <div class="app-card">
            <div class="app-info">
                <div class="app-date">${formatDateTime(app.dateOfCreation)}</div>
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
            // Сортируем заявки по дате создания по убыванию
            data.sort((a, b) => new Date(b.dateOfCreation) - new Date(a.dateOfCreation));

            container.innerHTML = data.map(createApplicationCard).join('');
        }

    } catch (err) {
        console.error(err);
        document.getElementById('applications-container').innerHTML =
            `<p class="error-msg">Ошибка при загрузке данных</p>`;
    }
}

window.addEventListener('DOMContentLoaded', loadApplications);