CREATE TABLE courses
(
    id                 BIGSERIAL,
    course_name        TEXT NOT NULL,
    course_description TEXT NOT NULL,
    update_at          TIMESTAMP,
    create_at          TIMESTAMP,
    CONSTRAINT  pk_courses_id           PRIMARY KEY (id),
    CONSTRAINT  uq_courses_course_name  UNIQUE (course_name)
);
CREATE TABLE academic_groups
(
    id         BIGSERIAL,
    group_name TEXT NOT NULL,
    update_at  TIMESTAMP,
    create_at  TIMESTAMP,
    CONSTRAINT pk_academic_groups_id    PRIMARY KEY (id),
    CONSTRAINT uq_academic_groups_name  UNIQUE (group_name)
);
CREATE TABLE users
(
    id          BIGSERIAL,
    email       TEXT NOT NULL,
    password    TEXT NOT NULL,
    update_at   TIMESTAMP,
    create_at   TIMESTAMP,
    CONSTRAINT pk_users_id    PRIMARY KEY (id),
    CONSTRAINT uq_users_email UNIQUE (email)
);
CREATE TABLE user_roles
(
    user_id BIGINT,
    roles TEXT,
    CONSTRAINT pk_user_roles_id             PRIMARY KEY (user_id),
    CONSTRAINT fk_user_roles_user_id_users  FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE INDEX idx_users_email ON users (email);
CREATE TABLE students
(
    id              BIGSERIAL,
    user_id         BIGINT,
    group_id        BIGINT,
    first_name      TEXT        NOT NULL,
    last_name       TEXT        NOT NULL,
    city            TEXT        NOT NULL,
    street          TEXT        NOT NULL,
    number          TEXT        NOT NULL,
    apt             TEXT,
    zip             TEXT        NOT NULL,
    phone    TEXT        NOT NULL,
    update_at       TIMESTAMP,
    create_at       TIMESTAMP,
    CONSTRAINT pk_students_id       PRIMARY KEY (id),
    CONSTRAINT fk_students_users    FOREIGN KEY (user_id)   REFERENCES users (id),
    CONSTRAINT fk_students_groups   FOREIGN KEY (group_id)  REFERENCES academic_groups (id),
    CONSTRAINT uq_students_phone UNIQUE (phone)

);
CREATE INDEX idx_students_user_id  ON  students (user_id);
CREATE INDEX idx_students_group_id ON students (group_id);
CREATE TABLE students_courses
(
    student_id BIGINT,
    course_id  BIGINT,
    CONSTRAINT pk_students_courses          PRIMARY KEY (student_id, course_id),
    CONSTRAINT fk_students_courses_students FOREIGN KEY (student_id)    REFERENCES students (id),
    CONSTRAINT fk_students_courses_courses  FOREIGN KEY (course_id)     REFERENCES courses (id)
);
CREATE INDEX idx_students_courses_student_id ON students_courses (student_id);
CREATE INDEX idx_students_courses_course_id  ON students_courses (course_id);
CREATE TABLE teachers
(
    id                BIGSERIAL,
    user_id           BIGINT,
    first_name        TEXT        NOT NULL,
    last_name         TEXT        NOT NULL,
    academic_degree   TEXT,
    professional_post TEXT,
    city              TEXT        NOT NULL,
    street            TEXT        NOT NULL,
    number            TEXT        NOT NULL,
    apt               TEXT,
    zip               TEXT        NOT NULL,
    phone      TEXT        NOT NULL,
    update_at         TIMESTAMP,
    create_at         TIMESTAMP,
    CONSTRAINT pk_teachers_id    PRIMARY KEY (id),
    CONSTRAINT fk_teachers_users FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT uq_teachers_phone UNIQUE (phone)
);
CREATE INDEX idx_teachers_user_id ON teachers (user_id);
CREATE TABLE teachers_courses
(
    teacher_id BIGINT,
    course_id  BIGINT,
    CONSTRAINT pk_teachers_courses          PRIMARY KEY (teacher_id, course_id),
    CONSTRAINT fk_teachers_courses_teachers FOREIGN KEY (teacher_id)    REFERENCES teachers (id),
    CONSTRAINT fk_teachers_courses_courses  FOREIGN KEY (course_id)     REFERENCES courses (id)
);
CREATE INDEX idx_teachers_courses_teacher_id ON teachers_courses (teacher_id);
CREATE INDEX idx_teachers_courses_course_id  ON teachers_courses (course_id);
CREATE TABLE classrooms
(
    id                    BIGSERIAL,
    classroom_number      TEXT NOT NULL,
    classroom_description TEXT,
    capacity              INT  NOT NULL,
    update_at             TIMESTAMP,
    create_at             TIMESTAMP,
    CONSTRAINT pk_classrooms                    PRIMARY KEY (id),
    CONSTRAINT uq_classrooms_classroom_number   UNIQUE (classroom_number)
);
CREATE TABLE academic_hours
(
    id        BIGSERIAL,
    name      TEXT,
    begin_at  TIME NOT NULL,
    end_at    TIME NOT NULL,
    update_at TIMESTAMP,
    create_at TIMESTAMP,
    CONSTRAINT pk_academic_hours        PRIMARY KEY (id),
    CONSTRAINT uq_academic_hours_name   UNIQUE (name)
);
CREATE TABLE day_schedule
(
    id         BIGSERIAL,
    date       DATE                 NOT NULL,
    update_at  TIMESTAMP,
    create_at  TIMESTAMP,
    CONSTRAINT pk_day_schedule      PRIMARY KEY (id),
    CONSTRAINT uq_day_schedule_date UNIQUE (date)
);
CREATE TABLE lessons
(
    id               BIGSERIAL,
    day_schedule_id  BIGINT,
    academic_hour_id BIGINT,
    classroom_id     BIGINT     NOT NULL,
    group_id         BIGINT     NOT NULL,
    course_id        BIGINT     NOT NULL,
    teacher_id       BIGINT     NOT NULL,
    update_at        TIMESTAMP,
    create_at        TIMESTAMP,
    CONSTRAINT pk_lessons                   PRIMARY KEY (id),
    CONSTRAINT fk_lessons_day_schedule      FOREIGN KEY (day_schedule_id)   REFERENCES day_schedule (id),
    CONSTRAINT fk_lessons_academic_hours    FOREIGN KEY (academic_hour_id)  REFERENCES academic_hours (id),
    CONSTRAINT fk_lessons_classrooms        FOREIGN KEY (classroom_id)      REFERENCES classrooms (id),
    CONSTRAINT fk_lessons_groups            FOREIGN KEY (group_id)          REFERENCES academic_groups (id),
    CONSTRAINT fk_lessons_courses           FOREIGN KEY (course_id)         REFERENCES courses (id),
    CONSTRAINT fk_lessons_teachers          FOREIGN KEY (teacher_id)        REFERENCES teachers (id)
);
CREATE INDEX idx_lessons_day_schedule_id    ON lessons (day_schedule_id);
CREATE INDEX idx_lessons_academic_hour_id   ON lessons (academic_hour_id);
CREATE INDEX idx_lessons_classroom_id       ON lessons (academic_hour_id);
CREATE INDEX idx_lessons_group_id           ON lessons (group_id);
CREATE INDEX idx_lessons_course_id          ON lessons (course_id);
CREATE INDEX idx_lessons_teacher_id         ON lessons (teacher_id);