CREATE TABLE IF NOT EXISTS person (
    id BIGINT auto_increment NOT NULL,
    name varchar(50) NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS person_bootcamp (
    person_id BIGINT NOT NULL,
    bootcamp_id BIGINT NOT NULL,
    CONSTRAINT person_bootcamp_pk PRIMARY KEY (person_id, bootcamp_id)
);