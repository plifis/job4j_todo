create table items(
    id serial primary key,
    description VARCHAR NOT NULL,
    created TIMESTAMP NOT NULL,
    done BOOLEAN NOT NULL,
    user_id INT NOT NULL REFERENCES user(id),
    category_id INT NOT NULL REFERENCES categories(id)
);

create table users (
    id serial primary key,
    name VARCHAR (200) NOT NULL UNIQUE,
    email VARCHAR (50) NOT NULl UNIQUE,
    password VARCHAR (20) NOT NULL
);
create table categories (
    id serial primary key,
    name VARCHAR (50) NOT NULL,
);
