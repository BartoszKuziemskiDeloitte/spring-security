create table users (
    id integer generated always as identity,
    username text not null,
    password text not null,
    is_account_non_expired boolean not null default true,
    is_account_non_locked boolean not null default true,
    is_credentials_non_expired boolean not null default true,
    is_enabled boolean not null default false,

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
    user_id integer not null,
    authority_id integer not null,

    constraint uq__users_authorities__user_id__authority_id unique (user_id, authority_id),
    constraint fk__users_authorities__user_id foreign key (user_id) references users (id),
    constraint fk__users_authorities__authority_id foreign key (authority_id) references authorities (id)
);