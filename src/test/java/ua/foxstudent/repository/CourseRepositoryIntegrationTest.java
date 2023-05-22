package ua.foxstudent.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.validation.ConstraintViolationException;
import ua.foxstudent.domain.activity.Course;

@DataJpaTest(properties = "spring.datasource.url=jdbc:tc:postgresql:12:///ums_db")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseRepositoryIntegrationTest {

    @Autowired
    private CourseRepository courseRepository;

    @DisplayName("CourseRepository::Save should save new Course to DB if it valid")
    @Test
    void courseRepositorySaveShouldSaveNewCourseToDatabaseWhenPassValidCourse() {
        // given:
        var course = new Course("Valid_name", "valid_description");

        // when:
        var savedCourse = courseRepository.save(course);

        // expect:
        Assertions.assertEquals(course, savedCourse);
    }

    @DisplayName("CourseRepository::Save should throw validation exception if Course parameters are blank")
    @ParameterizedTest
    @CsvSource({
        ",",
        " , "
    })
    void courseRepositorySaveShouldThrowConstraintViolationExceptionWhenPassInvalidCourseParameters(
        String name,
        String description) {
        // given:
        var course = new Course(name, description);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            courseRepository.save(course);
            courseRepository.flush();
        });
    }

    @DisplayName("CourseRepository::Save should throw validation exception if Course.name is null")
    @Test
    void courseRepositorySaveShouldThrowConstraintViolationExceptionWhenPassNullInsteadCourseName() {
        // given:
        var course = new Course(null, "NotNull");

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            courseRepository.save(course);
            courseRepository.flush();
        });
    }

    @DisplayName("CourseRepository::FindById should return existing Course from DB")
    @Test
    void courseRepositoryFindByIdShouldReturnExistingCourseFromDatabase() {
        // given:
        long id = 1L;

        // when:
        var course = courseRepository.findById(id).orElseThrow();

        // expect:
        Assertions.assertEquals(id, course.getId());
    }

    @DisplayName("CourseRepository::FindById should return Optional.empty if Course is not found")
    @Test
    void courseRepositoryFindByIdShouldReturnOptionalEmptyFromDatabaseIfCourseIsNotExists() {
        // given:
        long id = 100L;

        // when:
        var optionalCourse = courseRepository.findById(id);

        // expect:
        Assertions.assertTrue(optionalCourse.isEmpty());
    }

    @DisplayName("CourseRepository::Update should update Course.description")
    @Test
    void courseRepositoryUpdateShouldUpdateExistingCourseInDatabase() {
        // given:
        long id = 1L;
        var descriptionForUpdate = "Updated description";

        // when:
        var courseToUpdate = courseRepository.findById(id).orElseThrow();
        courseToUpdate.setDescription(descriptionForUpdate);
        courseRepository.save(courseToUpdate);
        courseRepository.flush();

        // then:
        var updatedCourse = courseRepository.findById(id).orElseThrow();

        // expect:
        Assertions.assertEquals(descriptionForUpdate, updatedCourse.getDescription());
    }

    @DisplayName("CourseRepository::DeleteById should remove existing Course from DB")
    @Test
    void courseRepositoryDeleteByIdShouldRemoveExistingCourseFromDatabase() {
        // given:
        long id = 1L;

        // when:
        courseRepository.deleteById(id);
        var deletedCourse = courseRepository.findById(id);

        // expect:
        Assertions.assertTrue(deletedCourse.isEmpty());
    }

    @DisplayName("CourseRepository::FindByName should return existing Course from DB")
    @Test
    void courseRepositoryFindDyNameShouldReturnExistingCourseFromDatabase() {
        // given:
        var courseName = "Mathematics";

        // when:
        var course = courseRepository.findByName(courseName).orElseThrow();

        // expect:
        Assertions.assertEquals(courseName, course.getName());
    }

    @DisplayName("CourseRepository::FindByName should return Optional.empty if Course is not found")
    @Test
    void courseRepositoryFindByNameShouldReturnOptionalEmptyFromDatabaseIfCourseIsNotExists() {
        // given:
        var courseName = "NotExistingCourse";

        // when:
        var optionalCourse = courseRepository.findByName(courseName);

        // expect:
        Assertions.assertTrue(optionalCourse.isEmpty());
    }
}