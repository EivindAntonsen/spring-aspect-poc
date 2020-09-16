create table if not exists currencies.rates
(
    id          serial primary key,
    currency_id int   not null,
    rate        float not null,
    foreign key (currency_id) references currencies.currency (id) on delete cascade
)
