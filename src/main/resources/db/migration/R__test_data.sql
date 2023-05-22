INSERT INTO courses (course_name, course_description)
VALUES
('Mathematics', 'Calculus, algebra, and geometry'),
('English Literature', 'Shakespeare, poetry, and novels'),
('Computer Science', 'Programming and algorithms'),
('Biology', 'Anatomy, ecology, and genetics');

INSERT INTO academic_groups (group_name)
VALUES
('Group A'),
('Group B'),
('Group C');

INSERT INTO users (email, password)
VALUES
('johndoe@example.com', 'password1'),
('janedoe@example.com', 'password2'),
('bobsmith@example.com', 'password3'),
('jillsmith@example.com', 'password4'),
('alicejohnson@example.com', 'password5'),
('bobbrown@example.com', 'password6'),
('charliechaplin@example.com', 'password7');

INSERT INTO user_roles (user_id, roles)
VALUES
(1, 'ROLE_STUDENT'),
(2, 'ROLE_STUDENT'),
(3, 'ROLE_STUDENT'),
(4, 'ROLE_STUDENT'),
(5, 'ROLE_TEACHER'),
(6, 'ROLE_TEACHER'),
(7, 'ROLE_TEACHER, ROLE_ADMIN');

INSERT INTO students
(user_id, group_id, first_name, last_name, city, street, number, zip, phone)
VALUES
(1, 1, 'John', 'Doe', 'Kiyv', 'Street1', '12', '123456', '0934732402293'),
(2, 2, 'Jane', 'Doe', 'Kiyv', 'Street2', '1', '123456', '987459237498'),
(3, 3, 'Bob', 'Smith', 'Kiyv', 'Street3', '43A', '123456', '635245573910'),
(4, 1, 'Jill', 'Smith', 'Kiyv', 'Street4', '9', '123456', '97364253748');

INSERT INTO students_courses (student_id, course_id)
VALUES
(1, 1),
(1, 2),
(2, 2),
(3, 3),
(4, 4);

INSERT INTO teachers
(user_id, first_name, last_name, academic_degree, professional_post, city, street, number, zip, phone)
VALUES
(5, 'Alice', 'Johnson', 'DOCTOR', 'PROFESSOR', 'Kiyv', 'Street1', '1A', '123456', '836235476238'),
(6, 'Bob', 'Brown', 'DOCTOR', 'PROFESSOR', 'Kiyv', 'Street5', '53', '123456', '9736482346'),
(7, 'Charlie', 'Chaplin', 'DOCTOR', 'PROFESSOR', 'Kiyv', 'Street6', '33', '123456', '08374723645');

INSERT INTO teachers_courses (teacher_id, course_id)
VALUES
(1, 1),
(1, 2),
(2, 3),
(3, 4);

INSERT INTO classrooms (classroom_number, classroom_description, capacity)
VALUES
('101', 'Small room with whiteboard', 20),
('102', 'Medium room with projector', 30),
('103', 'Large room with podium', 50);

INSERT INTO academic_hours (name, begin_at, end_at)
VALUES
('Morning', '09:00:00', '12:00:00'),
('Afternoon', '13:00:00', '16:00:00'),
('Evening', '18:00:00', '21:00:00');

INSERT INTO day_schedule(date)
VALUES
('2023-03-01'),
('2023-03-02'),
('2023-03-03');

INSERT INTO lessons
(day_schedule_id, academic_hour_id, classroom_id, group_id, course_id, teacher_id)
VALUES
(1, 1, 1, 1, 1, 1),
(1, 2, 2, 2, 2, 2),
(2, 1, 1, 1, 1, 1),
(2, 2, 2, 2, 2, 2),
(3, 1, 1, 1, 1, 1),
(3, 2, 2, 2, 2, 2);
