select * from students

select * from students where age > 10 and age < 20;

select s."name" from students as s;

select * from students where name like '%a%' or name like '%o%';

select * from students where age < "id";

select * from students order by age;

select faculty.* from faculty, students
where faculty.id = students.faculty_id and students.name like '%Potter%';

select students.* from faculty, students
where faculty.id = students.faculty_id and faculty."id" = 3;
