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
import ua.foxstudent.domain.person.Address;
import ua.foxstudent.domain.person.Student;
import ua.foxstudent.dto.person.PersonalInfoDto;

@DataJpaTest(properties = "spring.datasource.url=jdbc:tc:postgresql:12:///ums_db")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentRepositoryIntegrationTest {

    @Autowired
    private StudentRepository studentRepository;

    @DisplayName("StudentRepository::SaveShouldSave new Student to DB if it valid")
    @Test
    void studentRepositorySaveShouldSaveNewStudentToDatabaseWhenPassValidStudent() {
        // given:
        var student = new Student();
        student.setFirstName("Valid_name");
        student.setLastName("Valid_surname");
        student.setPhone("+123456789");
        student.setAddress(new Address("City", "Street", "01A", "36428"));

        // when:
        var savedStudent = studentRepository.save(student);

        // expect:
        Assertions.assertEquals(student, savedStudent);
        Assertions.assertTrue(savedStudent.getId() != 0);
    }

    @DisplayName("StudentRepository::Save should throw validation exception Student is invalid")
    @ParameterizedTest
    @CsvSource({
        "valid_name, valid_surname,",
        ", valid_surname, +123456789",
        "valid_name,,+123456789",
        "valid_name, valid_surname,     ",
        "      , valid_surname, +123456789",
        "valid_name,      ,+123456789"
    })
    void studentRepositorySaveShouldThrowConstraintViolationExceptionWhenPassInvalidStudentParameters(
        String firstName,
        String lastName,
        String phone) {
        // given:
        var student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setPhone(phone);
        student.setAddress(new Address("City", "Street", "01A", "36428"));

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            studentRepository.save(student);
            studentRepository.flush();
        });
    }

    @DisplayName("StudentRepository::Save should throw validation exception if Student.address is null")
    @Test
    void studentRepositorySaveShouldThrowConstraintViolationExceptionWhenPassNullInsteadStudentAddress() {
        // given:
        var student = new Student();
        student.setFirstName("Valid_name");
        student.setLastName("Valid_surname");
        student.setPhone("+123456789");
        student.setAddress(null);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            studentRepository.save(student);
            studentRepository.flush();
        });
    }

    @DisplayName("StudentRepository::FindById should return existing Student from DB")
    @Test
    void studentRepositoryFindByIdShouldReturnExistingStudentFromDatabase() {
        // given:
        long id = 1L;

        // when:
        var student = studentRepository.findById(id).orElseThrow();

        // expect:
        Assertions.assertEquals(id, student.getId());
    }

    @DisplayName("StudentRepository::FindById should return Optional.empty if Student is not found")
    @Test
    void studentRepositoryFindByIdShouldReturnOptionalEmptyFromDatabaseIfStudentIsNotExists() {
        // given:
        long id = 100L;

        // when:
        var optionalStudent = studentRepository.findById(id);

        // expect:
        Assertions.assertTrue(optionalStudent.isEmpty());
    }

    @DisplayName("StudentRepository::Update should update Student if parameters are valid")
    @Test
    void studentRepositoryUpdateShouldUpdateExistingStudentInDatabase() {
        // given:
        long id = 1L;
        var lastnameForUpdate = "UpdatedLastname";
        var firstForUpdate = "UpdatedFirstname";
        var addressForUpdate = new Address("City", "Street", "01A", "36428");

        // when:
        var studentToUpdate = studentRepository.findById(id).orElseThrow();
        studentToUpdate.setLastName(lastnameForUpdate);
        studentToUpdate.setFirstName(firstForUpdate);
        studentToUpdate.setAddress(addressForUpdate);

        studentRepository.save(studentToUpdate);
        studentRepository.flush();

        // then:
        var updatedStudent = studentRepository.findById(id).orElseThrow();

        // expect:
        Assertions.assertEquals(lastnameForUpdate, updatedStudent.getLastName());
    }

    @DisplayName("StudentRepository::Update should throw validation exception if Student.firstName is blank")
    @Test
    void studentRepositoryUpdateShouldThrowConstraintViolationExceptionIfFirstnameIsBlank() {
        // given:
        long id = 1L;
        var lastnameForUpdate = "UpdatedLastname";
        var firstForUpdate = "";
        var addressForUpdate = new Address("City", "Street", "01A", "36428");

        // when:
        var studentToUpdate = studentRepository.findById(id).orElseThrow();
        studentToUpdate.setLastName(lastnameForUpdate);
        studentToUpdate.setFirstName(firstForUpdate);
        studentToUpdate.setAddress(addressForUpdate);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            studentRepository.save(studentToUpdate);
            studentRepository.flush();
        });
    }

    @DisplayName("StudentRepository::Update should throw validation exception if Student.lastName is blank")
    @Test
    void studentRepositoryUpdateShouldThrowConstraintViolationExceptionIfLastnameIsBlank() {
        // given:
        long id = 1L;
        var lastnameForUpdate = "";
        var firstForUpdate = "UpdatedFirstname";
        var addressForUpdate = new Address("City", "Street", "01A", "36428");

        // when:
        var studentToUpdate = studentRepository.findById(id).orElseThrow();
        studentToUpdate.setLastName(lastnameForUpdate);
        studentToUpdate.setFirstName(firstForUpdate);
        studentToUpdate.setAddress(addressForUpdate);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            studentRepository.save(studentToUpdate);
            studentRepository.flush();
        });
    }


    @DisplayName("StudentRepository::Update should throw validation exception if Student.address is null")
    @Test
    void studentRepositoryUpdateShouldThrowConstraintViolationExceptionIfAddressIsNull() {
        // given:
        long id = 1L;
        var lastnameForUpdate = "UpdatedLastname";
        var firstForUpdate = "UpdatedFirstname";
        Address addressForUpdate = null;

        // when:
        var studentToUpdate = studentRepository.findById(id).orElseThrow();
        studentToUpdate.setLastName(lastnameForUpdate);
        studentToUpdate.setFirstName(firstForUpdate);
        studentToUpdate.setAddress(addressForUpdate);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            studentRepository.save(studentToUpdate);
            studentRepository.flush();
        });
    }

    @DisplayName("StudentRepository::DeleteById should remove existing Student from DB")
    @Test
    void studentRepositoryDeleteByIdShouldRemoveExistingStudentFromDatabase() {
        // given:
        long id = 1L;

        // when:
        studentRepository.deleteById(id);
        var deletedStudent = studentRepository.findById(id);

        // expect:
        Assertions.assertTrue(deletedStudent.isEmpty());
    }

    @DisplayName("StudentRepository::findPersonalInfoDtoById should return PersonalInfoDto")
    @Test
    void setStudentRepositoryFindPersonalInfoDtoByIdShouldReturnPersonalInfoDtoByIdById() {
        // given:
        long id = 1L;
        var student = studentRepository.findById(id).get();

        var actual = new PersonalInfoDto();
        actual.setId(id);
        actual.setFirstName(student.getFirstName());
        actual.setLastName(student.getLastName());
        actual.setPhone(student.getPhone());
        actual.setAddressCity(student.getAddress().getCity());
        actual.setAddressStreet(student.getAddress().getStreet());
        actual.setAddressNumber(student.getAddress().getNumber());
        actual.setAddressApt(student.getAddress().getApt());
        actual.setAddressZip(student.getAddress().getZip());

        // when:
        var expected = studentRepository.findPersonalInfoDtoById(id).get();

        // then:
        Assertions.assertEquals(expected, actual);
    }
}