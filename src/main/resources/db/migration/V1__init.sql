CREATE SCHEMA IF NOT EXISTS company;

CREATE TABLE IF NOT EXISTS company.companies (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255),
    permalink VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    description VARCHAR(255),
    overview VARCHAR(255)
);