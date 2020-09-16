create table ecb.currency
(
    id     serial primary key,
    symbol varchar not null UNIQUE
);
