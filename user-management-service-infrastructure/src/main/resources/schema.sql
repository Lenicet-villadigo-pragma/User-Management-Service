CREATE TABLE IF NOT EXISTS person (
    id BIGINT auto_increment NOT NULL,
    name varchar(50) NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS person_bootcamp (
    id bigint NOT NULL AUTO_INCREMENT,
    person_id bigint NOT NULL,
    bootcamp_id bigint NOT NULL,
    status_subscription int NOT NULL,
    subscribed_on datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    unsubscribed_on datetime DEFAULT NULL,
    PRIMARY KEY (id)
);