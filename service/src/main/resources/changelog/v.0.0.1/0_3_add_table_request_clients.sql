--changeset kazan-mp:0_3_add_table_request_clients.sql

create table if not exists requests
(
    request_id  varchar(32) primary key not null,
    request_body jsonb,
    date        timestamp with time zone default CURRENT_TIMESTAMP,
    clients_id  varchar(32)             not null references clients (id)
);

comment on column requests.request_id is 'Индентификатор запроса';
comment on column requests.request_body is 'Запрос клиента';
comment on column requests.date is 'Дата';
comment on column requests.clients_id is 'Клиент';

