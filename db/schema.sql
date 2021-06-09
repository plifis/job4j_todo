create table item(
    id serial primary key,
    description VARCHAR NOT NULL,
    created TIMESTAMP NOT NULL,
    done BOOLEAN NOT NULL
);