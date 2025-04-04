--changeset kazan-mp:0_5_alert_table_currency_clients.sql

alter table if exists clients
    add column if not exists is_sent      bool default false,
    add column if not exists is_processed bool default false,
    add column if not exists status varchar(15) default 'WAIT_SEND';

alter table if exists currency
    add column if not exists is_sent      bool default false,
    add column if not exists is_processed bool default false,
    add column if not exists status varchar(15) default 'WAIT_SEND';

