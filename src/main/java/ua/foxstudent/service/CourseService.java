package ua.foxstudent.service;

import ua.foxstudent.domain.activity.Course;

import java.util.List;

public interface CourseService {
    Long add(Course course);

    Course getById(Long id);

    void update(Long id, Course existingCourse);

    void deleteById(Long id);

    List<Course> getAll();
}
