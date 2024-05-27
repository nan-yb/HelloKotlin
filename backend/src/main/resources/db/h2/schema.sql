drop table if exists reservation;
create table reservation (
 id bigint primary key auto_increment,
 user_id bigint,
 number int
);

create table if not exists book (
    BOOK_ID bigint not null auto_increment,
    title_korean varchar(100) not null ,
    title_english varchar(100) not null ,
    description varchar(100) not null ,
    author varchar(100) not null ,
    isbn varchar(100) not null unique ,
    publish_date varchar(100) not null ,
    created_at datetime not null ,
    last_modified_at datetime not null ,
     primary key (book_id)
);