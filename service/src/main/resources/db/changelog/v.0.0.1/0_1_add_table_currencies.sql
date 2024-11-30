--changeset kazan-mp:0_1_add_table_currencies.sql
create table currency (
                          id varchar(30) primary key not null,
                          num_code integer unique not null,
                          char_code varchar(3) unique not null,
                          nominal integer,
                          name text,
                          value numeric(10,10) not null,
                          previous numeric(10,10),
                          create_date timestamp with time zone,
                          update_date timestamp with time zone
)