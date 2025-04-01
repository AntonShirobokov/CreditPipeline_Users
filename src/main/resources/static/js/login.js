document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const phoneInput = document.getElementById('phone');
    const usernameInput = document.getElementById('username'); // Скрытое поле для username

    // Маска для телефона
    phoneInput.addEventListener('input', function(e) {
        let x = e.target.value.replace(/\D/g, '').match(/(\d{0,3})(\d{0,3})(\d{0,2})(\d{0,2})/);
        e.target.value = !x[2] ? x[1] : x[1] + ' ' + x[2] + (x[3] ? '-' + x[3] : '') + (x[4] ? '-' + x[4] : '');
    });

    // Обработка отправки формы
    loginForm.addEventListener('submit', function(e) {
        // Получаем значение телефона и очищаем от всех нецифровых символов
        let phoneDigits1 = phoneInput.value.replace(/\D/g, '');
        let phoneDigits2 = phoneInput.value;

        // Проверяем, что номер имеет правильную длину (10 цифр без +7)
        if (phoneDigits1.length !== 10) {
            e.preventDefault();
            document.getElementById('phoneError').textContent = 'Введите корректный номер телефона';
            return;
        }

        // Формируем номер в формате +7XXXXXXXXXX и записываем в скрытое поле username
        usernameInput.value = '+7 ' + phoneDigits2;

        // Очищаем поле phone, чтобы оно не отправлялось
        phoneInput.removeAttribute('name');
    });
});