package ua.foxstudent.service.impl;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxstudent.service.CourseService;
import ua.foxstudent.domain.activity.Course;
import ua.foxstudent.repository.CourseRepository;
import ua.foxstudent.service.exceptions.CourseAlreadyExistsException;
import ua.foxstudent.service.exceptions.CourseNotFoundException;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    public static final String COURSE_ALREADY_EXISTS = "Course with name: %s already exists, and hase id: %d";
    public static final String COURSE_NOT_FOUND = "Course with id: %d was not found";
    private final CourseRepository courseRepository;

    @Override
    public Long add(@NotNull Course course) {
        log.info("Save Course: {} ...", course.toString());

        courseRepository.findByName(course.getName())
            .ifPresent(
                c -> {
                    var message = String.format(COURSE_ALREADY_EXISTS, course.getName(), c.getId());
                    log.error(message);

                    throw new CourseAlreadyExistsException(message);
                }
            );

        var savedCourse = courseRepository.save(course);

        log.debug("Course was saved to DB with id: {}", savedCourse.getId());

        return savedCourse.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Course getById(@NotNull @Positive Long id) {
        log.info("Retrieve Course with id: {} ...", id);


        var course = courseRepository.findById(id).orElseThrow(
            () -> {
                var message = String.format(COURSE_NOT_FOUND, id);
                log.error(message);

                throw new CourseNotFoundException(message);
            }
        );

        log.info("Found course with id - {} Course: {}", id, course.toString());

        return course;
    }

    @Override
    public void update(@NotNull @Positive Long id, @NotNull Course course) {
        log.info("Update Course with id - {} to {} ...", id, course.toString());

        courseRepository.findById(id)
            .ifPresentOrElse(
                c -> {
                    c.setName(course.getName());
                    c.setDescription(course.getDescription());

                    courseRepository.save(c);

                    log.debug("Course with id: {} updated", id);
                },
                () -> {
                    var message = String.format(COURSE_NOT_FOUND, id);
                    log.error(message);

                    throw new CourseNotFoundException(message);
                }
            );
    }

    @Override
    public void deleteById(@NotNull @Positive Long id) {
        log.info("Delete Course with id: {} ...", id);

        courseRepository.findById(id)
            .ifPresentOrElse(
                courseRepository::delete,
                () -> {
                    var message = String.format(COURSE_NOT_FOUND, id);
                    log.error(message);

                    throw new CourseNotFoundException(message);
                }
            );

        log.debug("Course with id: {} was deleted", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> getAll() {
        log.info("Retrieve courses from DB ...");

        var courseList = courseRepository.findAll();

        log.debug("Return {} courses", courseList.size());

        return courseList;
    }
}
