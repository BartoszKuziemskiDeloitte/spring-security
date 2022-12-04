insert into roles (role_type)
values ('ROLE_USER'),
       ('ROLE_ADMIN'),
       ('ROLE_SUPERADMIN');

insert into users (username, password, first_name, surname)
values ('bartek', 'user', 'Bartek', 'Kuziemski'),
       ('adam', 'user', 'Adam', 'Kowalewski'),
       ('admin', 'admin', 'Adminiak', 'Adminiakowy'),
       ('superadmin', 'admin', 'Super', 'Admiński');

insert into users_roles (user_id, role_id)
values (1, 1),
       (2, 1),
       (3, 2),
       (4, 3);
