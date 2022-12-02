create table users (
    id integer generated always as identity,
    username text not null,
    password text not null,
    is_account_non_expired boolean not null,
    is_account_non_locked boolean not null,
    is_credentials_non_expired boolean not null,
    is_enabled boolean not null,

    constraint pk__users primary key (id),
    constraint uq__users__username unique (username)
);

create table authorities (
    id integer generated always as identity,
    authority text not null,

    constraint pk__authorities primary key (id),
    constraint uq__authorities unique (authority)
);

create table users_authorities (
    id integer generated always as identity,
    user_id integer not null,
    authority_id integer not null,

    constraint pk__users_authorities primary key (id),
    constraint fk__users_authorities__user_id foreign key (user_id) references users (id),
    constraint fk__users_authorities__authority_id foreign key (authority_id) references authorities (id)
);