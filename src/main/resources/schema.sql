drop table t_user_authority;
drop table t_user;
drop table t_deactivated_token;


create table t_user
(
    id         serial primary key,
    c_phone varchar not null unique,
    c_password varchar not null,
    c_firstname varchar not null,
    c_lastname varchar not null,
    c_middlename varchar not null
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

