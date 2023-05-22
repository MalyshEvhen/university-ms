package ua.foxstudent.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxstudent.domain.schedule.AcademicHour;

import java.time.LocalTime;

@DataJpaTest(properties = "spring.datasource.url=jdbc:tc:postgresql:12:///ums_db")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AcademicHourRepositoryIntegrationTest {

    @Autowired
    private AcademicHourRepository academicHourRepository;


    @DisplayName("AcademicHourRepository::Save should save a new AcademicHour to DB when it is valid")
    @Test
    void academicHourRepositorySaveShouldSaveNewAcademicHourToDatabaseWhenPassValidAcademicHour() {
        // given:
        var academicHour = new AcademicHour(
            "valid_name",
            LocalTime.now(),
            LocalTime.now().plusHours(1));

        // when:
        var savedAcademicHour = academicHourRepository.save(academicHour);

        // expect:
        Assertions.assertEquals(academicHour, savedAcademicHour);
    }

    @DisplayName("AcademicHourRepository::Save should throw validation exception if AcademicHour.name is blank or empty")
    @ParameterizedTest
    @ValueSource(strings = {"", "  "})
    void academicHourRepositorySaveShouldThrowConstraintViolationExceptionWhenPassAcademicHourWithInvalidName(
        String name) {
        // given:
        var academicHour = new AcademicHour(
            name,
            LocalTime.now(),
            LocalTime.now().plusHours(1));

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            academicHourRepository.save(academicHour);
            academicHourRepository.flush();
        });
    }

    @DisplayName("AcademicHourRepository::Save should throw validation exception if AcademicHour.name is null")
    @Test
    void academicHourRepositorySaveShouldThrowConstraintViolationExceptionWhenPassAcademicHourWithNullName() {
        // given:
        var academicHour = new AcademicHour(
            null,
            LocalTime.now(),
            LocalTime.now().plusHours(1));

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            academicHourRepository.save(academicHour);
            academicHourRepository.flush();
        });
    }

    @DisplayName("AcademicHourRepository::Save should throw validation exception if AcademicHour.beginAt is null")
    @Test
    void academicHourRepositorySaveShouldThrowConstraintViolationExceptionWhenPassAcademicHourWithNullBeginAt() {
        // given:
        var academicHour = new AcademicHour("valid_name", null, LocalTime.now().plusHours(1));

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            academicHourRepository.save(academicHour);
            academicHourRepository.flush();
        });
    }


    @DisplayName("AcademicHourRepository::Save should throw validation exception if AcademicHour.endAt is null")
    @Test
    void academicHourRepositorySaveShouldThrowConstraintViolationExceptionWhenPassAcademicHourWithNullEndAt() {
        // given:
        var academicHour = new AcademicHour("valid_name", LocalTime.now(), null);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            academicHourRepository.save(academicHour);
            academicHourRepository.flush();
        });
    }

    @DisplayName("AcademicHourRepository::FindById should return AcademicHour if it exists")
    @Test
    void academicHourRepositoryFindByIdShouldReturnExistingAcademicHourByIdFromDatabase() {
        // when:
        var academicHour = academicHourRepository.findById(1L).orElseThrow();

        // expect:
        Assertions.assertEquals(academicHour.getId(), 1L);
    }

    @DisplayName("AcademicHourRepository::FindById should return Optional.empty if record not found")
    @Test
    void academicHourRepositoryFindByIdShouldReturnOptionalEmptyFromDatabaseItIsNotExists() {
        // given:
        long id = 100L;

        // when:
        var optionalAcademicHour = academicHourRepository.findById(id);

        // expect:
        Assertions.assertTrue(optionalAcademicHour.isEmpty());
    }

    @DisplayName("AcademicHourRepository::Update should update existing AcademicHour if update parameters is valid")
    @Test
    void academicHourRepositoryUpdateShouldUpdateExistingAcademicHourInDatabase() {
        // given:
        long id = 1L;
        var beginAtToUpdate = LocalTime.now();
        var endAtToUpdate = LocalTime.now().plusHours(1);
        var nameToUpdate = "updatedName";

        // when:
        var academicHourToUpdate = academicHourRepository.findById(id).orElseThrow();
        academicHourToUpdate.setName(nameToUpdate);
        academicHourToUpdate.setBeginAt(beginAtToUpdate);
        academicHourToUpdate.setEndAt(endAtToUpdate);
        academicHourRepository.save(academicHourToUpdate);
        academicHourRepository.flush();

        // then:
        var updatedAcademicHour = academicHourRepository.findById(id).orElseThrow();
        var updatedName = updatedAcademicHour.getName();

        // expect:
        Assertions.assertEquals(nameToUpdate, updatedName);
    }

    @DisplayName("AcademicHourRepository::Update should throw validation exception if AcademicHour.name is blank")
    @Test
    void academicHourRepositoryUpdateShouldThrowConstraintViolationExceptionIfNameIsBlank() {
        // given:
        long id = 1L;
        var nameToUpdate = "";

        // when:
        var academicHourToUpdate = academicHourRepository.findById(id).orElseThrow();
        academicHourToUpdate.setName(nameToUpdate);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            academicHourRepository.save(academicHourToUpdate);
            academicHourRepository.flush();
        });
    }

    @DisplayName("AcademicHourRepository::Update should throw validation exception if AcademicHour.name is null")
    @Test
    void academicHourRepositoryUpdateShouldThrowConstraintViolationExceptionIfNameIsNull() {
        // given:
        long id = 1L;
        String nameToUpdate = null;

        // when:
        var academicHourToUpdate = academicHourRepository.findById(id).orElseThrow();
        academicHourToUpdate.setName(nameToUpdate);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            academicHourRepository.save(academicHourToUpdate);
            academicHourRepository.flush();
        });
    }

    @DisplayName("AcademicHourRepository::Update should throw validation exception if AcademicHour.beginAt is null")
    @Test
    void academicHourRepositoryUpdateShouldThrowConstraintViolationExceptionIfBeginAtIsNull() {
        // given:
        long id = 1L;
        LocalTime beginAtToUpdate = null;

        // when:
        var academicHourToUpdate = academicHourRepository.findById(id).orElseThrow();
        academicHourToUpdate.setBeginAt(beginAtToUpdate);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            academicHourRepository.save(academicHourToUpdate);
            academicHourRepository.flush();
        });
    }

    @DisplayName("AcademicHourRepository::Update should throw validation exception if AcademicHour.endAt is null")
    @Test
    void academicHourRepositoryUpdateShouldThrowConstraintViolationExceptionIfEndAtIsNull() {
        // given:
        long id = 1L;
        LocalTime endAtToUpdate = null;

        // when:
        var academicHourToUpdate = academicHourRepository.findById(id).orElseThrow();
        academicHourToUpdate.setEndAt(endAtToUpdate);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            academicHourRepository.save(academicHourToUpdate);
            academicHourRepository.flush();
        });
    }

    @DisplayName("AcademicHourRepository::DeleteById should delete existing AcademicHour from DB")
    @Test
    void academicHourRepositoryDeleteByIdShouldDeleteExistingAHFromDatabase() {
        // given:
        long id = 1L;

        // when:
        academicHourRepository.deleteById(id);
        var optionalAcademicHour = academicHourRepository.findById(id);

        // expect:
        Assertions.assertTrue(optionalAcademicHour.isEmpty());
    }
}