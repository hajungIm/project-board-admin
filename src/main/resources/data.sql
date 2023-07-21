insert into user_account (user_id, user_password, role_types, nickname, email, memo, created_at, created_by, modified_at,
                          modified_by)
values ('uno', '{noop}1234', 'USER', 'Uno', 'uno@mail.com', 'I am Uno.', now(), 'uno', now(), 'uno'),
       ('mark', '{noop}1234', 'MANAGER', 'Mark', 'mark@mail.com', 'I am Mark.', now(), 'mark', now(), 'mark'),
       ('susan', '{noop}1234', 'MANAGER,DEVELOPER', 'Susan', 'susan@mail.com', 'I am Susan.', now(), 'susan', now(), 'susan'),
       ('ihj', '{noop}1234', 'ADMIN', 'Ihj', 'ihj@mail.com', 'I am Ihj.', now(), 'ihj', now(), 'ihj')
;
