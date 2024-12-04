--changeset kazan-mp:0_2_add_table_clients.sql

create table if not exists clients
(
    id          varchar(32) primary key not null,
    ip_address  varchar(15)             not null,
    first_date  timestamp with time zone,
    last_date   timestamp with time zone
);

comment on column clients.id is 'Индентификатор клиента';
comment on column clients.ip_address is 'IP адресс клиента';
comment on column clients.first_date is 'Первый запрос';
comment on column clients.last_date is 'Последний запрос';