create table member (
    id varchar(255) not null, -- 아이디(기본 키)
    name varchar(255),        -- 이름
    age integer not null,     -- 나이
    primary key (id)
);