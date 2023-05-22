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
import ua.foxstudent.domain.person.AcademicDegree;
import ua.foxstudent.domain.person.Address;
import ua.foxstudent.domain.person.ProfessionalPost;
import ua.foxstudent.domain.person.Teacher;


@DataJpaTest(properties = "spring.datasource.url=jdbc:tc:postgresql:12:///ums_db")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TeacherRepositoryIntegrationTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @DisplayName("TeacherRepository::SaveShould save new Teacher to DB if it valid")
    @Test
    void teacherRepositorySaveShouldSaveNewTeacherToDatabaseWhenPassValidTeacher() {
        // given:
        var teacher = new Teacher();
        teacher.setFirstName("Valid_name");
        teacher.setLastName("Valid_surname");
        teacher.setPhone("+123456789");
        teacher.setAddress(new Address("City", "Street", "01A", "36428"));
        teacher.setProfessionalPost(ProfessionalPost.PROFESSOR);
        teacher.setAcademicDegree(AcademicDegree.DOCTOR);

        // when:
        var savedTeacher = teacherRepository.save(teacher);

        // expect:
        Assertions.assertEquals(teacher, savedTeacher);
    }

    @DisplayName("TeacherRepository::Save should throw validation exception Teacher parameters are invalid")
    @ParameterizedTest
    @CsvSource({
        "valid_name, valid_surname,",
        ", valid_surname, +123456789",
        "valid_name,,+123456789",
        "valid_name, valid_surname,     ",
        "      , valid_surname, +123456789",
        "valid_name,      ,+123456789"
    })
    void teacherRepositorySaveShouldThrowConstraintViolationExceptionWhenPassInvalidTeacherParameters(
        String firstName,
        String lastName,
        String phone) {
        // given:
        var teacher = new Teacher();
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacher.setPhone(phone);
        teacher.setAddress(new Address("City", "Street", "01A", "36428"));
        teacher.setProfessionalPost(ProfessionalPost.PROFESSOR);
        teacher.setAcademicDegree(AcademicDegree.DOCTOR);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            teacherRepository.save(teacher);
            teacherRepository.flush();
        });
    }

    @DisplayName("TeacherRepository::Save should throw validation exception Teacher.address is null")
    @Test
    void teacherRepositorySaveShouldThrowConstraintViolationExceptionWhenPassNullInsteadTeacherAddress() {
        // given:
        var teacher = new Teacher();
        teacher.setFirstName("Valid_name");
        teacher.setLastName("Valid_surname");
        teacher.setPhone("+123456789");
        teacher.setAddress(null);
        teacher.setProfessionalPost(ProfessionalPost.PROFESSOR);
        teacher.setAcademicDegree(AcademicDegree.DOCTOR);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            teacherRepository.save(teacher);
            teacherRepository.flush();
        });
    }

    @DisplayName("TeacherRepository::Save should throw validation exception Teacher.professionalPost is null")
    @Test
    void teacherRepositorySaveShouldThrowConstraintViolationExceptionWhenPassNullInsteadProfessionalPost() {
        // given:
        var teacher = new Teacher();
        teacher.setFirstName("Valid_name");
        teacher.setLastName("Valid_surname");
        teacher.setPhone("+123456789");
        teacher.setAddress(new Address("City", "Street", "01A", "36428"));
        teacher.setProfessionalPost(null);
        teacher.setAcademicDegree(AcademicDegree.DOCTOR);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            teacherRepository.save(teacher);
            teacherRepository.flush();
        });
    }

    @DisplayName("TeacherRepository::Save should throw validation exception Teacher.academicDegree is null")
    @Test
    void teacherRepositorySaveShouldThrowConstraintViolationExceptionWhenPassNullInsteadAcademicDegree() {
        // given:
        var teacher = new Teacher();
        teacher.setFirstName("Valid_name");
        teacher.setLastName("Valid_surname");
        teacher.setPhone("+123456789");
        teacher.setAddress(new Address("City", "Street", "01A", "36428"));
        teacher.setProfessionalPost(ProfessionalPost.PROFESSOR);
        teacher.setAcademicDegree(null);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            teacherRepository.save(teacher);
            teacherRepository.flush();
        });
    }

    @DisplayName("TeacherRepository::FindById should return existing Teacher from DB")
    @Test
    void teacherRepositoryFindByIdShouldReturnExistingTeacherFromDatabase() {
        // given:
        long id = 1L;

        // when:
        var teacher = teacherRepository.findById(id).orElseThrow();

        // expect:
        Assertions.assertEquals(id, teacher.getId());
    }

    @DisplayName("TeacherRepository::FindById should return Optional.empty if Teacher is not found")
    @Test
    void teacherRepositoryFindByIdShouldReturnOptionalEmptyFromDatabaseIfTeacherIsNotExists() {
        // given:
        long id = 100L;

        // when:
        var optionalTeacher = teacherRepository.findById(id);

        // expect:
        Assertions.assertTrue(optionalTeacher.isEmpty());
    }

    @DisplayName("TeacherRepository::Update should update existing Teacher if it valid")
    @Test
    void teacherRepositoryUpdateShouldUpdateExistingTeacherInDatabase() {
        // given:
        long id = 1L;
        var lastnameForUpdate = "UpdatedLastname";
        var firstnameForUpdate = "UpdatedFirstName";
        var academicDegreeForUpdate = AcademicDegree.ASSOCIATE;
        var professionalPostForUpdate = ProfessionalPost.ASSISTANT;

        // when:
        var teacherToUpdate = teacherRepository.findById(id).orElseThrow();
        teacherToUpdate.setLastName(lastnameForUpdate);
        teacherToUpdate.setFirstName(firstnameForUpdate);
        teacherToUpdate.setAcademicDegree(academicDegreeForUpdate);
        teacherToUpdate.setProfessionalPost(professionalPostForUpdate);
        teacherRepository.save(teacherToUpdate);
        teacherRepository.flush();

        // then:
        var updatedTeacher = teacherRepository.findById(id).orElseThrow();

        // expect:
        Assertions.assertEquals(lastnameForUpdate, updatedTeacher.getLastName());
    }

    @DisplayName("TeacherRepository::Update should throw validation exception if Teacher.firstName invalid")
    @Test
    void teacherRepositoryUpdateShouldThrowConstraintViolationExceptionIfFirstNameIsBlank() {
        // given:
        long id = 1L;
        var lastnameForUpdate = "";
        var firstnameForUpdate = "UpdatedFirstName";
        var academicDegreeForUpdate = AcademicDegree.ASSOCIATE;
        var professionalPostForUpdate = ProfessionalPost.ASSISTANT;

        // when:
        var teacherToUpdate = teacherRepository.findById(id).orElseThrow();
        teacherToUpdate.setLastName(lastnameForUpdate);
        teacherToUpdate.setFirstName(firstnameForUpdate);
        teacherToUpdate.setAcademicDegree(academicDegreeForUpdate);
        teacherToUpdate.setProfessionalPost(professionalPostForUpdate);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            teacherRepository.save(teacherToUpdate);
            teacherRepository.flush();
        });
    }

    @DisplayName("TeacherRepository::Update should throw validation exception if Teacher.lastName invalid")
    @Test
    void teacherRepositoryUpdateShouldThrowConstraintViolationExceptionIfLastNameIsBlank() {
        // given:
        long id = 1L;
        var lastnameForUpdate = "UpdatedLastname";
        var firstnameForUpdate = "";
        var academicDegreeForUpdate = AcademicDegree.ASSOCIATE;
        var professionalPostForUpdate = ProfessionalPost.ASSISTANT;

        // when:
        var teacherToUpdate = teacherRepository.findById(id).orElseThrow();
        teacherToUpdate.setLastName(lastnameForUpdate);
        teacherToUpdate.setFirstName(firstnameForUpdate);
        teacherToUpdate.setAcademicDegree(academicDegreeForUpdate);
        teacherToUpdate.setProfessionalPost(professionalPostForUpdate);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            teacherRepository.save(teacherToUpdate);
            teacherRepository.flush();
        });
    }

    @DisplayName("TeacherRepository::Update should throw validation exception if Teacher.academicDegree invalid")
    @Test
    void teacherRepositoryUpdateShouldThrowConstraintViolationExceptionIfAcademicDegreeIsNull() {
        // given:
        long id = 1L;
        var lastnameForUpdate = "UpdatedLastname";
        var firstnameForUpdate = "UpdatedFirstName";
        AcademicDegree academicDegreeForUpdate = null;
        var professionalPostForUpdate = ProfessionalPost.ASSISTANT;

        // when:
        var teacherToUpdate = teacherRepository.findById(id).orElseThrow();
        teacherToUpdate.setLastName(lastnameForUpdate);
        teacherToUpdate.setFirstName(firstnameForUpdate);
        teacherToUpdate.setAcademicDegree(academicDegreeForUpdate);
        teacherToUpdate.setProfessionalPost(professionalPostForUpdate);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            teacherRepository.save(teacherToUpdate);
            teacherRepository.flush();
        });
    }

    @DisplayName("TeacherRepository::Update should throw validation exception if Teacher.professionalPost invalid")
    @Test
    void teacherRepositoryUpdateShouldThrowConstraintViolationExceptionIfProfessionalPostIsNull() {
        // given:
        long id = 1L;
        var lastnameForUpdate = "UpdatedLastname";
        var firstnameForUpdate = "UpdatedFirstName";
        var academicDegreeForUpdate = AcademicDegree.ASSOCIATE;
        ProfessionalPost professionalPostForUpdate = null;

        // when:
        var teacherToUpdate = teacherRepository.findById(id).orElseThrow();
        teacherToUpdate.setLastName(lastnameForUpdate);
        teacherToUpdate.setFirstName(firstnameForUpdate);
        teacherToUpdate.setAcademicDegree(academicDegreeForUpdate);
        teacherToUpdate.setProfessionalPost(professionalPostForUpdate);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            teacherRepository.save(teacherToUpdate);
            teacherRepository.flush();
        });
    }

    @DisplayName("TeacherRepository::DeleteById should remove existing Teacher from database")
    @Test
    void teacherRepositoryDeleteByIdShouldRemoveExistingTeacherFromDatabase() {
        // given:
        long id = 1L;

        // when:
        teacherRepository.deleteById(id);
        var deletedTeacher = teacherRepository.findById(id);

        // expect:
        Assertions.assertTrue(deletedTeacher.isEmpty());
    }
}