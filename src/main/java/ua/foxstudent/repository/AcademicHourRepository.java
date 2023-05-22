package ua.foxstudent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxstudent.domain.schedule.AcademicHour;

@Repository
public interface AcademicHourRepository extends JpaRepository<AcademicHour, Long> {
}
