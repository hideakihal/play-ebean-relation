# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table members (
  id                        bigint not null,
  name                      varchar(255),
  mail                      varchar(255),
  password                  varchar(255),
  postdate                  timestamp not null,
  constraint pk_members primary key (id))
;

create table message (
  id                        bigint not null,
  mail                      varchar(255),
  message                   varchar(255),
  postdate                  timestamp not null,
  constraint pk_message primary key (id))
;


create table message_members (
  message_id                     bigint not null,
  members_id                     bigint not null,
  constraint pk_message_members primary key (message_id, members_id))
;
create sequence members_seq;

create sequence message_seq;




alter table message_members add constraint fk_message_members_message_01 foreign key (message_id) references message (id) on delete restrict on update restrict;

alter table message_members add constraint fk_message_members_members_02 foreign key (members_id) references members (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists members;

drop table if exists message_members;

drop table if exists message;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists members_seq;

drop sequence if exists message_seq;

