--changeset kazan-mp:0_1_add_table_currencies.sql
create table if not exists currency (
                          id varchar(30) primary key not null,
                          num_code integer unique not null,
                          char_code varchar(3) unique not null,
                          nominal integer,
                          name text,
                          value numeric(20,4) not null,
                          previous numeric(20,4),
                          create_date timestamp with time zone,
                          update_date timestamp with time zone
);

insert into currency values ('R00001', 643, 'RUB', 1, 'Российский рубль', 1.0, null, CURRENT_TIMESTAMP, null);

comment on column currency.name is 'Наимнование полное валюты';
comment on column currency.id is 'Индентификатор валюты';
comment on column currency.num_code is 'Код (номерной) валюты';
comment on column currency.char_code is 'Код (буквенный) валюты';
comment on column currency.nominal is 'Номинал валюты';
comment on column currency.value is 'Курс валюты к рублю';
comment on column currency.previous is 'Курс валюты прошлый к рублю';
comment on column currency.create_date is 'Первое попадание';
comment on column currency.update_date is 'Последнее обновление';