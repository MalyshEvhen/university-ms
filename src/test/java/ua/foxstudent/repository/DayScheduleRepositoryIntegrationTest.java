package ua.foxstudent.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.JavaTimeConversionPattern;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxstudent.domain.schedule.DaySchedule;

import java.time.LocalDate;

@DataJpaTest(properties = "spring.datasource.url=jdbc:tc:postgresql:12:///ums_db")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DayScheduleRepositoryIntegrationTest {

    @Autowired
    private DayScheduleRepository dayScheduleRepository;

    @DisplayName("DayScheduleRepository::Save should save new DayScheduleToDatabase if date is valid")
    @Test
    void dayScheduleRepositorySaveShouldSaveNewDayScheduleToDatabaseWhenDateIsValid() {
        // given:
        var daySchedule = new DaySchedule(LocalDate.now());

        // when:
        var savedDaySchedule = dayScheduleRepository.save(daySchedule);

        // expect:
        Assertions.assertEquals(daySchedule, savedDaySchedule);
    }

    @DisplayName("DayScheduleRepository::Save should throw validation exception DaySchedule.date is invalid")
    @ParameterizedTest
    @CsvSource({"03.03.2023", "01.01.1872"})
    void dayScheduleRepositorySaveShouldThrowConstraintViolationExceptionWhenPassDayScheduleWithInvalidDate(
        @JavaTimeConversionPattern("dd.MM.yyyy") LocalDate date) {
        // given:
        var daySchedule = new DaySchedule(date);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            dayScheduleRepository.save(daySchedule);
            dayScheduleRepository.flush();
        });
    }

    @DisplayName("DayScheduleRepository::Save should throw validation exception DaySchedule.date is null")
    @Test
    void dayScheduleRepositorySaveShouldThrowConstraintViolationExceptionWhenPassDayScheduleWithNullDateParameter() {
        // given:
        var daySchedule = new DaySchedule(null);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            dayScheduleRepository.save(daySchedule);
            dayScheduleRepository.flush();
        });
    }

    @DisplayName("DayScheduleRepository::FindById should return existing DaySchedule from DB")
    @Test
    void dayScheduleRepositoryFindByIdShouldReturnExistingDayScheduleByIdFromDatabase() {
        // when:
        var daySchedule = dayScheduleRepository.findById(1L).orElseThrow();

        // expect:
        Assertions.assertEquals(daySchedule.getId(), 1L);
    }

    @DisplayName("DayScheduleRepository::FindById should return Optional.empty if DaySchedule is not found")
    @Test
    void dayScheduleRepositoryFindByIdShouldReturnOptionalEmptyFromDatabaseItIsNotExists() {
        // given:
        long id = 100L;

        // when:
        var optionalDaySchedule = dayScheduleRepository.findById(id);

        // expect:
        Assertions.assertTrue(optionalDaySchedule.isEmpty());
    }

    @DisplayName("DayScheduleRepository::DeleteById should delete existing DaySchedule from DB")
    @Test
    void dayScheduleRepositoryDeleteByIdShouldDeleteExistingDayScheduleFromDatabase() {
        // given:
        long id = 1L;

        // when:
        dayScheduleRepository.deleteById(id);
        var optionalDaySchedule = dayScheduleRepository.findById(id);

        // expect:
        Assertions.assertTrue(optionalDaySchedule.isEmpty());
    }
}