const fields = ['lastName', 'firstName', 'middleName', 'phone', 'email'];
const passportFields = [
    'series', 'number', 'birthDate', 'birthPlace',
    'departmentCode', 'issuedBy', 'issueDate', 'inn', 'snils'
];
const addressFields = ['registrationAddress'];

const editBtn = document.getElementById('edit-btn');
const saveBtn = document.getElementById('save-btn');

function setupAddressAutocomplete(input) {
    const dropdown = document.createElement('ul');
    dropdown.className = 'autocomplete-dropdown';
    input.parentNode.appendChild(dropdown);

    input.addEventListener('input', async () => {
        const query = input.value.trim();
        dropdown.innerHTML = '';

        if (query.length < 3) return;

        try {
            const res = await fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(query)}&addressdetails=1&limit=5&countrycodes=ru`);

            const results = await res.json();

            results.forEach(place => {
                const addr = place.address;
                const formatted = [
                    addr.state,               // Республика / область
                    addr.county,              // Район
                    addr.city || addr.town || addr.village, // Населённый пункт
                    addr.road,                // Улица
                    addr.house_number         // Дом
                ].filter(Boolean).join(', '); // Убирает пустые значения и соединяет

                const item = document.createElement('li');
                item.textContent = formatted;
                item.addEventListener('click', () => {
                    input.value = formatted;
                    dropdown.innerHTML = '';
                });
                dropdown.appendChild(item);
            });
        } catch (e) {
            console.error('Ошибка автоподсказки адреса:', e);
        }
    });

    input.addEventListener('blur', () => {
        setTimeout(() => dropdown.innerHTML = '', 100); // скрыть подсказки с задержкой
    });
}



function enableEditing(fieldKeys, section) {
    fieldKeys.forEach(key => {
        const span = document.querySelector(`${section} .field[data-field="${key}"]`);
        if (!span) return;
        const value = span.textContent.trim();
        const wrapper = document.createElement('div');
        wrapper.classList.add('field-wrapper');

        const input = document.createElement('input');
        input.name = key;
        input.value = value !== '-' ? value : '';
        input.classList.add('field');
        input.setAttribute('data-field', key);

        wrapper.appendChild(input);
        span.replaceWith(wrapper);

        // Добавляем автозаполнение только для адреса
        if (key === 'registrationAddress') {
            setupAddressAutocomplete(input);
        }
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
            const result = await res.json();
            if (result.errors) {
                showFieldErrors(result.errors);
            } else {
                alert("Ошибка обновления");
            }
        }
    } catch (err) {
        alert("Ошибка запроса: " + err.message);
    }
}

function showFieldErrors(errors) {
    document.querySelectorAll('.field-error').forEach(el => el.remove());

    Object.entries(errors).forEach(([field, message]) => {
        const input = document.querySelector(`input[name="${field}"]`);
        if (input) {
            const wrapper = input.closest('.field-wrapper');
            if (wrapper) {
                const errorDiv = document.createElement('div');
                errorDiv.className = 'field-error';
                errorDiv.textContent = message;
                wrapper.appendChild(errorDiv);
            }
        }
    });
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
