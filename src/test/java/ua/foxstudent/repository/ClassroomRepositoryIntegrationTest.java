package ua.foxstudent.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxstudent.domain.schedule.Classroom;

@DataJpaTest(properties = "spring.datasource.url=jdbc:tc:postgresql:12:///ums_db")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClassroomRepositoryIntegrationTest {

    @Autowired
    private ClassroomRepository classroomRepository;

    @DisplayName("ClassroomRepository::Save should save new Classroom to DB  if it valid")
    @Test
    void classroomRepositorySaveShouldSaveNewClassroomToDatabaseWhenPassValidClassroom() {
        // given:
        var classroom = new Classroom(10, 10);

        // when:
        var savedClassroom = classroomRepository.save(classroom);

        // expect:
        Assertions.assertEquals(classroom, savedClassroom);
    }

    @DisplayName("ClassroomRepository::Save should throw validation exception if Classroom parameters is invalid")
    @ParameterizedTest
    @CsvSource({
        "-1, 1",
        "0, 1",
        "1, -1",
        "1, 0"
    })
    void classroomRepositorySaveShouldThrowConstraintViolationExceptionWhenPassInvalidClassroomParameters(
        int number,
        int capacity) {
        // given:
        var classroom = new Classroom(number, capacity);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            classroomRepository.save(classroom);
            classroomRepository.flush();
        });
    }

    @DisplayName("ClassroomRepository::Save should throw validation exception Classroom.number is null")
    @Test
    void classroomRepositorySaveShouldThrowConstraintViolationExceptionWhenPassNullInsteadClassroomNumber() {
        // given:
        var classroom = new Classroom(null, 10);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            classroomRepository.save(classroom);
            classroomRepository.flush();
        });
    }

    @DisplayName("ClassroomRepository::Save should throw validation exception Classroom.capacity is null")
    @Test
    void classroomRepositorySaveShouldThrowConstraintViolationExceptionWhenPassNullInsteadClassroomCapacity() {
        // given:
        var classroom = new Classroom(10, null);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            classroomRepository.save(classroom);
            classroomRepository.flush();
        });
    }

    @DisplayName("ClassroomRepository::FindById should return an existing Classroom from DB")
    @Test
    void classroomRepositoryFindByIdShouldReturnExistingClassroomFromDatabase() {
        // given:
        long id = 1L;

        // when:
        var classroom = classroomRepository.findById(id).orElseThrow();

        // expect:
        Assertions.assertEquals(id, classroom.getId());
    }

    @DisplayName("ClassroomRepository::FindById should return Optional.empty from DB if Classroom is not found")
    @Test
    void classroomRepositoryFindByIdShouldReturnOptionalEmptyFromDatabaseIfClassroomIsNotExists() {
        // given:
        long id = 100L;

        // when:
        var optionalClassroom = classroomRepository.findById(id);

        // expect:
        Assertions.assertTrue(optionalClassroom.isEmpty());
    }

    @DisplayName("ClassroomRepository::Update should update existing Classroom if Classroom.capacity is valid")
    @Test
    void classroomRepositoryUpdateShouldUpdateExistingClassroomInDatabase() {
        // given:
        long id = 1L;
        int capacityForUpdate = 100;

        // when:
        var classroomToUpdate = classroomRepository.findById(id).orElseThrow();
        classroomToUpdate.setCapacity(capacityForUpdate);
        classroomRepository.save(classroomToUpdate);
        classroomRepository.flush();

        // then:
        var updatedClassroom = classroomRepository.findById(id).orElseThrow();

        // expect:
        Assertions.assertEquals(capacityForUpdate, updatedClassroom.getCapacity());
    }

    @DisplayName("ClassroomRepository::Update should throw validation exception if Classroom.capacity is invalid")
    @Test
    void classroomRepositoryUpdateShouldThrowConstraintViolationException() {
        // given:
        long id = 1L;
        int capacityForUpdate = -100;

        // when:
        var classroomToUpdate = classroomRepository.findById(id).orElseThrow();
        classroomToUpdate.setCapacity(capacityForUpdate);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            classroomRepository.save(classroomToUpdate);
            classroomRepository.flush();
        });
    }

    @DisplayName("ClassroomRepository::DeleteById should remove existing Classroom from DB")
    @Test
    void classroomRepositoryDeleteByIdShouldRemoveExistingClassroomFromDatabase() {
        // given:
        long id = 1L;

        // when:
        classroomRepository.deleteById(id);
        var deletedClassroom = classroomRepository.findById(id);

        // expect:
        Assertions.assertTrue(deletedClassroom.isEmpty());
    }
}