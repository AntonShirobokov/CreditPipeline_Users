const fields = ['lastName', 'firstName', 'middleName', 'phone', 'email'];
const passportFields = [
    'series', 'number', 'birthDate', 'birthPlace',
    'departmentCode', 'issuedBy', 'issueDate', 'inn', 'snils'
];
const addressFields = ['registrationAddress'];

const editBtn = document.getElementById('edit-btn');
const saveBtn = document.getElementById('save-btn');

// Универсальная функция редактирования
function enableEditing(fieldKeys, section) {
    fieldKeys.forEach(key => {
        const span = document.querySelector(`${section} .field[data-field="${key}"]`);
        if (!span) return;
        const value = span.textContent.trim();
        const input = document.createElement('input');
        input.name = key;
        input.value = value !== '-' ? value : '';
        input.classList.add('field');
        input.setAttribute('data-field', key);
        span.replaceWith(input);
    });
}

// Универсальная функция сбора данных
function collectData(fieldKeys, section) {
    const data = {};
    fieldKeys.forEach(key => {
        const input = document.querySelector(`${section} input[name="${key}"]`);
        if (input) {
            data[key] = input.value;
        }
    });
    return data;
}

// Получение CSRF токена
async function fetchCsrfToken() {
    const res = await fetch('/csrf', {
        credentials: 'same-origin'
    });
    if (!res.ok) {
        throw new Error("Не удалось получить CSRF токен");
    }
    return res.json();
}

// Отправка данных
async function saveSection(data, endpoint) {
    try {
        const csrf = await fetchCsrfToken();
        const res = await fetch(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrf.headerName]: csrf.token
            },
            credentials: 'same-origin',
            body: JSON.stringify(data)
        });

        if (res.ok) {
            alert("Данные обновлены");
            location.reload();
        } else {
            alert("Ошибка обновления");
        }
    } catch (err) {
        alert("Ошибка запроса: " + err.message);
    }
}

// Обработка ФИО и контактов
editBtn.addEventListener('click', () => {
    enableEditing(fields, '.section');
    editBtn.style.display = 'none';
    saveBtn.style.display = 'inline-block';
});
saveBtn.addEventListener('click', async () => {
    const data = collectData(fields, '.section');
    await saveSection(data, '/api/user/update-contact');
});

// Обработка Паспортных данных
document.querySelector('.edit-btn[data-section="passport"]').addEventListener('click', () => {
    enableEditing(passportFields, '.section:nth-of-type(2)');
    document.querySelector('.edit-btn[data-section="passport"]').style.display = 'none';
    document.querySelector('.save-btn[data-section="passport"]').style.display = 'inline-block';
});
document.querySelector('.save-btn[data-section="passport"]').addEventListener('click', async () => {
    const data = collectData(passportFields, '.section:nth-of-type(2)');
    await saveSection(data, '/api/user/update-passport');
});

// Обработка Адреса
document.querySelector('.edit-btn[data-section="address"]').addEventListener('click', () => {
    enableEditing(addressFields, '.section:nth-of-type(3)');
    document.querySelector('.edit-btn[data-section="address"]').style.display = 'none';
    document.querySelector('.save-btn[data-section="address"]').style.display = 'inline-block';
});
document.querySelector('.save-btn[data-section="address"]').addEventListener('click', async () => {
    const data = collectData(addressFields, '.section:nth-of-type(3)');
    await saveSection(data, '/api/user/update-address');
});
