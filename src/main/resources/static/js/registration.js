document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('registrationForm');
    const phoneInput = document.getElementById('phoneForm');
    const phoneInputNew = document.getElementById('phone');
    // Маска для телефона
    phoneInput.addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');

        // Форматирование ввода
        let formattedValue = '';
        if (value.length > 0) {
            formattedValue = value.substring(0, 3);
            if (value.length > 3) {
                formattedValue += ' ' + value.substring(3, 6);
            }
            if (value.length > 6) {
                formattedValue += '-' + value.substring(6, 8);
            }
            if (value.length > 8) {
                formattedValue += '-' + value.substring(8, 10);
            }
        }

        e.target.value = formattedValue;
    });

    form.addEventListener('submit', function(e) {

        e.preventDefault();

        // Очищаем предыдущие сообщения
        document.getElementById('phoneError').textContent = '';
        document.getElementById('passwordError').textContent = '';
        document.getElementById('successMessage').textContent = '';

        // Получаем значения
        const phone = phoneInput.value.trim();
        const password = form.password.value.trim();
        let isValid = true;

        // Валидация телефона
        const phoneDigits1 = phone.replace(/\D/g, '');
        const phoneDigits2 = phone;
        if (phoneDigits1.length !== 10) {
            document.getElementById('phoneError').textContent = 'Введите 10 цифр номера после +7';
            isValid = false;
        }

        // Валидация пароля
        if (password.length < 6) {
            document.getElementById('passwordError').textContent = 'Минимум 6 символов';
            isValid = false;
        }

        // Если валидация пройдена
        if (isValid) {
            const fullPhoneNumber = '+7 ' + phoneDigits2;

            // Здесь можно добавить отправку данных на сервер
            console.log('Данные для регистрации:', {
                phone: fullPhoneNumber,
                password
            });
            phoneInputNew.value = fullPhoneNumber;

            phoneInput.removeAttribute("name");

            form.submit();
        }
    });
});