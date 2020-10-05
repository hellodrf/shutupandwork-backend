create table USER
(
	id int auto_increment
		primary key,
	username varchar(15) null,
	score int default 0 not null,
	updated datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
	constraint User_username_uindex
		unique (username)
);

create index id_score_index
	on Users (score, id);

create index score_index
	on Users (score);

create table QUOTE
(
    id int auto_increment
        primary key,
    quote varchar(300) not null,
    type int default 1 not null
);