create table currencies.rates_request
(
    id               serial primary key,
    base_currency_id int       not null,
    datetime         timestamp not null,
    foreign key (base_currency_id) references currencies.currency (id) on delete cascade
);
