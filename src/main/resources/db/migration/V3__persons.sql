CREATE SCHEMA IF NOT EXISTS users;

CREATE TABLE IF NOT EXISTS users.persons (
	id varchar(36) NULL,
	name varchar(255) NULL,
	email varchar(255) NULL,
	CONSTRAINT persons_pk PRIMARY KEY (id)
);
