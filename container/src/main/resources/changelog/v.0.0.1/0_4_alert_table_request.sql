--changeset kazan-mp:0_4_alert_table_request

alter table if exists requests
    add column response_body jsonb;

