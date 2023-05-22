package ua.foxstudent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxstudent.domain.schedule.Classroom;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
}
