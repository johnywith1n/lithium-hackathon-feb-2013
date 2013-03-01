# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table company (
  uid                       bigint not null,
  name                      varchar(255) not null,
  bag_of_words_transform    blob,
  constraint pk_company primary key (uid))
;

create table sample_document (
  uid                       bigint not null,
  company_uid               bigint,
  url                       clob,
  body                      clob,
  vector                    blob,
  constraint pk_sample_document primary key (uid))
;

create sequence company_seq;

create sequence sample_document_seq;

alter table sample_document add constraint fk_sample_document_company_1 foreign key (company_uid) references company (uid) on delete restrict on update restrict;
create index ix_sample_document_company_1 on sample_document (company_uid);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists company;

drop table if exists sample_document;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists company_seq;

drop sequence if exists sample_document_seq;

