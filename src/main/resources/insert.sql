insert into roles (role_type)
values ('ROLE_USER'),
       ('ROLE_ADMIN'),
       ('ROLE_SUPERADMIN');

insert into users (username, password, first_name, surname)
values ('bartek', '$2a$10$nOWMQNBCYBNT39eBl/QsyeY5wYp6m6Vla5j5O4YF/VW3RstEBRKQa', 'Bartek', 'Kuziemski'),
       ('adam', '$2a$10$nOWMQNBCYBNT39eBl/QsyeY5wYp6m6Vla5j5O4YF/VW3RstEBRKQa', 'Adam', 'Kowalewski'),
       ('admin', '$2a$10$yiuFSsYeA2asDauHMuWmNOzPJ2P4KixIScyM1dF7QLoXv7a3JHK.O', 'Adminiak', 'Adminiakowy'),
       ('superadmin', '$2a$10$3h.7vFnl7v3i80IP6r4lkeehfEHc1Bs57KN9kiYQRt6E0AAZiOl4.', 'Super', 'Admiński');

insert into users_roles (user_id, role_id)
values (1, 1),
       (2, 1),
       (3, 1),
       (3, 2),
       (4, 1),
       (4, 2),
       (4, 3);
