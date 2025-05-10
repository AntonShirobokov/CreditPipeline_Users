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

