create database if not exists demo;

use demo;

create table if not exists users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(256) NOT NULL,
    password VARCHAR(128) default NULL,
    roles VARCHAR(64) default "editor",
    create_date datetime not null,
    last_update timestamp null default current_timestamp on update current_timestamp,
    status boolean default true
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into users (username, password, roles,  create_date) values ('root', '123456', 'admin', '2024-10-1 10:10:10');
