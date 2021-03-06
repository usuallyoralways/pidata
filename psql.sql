﻿

--删除t_te_entrypt
drop table t_te_entrypt1;
--创建t_te_entrypt

--清空数据表

truncate t_trade;

select * from t_trade limit 10;

update t_trade set tno= tno-10000


--创建表
drop table t_trade;
create TABLE public.t_trade
(
  tno serial,
  cost double precision,
  date date,
  primary key(tno)
)


truncate t_te_entrypt1;

insert into t_te_entrypt1 (select * from t_te_entrypt);
insert into t_te_entrypt (select * from t_te_entrypt1);

select count(*) from t_te_entrypt where b='ThisIsBFalseData'; 

select count(*) from t_te_entrypt;

truncate  t_te_entrypt;



select count(*) from t_trade where cost>=1 and cost<=800000;

select count(*) from t_te_entrypt where b_blockid>=0 and b_blockid<=79 and b!='ThisIsBFalseData';

select * from t_trade where tno=1;

select * from t_te_entrypt where b_blockid=456;

select count(*) from t_te_entrypt where b!='ThisIsBFalseData';

select * from t_trade where  tno=5781;

create table t_test_entrypt if not exists(
    a   varchar(50) primary key,
    b   varchar(50),
    c   varchar(50),
    a_hashvalue int;
    b_hashvalue int;
);

create TABLE public.t_mpdata
(
                id serial primary key,
                series_id varchar(20),
                year int,
                period varchar(10),
                value double precision,
                footnote_codes varchar(5)
);


create TABLE if not exists public.t_te_mpdata
                (
                  en_id varchar(50) primary key,
                  en_series_id varchar(50),
                  en_year varchar(50),
                  en_period varchar(20),
                  en_value varchar(50),
                  en_footnote_codes varchar(20),
                   a_blockid integer,
                   a_hashvalue integer,
                   a_hashpoint integer
                );


create table if not exists t_te_entrypt (
                a varchar(50),
                a_blockid int,
                a_hashvalue int,
                a_flag int,
                b varchar(100),
                b_blockid int,
                b_hashvalue int,
                c varchar(50),
                a_hashpoint int,
                b_hashpoint int,
                primary key(a)
                );
