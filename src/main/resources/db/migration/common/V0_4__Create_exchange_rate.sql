create table if not exists ecb.exchange_rate
(
    id                        serial primary key,
    exchange_rate_response_id int   not null,
    currency_id               int   not null,
    rate                      float not null,
    foreign key (currency_id) references ecb.currency (id) on delete cascade,
    foreign key (exchange_rate_response_id) references ecb.exchange_rate_response (id) on delete cascade
)
