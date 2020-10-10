create table Quote
(
    id int auto_increment,
    quote varchar(300) not null,
    type int default 1 not null,
    constraint Quote_id_uindex
        unique (id)
);

alter table Quote
    add primary key (id);

create table User
(
    id int auto_increment
        primary key,
    username varchar(15) not null,
    score int default 0 not null,
    updated bigint default 0 null,
    constraint Users_username_uindex
        unique (username)
);

create index Users_id_username_index
    on User (id, username);

create index Users_score_index
    on User (score desc);

create index Users_username_score_index
    on User (username asc, score desc);

