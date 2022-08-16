create table if not exists MPA
(
    MPA_ID   INTEGER,
    MPA_NAME VARCHAR,
    constraint MPA_ID
        primary key (MPA_ID)
);

create table if not exists FILMS
(
    FILM_ID          INTEGER auto_increment primary key,
    FILM_NAME        CHARACTER VARYING(100) not null,
    FILM_DESCRIPTION CHARACTER VARYING(300) not null,
    RELEASE_DATE     DATE                   not null,
    DURATION         INTEGER                not null,
    RATE             INTEGER,
    MPA_ID           INTEGER,
    constraint MPA_ID_FK
        foreign key (MPA_ID) references MPA
);

create table if not exists USERS
(
    USER_ID    INTEGER auto_increment primary key,
    USER_EMAIL CHARACTER VARYING(100) not null,
    USER_LOGIN CHARACTER VARYING(50)  not null,
    USER_NAME  CHARACTER VARYING(100) not null,
    BIRTHDAY   DATE                   not null
);

create table if not exists LIKES
(
    FILM_ID INTEGER,
    USER_ID INTEGER,
    constraint FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint USER_ID_FK
        foreign key (USER_ID) references USERS
);

create unique index if not exists EMAIL_UNQ
    on USERS (USER_EMAIL);

create unique index if not exists LOGIN_UNQ
    on USERS (USER_LOGIN);



CREATE TABLE if not exists GENRES
(
    GENRE_ID   INTEGER primary key,
    GENRE_NAME VARCHAR
);

CREATE TABLE if not exists FILM_GENRES
(
    FILM_ID  INTEGER,
    GENRE_ID INTEGER,
    constraint FILM_ID_FK_FOR_GENRES
        foreign key (FILM_ID) references FILMS,
    constraint GENRE_ID_FK
        foreign key (GENRE_ID) references GENRES
);

CREATE TABLE if not exists FRIENDS
(
    USER_ID INTEGER,
    FRIEND_ID INTEGER,
    constraint USER_ID_FK_FRIENDS
        foreign key (USER_ID) references USERS,
    constraint FRIEND_ID_FK
        foreign key (FRIEND_ID) references USERS (USER_ID)
)