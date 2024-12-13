-- Drop table

-- DROP TABLE public.users;

CREATE TABLE public.users (
	id bigserial NOT NULL,
	username varchar(50) NOT NULL,
	email varchar(255) NOT NULL,
	tax_code varchar(16) NOT NULL,
	nome varchar(50) NOT NULL,
	cognome varchar(50) NOT NULL,
	CONSTRAINT users_email_key UNIQUE (email),
	CONSTRAINT users_pkey PRIMARY KEY (id)
);

--Insert first users
INSERT INTO public.users
(id, username, email, tax_code, nome, cognome)
VALUES(1, 'admin', 'admin_user@intesigroup.com', 'WKEKJFFJJRRIEIEI', 'admin', 'admin');
INSERT INTO public.users
(id, username, email, tax_code, nome, cognome)
VALUES(2, 'creator_user', 'creator_user@intesigroup.com', 'SMIJNE75B59T227Z', 'creator', 'user');
INSERT INTO public.users
(id, username, email, tax_code, nome, cognome)
VALUES(3, 'reader_user', 'reader_user@intesigroup.com', 'SMIJNE75B59T117Z', 'reader', 'user');




CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO roles (name) VALUES
    ('OWNER'),
    ('OPERATOR'),
    ('MAINTAINER'),
    ('DEVELOPER'),
    ('REPORTER'),
    ('USER');

-- Drop table

-- DROP TABLE public.user_roles;

CREATE TABLE public.user_roles (
	user_id int8 NOT NULL,
	role_id int8 NOT NULL,
	CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id),
	CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
	CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO public.user_roles
(user_id, role_id)
VALUES(1, 1);
INSERT INTO public.user_roles
(user_id, role_id)
VALUES(2, 2);
INSERT INTO public.user_roles
(user_id, role_id)
VALUES(3, 6);

