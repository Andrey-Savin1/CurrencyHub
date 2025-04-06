create table currencies
(
    id          bigserial
        primary key,
    created_at  timestamp default now() not null,
    modified_at timestamp,
    code        varchar(3)              not null
        constraint code_unique
            unique,
    iso_code    integer                 not null
        constraint iso_code_unique
            unique,
    description varchar(64)             not null,
    active      boolean   default true,
    scale       integer                 not null,
    symbol      varchar(2)
);

create index currencies_active_uidx
    on currencies (active);

create table rate_providers
(
    provider_code      varchar(3)              not null
        primary key,
    created_at         timestamp default now() not null,
    modified_at        timestamp,
    provider_name      varchar(28)             not null
        unique,
    description        varchar(255),
    priority           integer                 not null,
    active             boolean   default true,
    default_multiplier numeric   default 1.0   not null
);


create index rate_providers_code_uidx
    on rate_providers (provider_code);

create table conversion_rates
(
    id               bigserial
        primary key,
    created_at       timestamp default now() not null,
    modified_at      timestamp,
    source_code      varchar(3)              not null
        references currencies (code),
    destination_code varchar(3)              not null
        references currencies (code),
    rate_begin_time  timestamp default now() not null,
    rate_end_time    timestamp               not null,
    rate             numeric                 not null,
    provider_code    varchar(3)
        references rate_providers,
    multiplier       numeric                 not null,
    system_rate      numeric                 not null
);

create table rate_correction_coefficients
(
    id               bigserial
        primary key,
    created_at       timestamp default now() not null,
    modified_at      timestamp,
    archived         boolean   default false,
    source_code      varchar                 not null,
    destination_code varchar                 not null,
    multiplier       numeric                 not null,
    provider_code    varchar(3)
        references rate_providers,
    creator          varchar(255),
    modifier         varchar(255),
    date_from        date,
    date_to          date,
    profile_type     varchar(50)
);

create table shedlock
(
    name       varchar(64)  not null
        primary key,
    lock_until timestamp    not null,
    locked_at  timestamp    not null,
    locked_by  varchar(255) not null
);

