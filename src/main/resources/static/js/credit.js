window.addEventListener('DOMContentLoaded', () => {
    fetch('/csrf')
        .then(response => response.json())
        .then(csrfData => {
            const csrfToken = csrfData.token;
            const csrfParameterName = csrfData.parameterName;
            const csrfHeaderName = csrfData.headerName;

            return fetch('/api/credit/checkInformation', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeaderName]: csrfToken
                }
            }).then(response => {
                if (!response.ok) throw new Error("invalid response");
                return response.json();
            }).then(data => {
                if (!data.valid) throw new Error("passport data invalid");

                document.getElementById('main-content').innerHTML = `
                    <h2>Форма заявки на кредит</h2>
                    <form id="credit-form" method="POST" action="/api/credit/applicationProcessing">
                        <input type="hidden" name="${csrfParameterName}" value="${csrfToken}">

                        <label>Сумма кредита: <input type="number" name="amount" required></label><br>
                        <label>Срок кредита (в месяцах): <input type="number" name="period" required></label><br>

                        <label>Цель кредита:
                            <select name="purpose" required>
                                <option value="">Выберите цель</option>
                                <option value="Автомобиль">Автомобиль</option>
                                <option value="Ремонт">Ремонт</option>
                                <option value="Образование">Образование</option>
                                <option value="Рефинансирование">Рефинансирование</option>
                                <option value="Другое">Другое</option>
                            </select>
                        </label><br>

                        <label>Уровень образования:
                            <select name="education" required>
                                <option value="">Выберите уровень</option>
                                <option value="Отсутствует">Отсутствует</option>
                                <option value="Основное общее">Основное общее</option>
                                <option value="Среднее общее">Среднее общее</option>
                                <option value="Среднее профессиональное">Среднее профессиональное</option>
                                <option value="Высшее">Высшее</option>
                            </select>
                        </label><br>

                        <label>Тип занятости:
                            <select name="typeOfEmployment" id="employment-select" required>
                                <option value="">Выберите тип</option>
                                <option value="Предприниматель">Предприниматель</option>
                                <option value="Работа по найму">Работа по найму</option>
                                <option value="Студент">Студент</option>
                                <option value="Пенсионер">Пенсионер</option>
                                <option value="Безработный">Безработный</option>
                            </select>
                        </label><br>

                        <div id="employment-fields">
                            <label>Доход в месяц: <input type="number" name="income" required></label><br>
                        </div>

                        <label>Тип жилья:
                            <select name="typeOfHousing" required>
                                <option value="">Выберите тип</option>
                                <option value="Собственное">Собственное</option>
                                <option value="Съемное">Съемное</option>
                                <option value="Проживаю с родственниками">Проживаю с родственниками</option>
                            </select>
                        </label><br>

                        <label>Количество иждивенцев: <input type="number" name="numberOfDependents" min="0" required></label><br>

                        <label>Семейное положение:
                            <select name="maritalStatus" required>
                                <option value="">Выберите статус</option>
                                <option value="Никогда не состоял(а)">Никогда не состоял(а)</option>
                                <option value="Замужем/женат">Замужем/женат</option>
                                <option value="Разведен/разведена">Разведен/разведена</option>
                                <option value="Вдова/вдовец">Вдова/вдовец</option>
                            </select>
                        </label><br>

                        <label>Залог:
                            <select name="deposit" required>
                                <option value="">Выберите тип</option>
                                <option value="Недвижимость">Недвижимость</option>
                                <option value="Транспортное средство">Транспортное средство</option>
                                <option value="Отсутствует">Отсутствует</option>
                            </select>
                        </label><br>

                        <button type="submit">Отправить заявку</button>
                    </form>
                `;

                const creditForm = document.getElementById('credit-form');

                creditForm.addEventListener('submit', function(event) {
                    event.preventDefault();

                    const formData = new FormData(creditForm);

                    fetch('/api/credit/applicationProcessing', {
                        method: 'POST',
                        headers: {
                            [csrfHeaderName]: csrfToken
                        },
                        body: formData
                    })
                        .then(response => response.json())
                        .then(data => {
                            if (!data.valid) {
                                document.getElementById('main-content').innerHTML = `
                                <p style="color:red;">Ошибка: ${data.message}</p>
                                <p><a href="/credit">Вернуться к заявке</a></p>
                            `;
                            } else {
                                document.getElementById('main-content').innerHTML = `
                                    <p style="color:green;">Заявка успешно отправлена!</p>
                                    <p><a href="/applications">Перейти к списку заявок</a></p>
                                `;
                            }
                        })
                        .catch(error => {
                            document.getElementById('main-content').innerHTML = `<p style="color:red;">Ошибка отправки: ${error.message}</p>`;
                        });
                });

                const employmentSelect = document.getElementById('employment-select');
                const employmentFields = document.getElementById('employment-fields');

                employmentSelect.addEventListener('change', () => {
                    employmentFields.innerHTML = `
                        <label>Доход в месяц: <input type="number" name="income" required></label><br>
                    `;
                });
            });
        })
        .catch(err => {
            document.getElementById('main-content').innerHTML = `
                <p>Перед заполнением заявки на кредит необходимо <a href="/profile">заполнить данные профиля</a>.</p>
            `;
        });
});
