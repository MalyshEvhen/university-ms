package ua.foxstudent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxstudent.domain.schedule.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
