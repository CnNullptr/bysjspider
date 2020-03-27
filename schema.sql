create table class
(
    id      varchar(10) not null comment '班号'
        primary key,
    college varchar(50) null comment '学院名',
    name    varchar(50) not null comment '专业名'
)
    comment '班级信息';

create table project
(
    student_id varchar(12)              not null,
    name       text                     null comment '课题名称',
    type       varchar(50) default '未知' null comment '课题类型',
    property   varchar(50) default '未知' null comment '课题性质',
    teacher    varchar(10) default '未知' null comment '教室姓名'
)
    comment '课设项目信息';

create table student
(
    id       varchar(12)     not null comment '学号',
    class_id varchar(10)     not null,
    name     varchar(12)     not null comment '姓名',
    sex      enum ('男', '女') not null,
    constraint student_number_uindex
        unique (id),
    constraint student_class_id_fk
        foreign key (class_id) references class (id)
            on update cascade on delete cascade
)
    comment '学生信息';

alter table student
    add primary key (id);

