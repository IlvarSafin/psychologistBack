alter table if exists appointment drop constraint if exists FK4apif2ewfyf14077ichee8g06;
alter table if exists appointment drop constraint if exists FK6sdkej5seltwdw95l97rmkxxf;
alter table if exists appointment drop constraint if exists FKd66unbcirxqwsy40gyv7oyb5d;
alter table if exists appointment_day drop constraint if exists FK2vphfxmpae6b9eqv62t1jo0ab;
alter table if exists appointment_day_dates drop constraint if exists FKl7gyrnf77s9jd1dwcruq8fl9t;
alter table if exists document_psy drop constraint if exists FKjn0csa0nuwwruw1by8hdioh1w;
alter table if exists psuchologist_filter drop constraint if exists FKix5xie7k9rft645865w6mbwaq;
alter table if exists psychologist_changes drop constraint if exists FKgo7v7expyttkwrqmgy1ha8o3k;
alter table if exists review drop constraint if exists FK9lv11ny63mbk7x66uweefc65r;
alter table if exists review drop constraint if exists FKddymrfs7e3ld6tlpbj43ni7ef;
alter table if exists user_role drop constraint if exists FKj345gk1bovqvfame88rcx7yyx;
alter table if exists users drop constraint if exists FKo3latlk97ayl9tfugde13h51t;
alter table if exists users drop constraint if exists FKj4ivnst88nql42volou8i6ks3;
drop table if exists appointment cascade;
drop table if exists appointment_day cascade;
drop table if exists appointment_day_dates cascade;
drop table if exists document_psy cascade;
drop table if exists patient cascade;
drop table if exists psuchologist_filter cascade;
drop table if exists psychologist cascade;
drop table if exists psychologist_changes cascade;
drop table if exists review cascade;
drop table if exists user_role cascade;
drop table if exists users cascade;
create table appointment (id serial not null, patient_id integer, psychologist_id integer, review_id integer unique, status boolean, date timestamp(6), link varchar(255), primary key (id));
create table appointment_day (id serial not null, psychologist_id integer, day_of_week varchar(255) check (day_of_week in ('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY')), primary key (id));
create table appointment_day_dates (appointment_day_id integer not null, dates time(6));
create table document_psy (id serial not null, psy_id integer, document oid, primary key (id));
create table patient (id serial not null, name varchar(255), money numeric, primary key (id));
create table psuchologist_filter (psychologist_id integer not null, specializations smallint check (specializations between 0 and 5));
create table psychologist (age integer, experience float(53), id serial not null, price float(53), sex char(1), status boolean, description varchar(255), name varchar(255), photo oid, primary key (id));
create table psychologist_changes (id serial not null, psychologist_id integer unique, description varchar(255), primary key (id));
create table review (id serial not null, is_liked boolean, patient_id integer, psy_id integer, status boolean, text varchar(255), primary key (id));
create table user_role (roles smallint check (roles between 0 and 2), user_id integer not null);
create table users (id serial not null, patient_id integer unique, psychologist_id integer unique, status boolean default false, email varchar(255), password varchar(255), confirm_code varchar(255), primary key (id));
create table refreshtoken (
                              id  bigserial not null,
                              expiry_date timestamp not null,
                              token varchar(255) not null,
                              user_id int4,
                              primary key (id)
);
alter table if exists appointment add constraint FK4apif2ewfyf14077ichee8g06 foreign key (patient_id) references patient;
alter table if exists appointment add constraint FK6sdkej5seltwdw95l97rmkxxf foreign key (psychologist_id) references psychologist;
alter table if exists appointment add constraint FKd66unbcirxqwsy40gyv7oyb5d foreign key (review_id) references review;
alter table if exists appointment_day add constraint FK2vphfxmpae6b9eqv62t1jo0ab foreign key (psychologist_id) references psychologist;
alter table if exists appointment_day_dates add constraint FKl7gyrnf77s9jd1dwcruq8fl9t foreign key (appointment_day_id) references appointment_day;
alter table if exists document_psy add constraint FKjn0csa0nuwwruw1by8hdioh1w foreign key (psy_id) references psychologist;
alter table if exists psuchologist_filter add constraint FKix5xie7k9rft645865w6mbwaq foreign key (psychologist_id) references psychologist;
alter table if exists psychologist_changes add constraint FKgo7v7expyttkwrqmgy1ha8o3k foreign key (psychologist_id) references psychologist;
alter table if exists review add constraint FK9lv11ny63mbk7x66uweefc65r foreign key (patient_id) references patient;
alter table if exists review add constraint FKddymrfs7e3ld6tlpbj43ni7ef foreign key (psy_id) references psychologist;
alter table if exists user_role add constraint FKj345gk1bovqvfame88rcx7yyx foreign key (user_id) references users;
alter table if exists users add constraint FKo3latlk97ayl9tfugde13h51t foreign key (patient_id) references patient;
alter table if exists users add constraint FKj4ivnst88nql42volou8i6ks3 foreign key (psychologist_id) references psychologist;
alter table refreshtoken add constraint UK_or156wbneyk8noo4jstv55ii3 unique (token);
alter table refreshtoken add constraint FKh82w2wygmoigmoxpv2iv5qdww foreign key (user_id) references users;