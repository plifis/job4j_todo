create table items(
    id serial primary key,
    description VARCHAR NOT NULL,
    created TIMESTAMP NOT NULL,
    done BOOLEAN NOT NULL,
    user_id INT NOT NULL REFERENCES user(id)
);

create table users (
    id serial primary key,
    name VARCHAR (200) NOT NULL UNIQUE,
    email VARCHAR (50) NOT NULl UNIQUE,
    password VARCHAR (20) NOT NULL
);

create table car_marks (
    id serial primary key,
    name VARCHAR (50) NOT NULL UNIQUE
);

create table car_models (
    id serial primary key,
    name VARCHAR (50) NOT NULL UNIQUE
);