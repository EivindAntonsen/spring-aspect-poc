create table ecb.exchange_rate_response
(
    id               serial primary key,
    base_currency_id int       not null,
    datetime         timestamp not null,
    foreign key (base_currency_id) references ecb.currency (id) on delete cascade
);
