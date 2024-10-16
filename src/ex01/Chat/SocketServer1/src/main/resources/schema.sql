CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL PRIMARY KEY UNIQUE,
    username VARCHAR(255),
    password VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS messages
(
    id     BIGSERIAL PRIMARY KEY UNIQUE,
    sender varchar(255),
    text   TEXT,
    time   TIMESTAMP DEFAULT LOCALTIMESTAMP(0)
);