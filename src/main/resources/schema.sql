drop table if exists t_user_authority;
drop table if exists t_user cascade;
drop table if exists t_passport;
drop table if exists t_deactivated_token;
drop table if exists t_deactivated_token;

create table t_user
(
    id         serial primary key,
    c_phone varchar not null unique,
    c_password varchar not null,
    c_firstname varchar not null,
    c_lastname varchar not null,
    c_middlename varchar not null,
    c_email varchar
);

create table t_passport
(
    id serial primary key,
    id_user int not null references t_user (id) ON DELETE CASCADE,
    c_series varchar(4),
    c_number varchar (6),
    c_birth_date date,
    c_birth_place varchar,
    c_department_code varchar (7),
    c_issued_by varchar,
    c_issue_date date,
    с_inn varchar,
    c_snils varchar,
    c_registration_address varchar
);

create table t_user_authority
(
    id          serial primary key,
    id_user     int     not null references t_user (id),
    c_authority varchar not null
);

create table t_deactivated_token
(
    id           uuid primary key,
    c_keep_until timestamp not null check ( c_keep_until > now() )
);

-- -- Создание ENUM типов
-- CREATE TYPE c_purpose_enum AS ENUM (
--     'Автомобиль',
--     'Ремонт',
--     'Образование',
--     'Рефинансирование',
--     'Другое'
--     );
--
-- CREATE TYPE c_type_of_employment_enum AS ENUM (
--     'Предприниматель',
--     'Работа по найму',
--     'Студент',
--     'Пенсионер',
--     'Безработный'
--     );
--
-- CREATE TYPE c_deposit_enum AS ENUM (
--     'Недвижимость',
--     'Транспортное средство',
--     'Отсутствует'
--     );
--
-- CREATE TYPE c_education_enum AS ENUM (
--     'Отсутствует',
--     'Основное общее',
--     'Среднее общее',
--     'Среднее профессиональное',
--     'Высшее'
--     );
--
-- CREATE TYPE c_type_of_housing_enum AS ENUM (
--     'Собственное',
--     'Съемное',
--     'Проживаю с родственниками'
--     );
--
-- CREATE TYPE c_marital_status_enum AS ENUM (
--     'Никогда не состоял(а)',
--     'Замужем/женат',
--     'Разведен/разведена',
--     'Вдова/вдовец'
--     );
--
-- CREATE TYPE c_status_enum AS ENUM (
--     'В рассмотрении',
--     'Ошибка оформления',
--     'Одобрено',
--     'Отказано'
--     );




-- Создание таблицы Application
CREATE TABLE t_application (
id SERIAL PRIMARY KEY,
id_user INT REFERENCES t_user(id),
c_amount INT,
c_period INT,
c_purpose varchar,
c_type_of_employment varchar,
c_deposit varchar,
c_income int,
c_dept INT,
c_education varchar,
c_type_of_housing varchar,
c_number_of_dependents INT,
c_marital_status varchar,
c_status varchar,
c_reason_for_refusal varchar,
c_real_income int,
c_was_bankrupt boolean
);
