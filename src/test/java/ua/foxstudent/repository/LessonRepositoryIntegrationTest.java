package ua.foxstudent.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxstudent.domain.schedule.Lesson;

@DataJpaTest(properties = "spring.datasource.url=jdbc:tc:postgresql:12:///ums_db")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LessonRepositoryIntegrationTest {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private DayScheduleRepository dayScheduleRepository;

    @Autowired
    private AcademicHourRepository academicHourRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @DisplayName("LessonRepository::Save should save new Lesson to DB lesson is valid")
    @Test
    public void lessonRepositorySaveShouldSaveNewLessonToDatabaseWhenPassValidParameters() {
        // given:
        var daySchedule = dayScheduleRepository.findById(1L).orElseThrow();
        var hour = academicHourRepository.findById(1L).orElseThrow();
        var classroom = classroomRepository.findById(1L).orElseThrow();
        var group = groupRepository.findById(1L).orElseThrow();
        var course = courseRepository.findById(1L).orElseThrow();
        var teacher = teacherRepository.findById(1L).orElseThrow();

        var lesson = new Lesson(daySchedule, hour, classroom, group, course, teacher);

        // when:
        var savedLesson = lessonRepository.save(lesson);

        // expect:
        Assertions.assertEquals(lesson, savedLesson);
    }

    @DisplayName("LessonRepository::Save should thrown validation exception if Lesson.daySchedule is null")
    @Test
    public void lessonRepositorySaveShouldThrownConstraintViolationExceptionWhenDayScheduleIsNull() {
        // given:
        var hour = academicHourRepository.findById(1L).orElseThrow();
        var classroom = classroomRepository.findById(1L).orElseThrow();
        var group = groupRepository.findById(1L).orElseThrow();
        var course = courseRepository.findById(1L).orElseThrow();
        var teacher = teacherRepository.findById(1L).orElseThrow();

        var lesson = new Lesson(null, hour, classroom, group, course, teacher);


        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            lessonRepository.save(lesson);
            lessonRepository.flush();
        });
    }

    @DisplayName("LessonRepository::Save should thrown validation exception if Lesson.academicHour is null")
    @Test
    public void lessonRepositorySaveShouldThrownConstraintViolationExceptionWhenAcademicHourIsNull() {
        // given:
        var daySchedule = dayScheduleRepository.findById(1L).orElseThrow();
        var classroom = classroomRepository.findById(1L).orElseThrow();
        var group = groupRepository.findById(1L).orElseThrow();
        var course = courseRepository.findById(1L).orElseThrow();
        var teacher = teacherRepository.findById(1L).orElseThrow();

        var lesson = new Lesson(daySchedule, null, classroom, group, course, teacher);


        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            lessonRepository.save(lesson);
            lessonRepository.flush();
        });
    }

    @DisplayName("LessonRepository::Save should thrown validation exception if Lesson.classroom is null")
    @Test
    public void lessonRepositorySaveShouldThrownConstraintViolationExceptionWhenClassroomIsNull() {
        // given:
        var daySchedule = dayScheduleRepository.findById(1L).orElseThrow();
        var hour = academicHourRepository.findById(1L).orElseThrow();
        var group = groupRepository.findById(1L).orElseThrow();
        var course = courseRepository.findById(1L).orElseThrow();
        var teacher = teacherRepository.findById(1L).orElseThrow();

        var lesson = new Lesson(daySchedule, hour, null, group, course, teacher);


        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            lessonRepository.save(lesson);
            lessonRepository.flush();
        });
    }

    @DisplayName("LessonRepository::Save should thrown validation exception if Lesson.group is null")
    @Test
    public void lessonRepositorySaveShouldThrownConstraintViolationExceptionWhenGroupIsNull() {
        // given:
        var daySchedule = dayScheduleRepository.findById(1L).orElseThrow();
        var hour = academicHourRepository.findById(1L).orElseThrow();
        var classroom = classroomRepository.findById(1L).orElseThrow();
        var course = courseRepository.findById(1L).orElseThrow();
        var teacher = teacherRepository.findById(1L).orElseThrow();

        var lesson = new Lesson(daySchedule, hour, classroom, null, course, teacher);


        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            lessonRepository.save(lesson);
            lessonRepository.flush();
        });
    }

    @DisplayName("LessonRepository::Save should thrown validation exception if Lesson.course is null")
    @Test
    public void lessonRepositorySaveShouldThrownConstraintViolationExceptionWhenCourseIsNull() {
        // given:
        var daySchedule = dayScheduleRepository.findById(1L).orElseThrow();
        var hour = academicHourRepository.findById(1L).orElseThrow();
        var classroom = classroomRepository.findById(1L).orElseThrow();
        var group = groupRepository.findById(1L).orElseThrow();
        var teacher = teacherRepository.findById(1L).orElseThrow();

        var lesson = new Lesson(daySchedule, hour, classroom, group, null, teacher);


        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            lessonRepository.save(lesson);
            lessonRepository.flush();
        });
    }

    @DisplayName("LessonRepository::Save should thrown validation exception if Lesson.teacher is null")
    @Test
    public void lessonRepositorySaveShouldThrownConstraintViolationExceptionWhenTeacherIsNull() {
        // given:
        var daySchedule = dayScheduleRepository.findById(1L).orElseThrow();
        var hour = academicHourRepository.findById(1L).orElseThrow();
        var classroom = classroomRepository.findById(1L).orElseThrow();
        var group = groupRepository.findById(1L).orElseThrow();
        var course = courseRepository.findById(1L).orElseThrow();

        var lesson = new Lesson(daySchedule, hour, classroom, group, course, null);


        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            lessonRepository.save(lesson);
            lessonRepository.flush();
        });
    }

    @DisplayName("LessonRepository::FindById should return existing Lesson from DB")
    @Test
    void lessonRepositoryFindByIdShouldReturnExistingLessonFromDatabase() {
        // given:
        long id = 1L;

        // when:
        var lesson = lessonRepository.findById(id).orElseThrow();

        // expect:
        Assertions.assertEquals(id, lesson.getId());
    }

    @DisplayName("LessonRepository::FindById should return Optional.empty from DB if Lesson is not found")
    @Test
    void lessonRepositoryFindByIdShouldReturnOptionalEmptyFromDatabaseIfLessonIsNotExists() {
        // given:
        long id = 100L;

        // when:
        var optionalLesson = lessonRepository.findById(id);

        // expect:
        Assertions.assertTrue(optionalLesson.isEmpty());
    }

    @DisplayName("LessonRepository::Update should update Lesson if parameters are valid")
    @Test
    void lessonRepositoryUpdateShouldUpdateExistingLessonInDatabase() {
        // given:
        long id = 1L;
        var classroomForUpdate = classroomRepository.findById(3L).orElseThrow();

        // when:
        var lessonToUpdate = lessonRepository.findById(id).orElseThrow();
        lessonToUpdate.setClassroom(classroomForUpdate);
        lessonRepository.save(lessonToUpdate);
        lessonRepository.flush();

        // then:
        var updatedGroup = lessonRepository.findById(id).orElseThrow();

        // expect:
        Assertions.assertEquals(classroomForUpdate, updatedGroup.getClassroom());
    }

    @DisplayName("LessonRepository::DeleteById should remove existing lesson from DB")
    @Test
    void lessonRepositoryDeleteByIdShouldRemoveExistingLessonFromDatabase() {
        // given:
        long id = 1L;

        // when:
        lessonRepository.deleteById(id);
        var deletedLesson = lessonRepository.findById(id);

        // expect:
        Assertions.assertTrue(deletedLesson.isEmpty());
    }
}