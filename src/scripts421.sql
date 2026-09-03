ALTER TABLE students ADD CONSTRAINT chk_student_age CHECK (age >= 16);

ALTER TABLE students ALTER COLUMN name SET NOT NULL;
ALTER TABLE students ADD CONSTRAINT unq_student_name UNIQUE (name);

ALTER TABLE faculty ADD CONSTRAINT unq_faculty_name_color UNIQUE (name, color);

ALTER TABLE students ALTER COLUMN age SET DEFAULT 20;
