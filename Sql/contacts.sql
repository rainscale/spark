create database if not exists demo;

use demo;

create table if not exists contacts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    birthday date,
    email VARCHAR(128) default NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    address VARCHAR(256) default null,
    gender varchar(2) default null,
    create_date datetime not null,
    last_update timestamp null default current_timestamp on update current_timestamp,
    title varchar(128),
    status boolean default true  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into contacts (name, birthday, email, phone, address, gender, create_date) values ('张三', '1984-05-20', 'zhangsan@hotmail.com', '13812345678', '北京市海淀区苏州街3号', '男', '2022-11-24 10:32:31');
insert into contacts (name, birthday, email, phone, address, gender, create_date) values ('李四', '1993-10-13', 'lisi@gmail.com', '13766663202', '广州市黄埔区香雪三路1号', '男', '2023-03-20 8:30:52');
insert into contacts (name, birthday, email, phone, address, gender, create_date) values ('王五', '2001-07-26', 'wangwu@hotmail.com', '18714678962', '上海市浦东区丰和路1号', '男', '2024-09-18 21:08:20');

alter table contacts change roles title varchar(64);
