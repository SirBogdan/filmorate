create table if not exists FILMS
(
    FILM_ID          INTEGER auto_increment,
    FILM_NAME        CHARACTER VARYING(100) not null,
    FILM_DESCRIPTION CHARACTER VARYING(300) not null,
    RELEASE_DATE     DATE not null,
    DURATION         INTEGER not null,
    constraint FILM_ID
        primary key (FILM_ID)
);

create table if not exists USERS
(
    USER_ID   INTEGER auto_increment,
    EMAIL     CHARACTER VARYING(100) not null,
    LOGIN     CHARACTER VARYING(50)  not null,
    USER_NAME CHARACTER VARYING(100) not null,
    BIRTHDAY  DATE not null,
    constraint USER_ID
        primary key (USER_ID)
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
    on USERS (EMAIL);

create unique index if not exists LOGIN_UNQ
    on USERS (LOGIN);
