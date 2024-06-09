insert into patient(id, name, money)
values (0, 'Ivan', 500),
       (1, 'Mihail', 6000),
       (2, 'Darya', 1222);


insert into psychologist(id, age, experience, price, sex, status, description, name)
values (0, 24, 4.5, 1700, 'M', true, 'i am psychologist Ilya', 'Ilya'),
       (1, 34, 5, 2900, 'M', true, 'i am psychologist Michail', 'Michail'),
       (2, 30, 3.6, 4300, 'M', false, 'i am psychologist Nikita', 'Nikita'),
       (3, 26, 5, 1800, 'W', true, 'i am psychologist Mary', 'Darya');

-- password - 123
insert into users(id, patient_id, psychologist_id, status, email, password)
values (0, 1, 1, true, 'il@bk.ru', '$2y$10$ORx3XmqwLEW4KvLWnoOCdOomJPaNPk.XTGGiVUwgXhuJYi1GccCpu'),
       (1, 2, 3, true, 'd@bk.ru', '$2y$10$ORx3XmqwLEW4KvLWnoOCdOomJPaNPk.XTGGiVUwgXhuJYi1GccCpu');

insert into users(id, psychologist_id, status, email, password)
values (2, 0, true, 'i@bk.ru', '$2y$10$ORx3XmqwLEW4KvLWnoOCdOomJPaNPk.XTGGiVUwgXhuJYi1GccCpu'),
       (3, 2, true, 'n@bk.ru', '$2y$10$ORx3XmqwLEW4KvLWnoOCdOomJPaNPk.XTGGiVUwgXhuJYi1GccCpu');

insert into users(id, patient_id, status, email, password)
values (4, 0, true, 'iv@bk.ru', '$2y$10$ORx3XmqwLEW4KvLWnoOCdOomJPaNPk.XTGGiVUwgXhuJYi1GccCpu');

insert into user_role(roles, user_id)
values (0, 0),
       (1, 0),
       (1, 2),
       (1, 3),
       (0, 4);

insert into review(is_liked, patient_id, psy_id, text, status)
values (true, 0, 1, 'good', true),
       (true, 0, 1, 'gas', true),
       (true, 0, 1, 'bless', true),
       (true, 0, 1, 'ohh', true),
       (true, 0, 1, 'okay', true),
       (true, 0, 1, 'dd', true),
       (true, 0, 1, 'asdfg', true);