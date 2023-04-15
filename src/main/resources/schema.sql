
DROP TABLE if exists USERS CASCADE;
DROP TABLE if exists FILM_GENRES CASCADE;
DROP TABLE if exists GENRES CASCADE;
DROP TABLE if exists MPA CASCADE;
DROP TABLE if exists FILM_LIKERS CASCADE;
DROP TABLE if exists USER_FRIENDS CASCADE;
DROP TABLE if exists FILMS CASCADE;

create table films
(
    id          int PRIMARY KEY AUTO_INCREMENT,
    name        varchar(100) NOT NULL,
    description varchar(200) NOT NULL,
    releaseDate timestamp    NOT NULL,
    duration    int,
    likes       int default 0,
    MPA_id      int
);

create table genres
(
    id int PRIMARY KEY,
    genre    varchar
);

create table film_genres
(
    film_id  int,
    genre_id int references genres (id)
);

create table MPA
(
    MPA_id int,
    MPA    varchar
);

create table users
(
    id       int PRIMARY KEY AUTO_INCREMENT,
    email    varchar,
    login    varchar(200) NOT NULL,
    name     varchar,
    birthday timestamp
);

create table film_likers
(
    film_id int references films (id),
    user_id int references users (id)
);

create table user_friends
(
    user_id   int,
    friend_id int,
    status    varchar
);