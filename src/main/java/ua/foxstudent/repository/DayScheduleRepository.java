package ua.foxstudent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxstudent.domain.schedule.DaySchedule;

@Repository
public interface DayScheduleRepository extends JpaRepository<DaySchedule, Long> {
}
