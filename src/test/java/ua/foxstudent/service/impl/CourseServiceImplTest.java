package ua.foxstudent.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.foxstudent.domain.activity.Course;
import ua.foxstudent.repository.CourseRepository;
import ua.foxstudent.service.CourseService;
import ua.foxstudent.service.exceptions.CourseAlreadyExistsException;
import ua.foxstudent.service.exceptions.CourseNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    private CourseService courseService;

    @BeforeEach
    void setUp() {
        courseService = new CourseServiceImpl(courseRepository);
    }

    @DisplayName("CourseServiceImpl::add should pass Course to CourseRepository::save")
    @Test
    void addShouldPassCourseToRepo() {
        // given:
        var course = new Course(1L, "New Course", "Description of new course");

        // when:
        when(courseRepository.save(course)).thenReturn(course);

        // then:
        courseService.add(course);
        verify(courseRepository, times(1)).findByName(course.getName());
        verify(courseRepository, times(1)).save(course);
    }

    @DisplayName("CourseServiceImpl::add should throw an exception when Course is already exists")
    @Test
    void addShouldThrowException() {
        // given:
        var course = new Course("New Course", "Description of new course");

        // when:
        when(courseRepository.findByName(course.getName())).thenReturn(Optional.of(course));

        // then:
        Assertions.assertThrows(CourseAlreadyExistsException.class,
            () -> courseService.add(course),
            "Course with name: " + course.getName() + " already exists");
    }

    @DisplayName("CourseServiceImpl::getById should should return existing Course")
    @Test
    void getByIdShouldPassIdToRepoAndReturnCourse() {
        // given:
        var expected = new Course(1L, "Course", "Description of the course");

        // when:
        when(courseRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        var actual = courseService.getById(expected.getId());

        // then:
        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("CourseServiceImpl::getById should throw an exception when Course is not found")
    @Test
    void getByIdShouldThrowException() {
        // given:
        var id = 1L;

        // when:
        when(courseRepository.findById(id)).thenReturn(Optional.empty());

        // then:
        Assertions.assertThrows(CourseNotFoundException.class,
            () -> courseService.getById(id),
            "Course with id: " + id + " was not found");
    }

    @DisplayName("CourseServiceImpl::update should pass updated Course to repository")
    @Test
    void updateShouldPassUpdatedCourseToRepo() {
        // given:
        var id = 1L;
        var existingCourse = new Course(id, "Name", "Description");
        var expected = new Course(id, "Updated name", "Updated description");

        // when:
        when(courseRepository.findById(id)).thenReturn(Optional.of(existingCourse));

        // then:
        courseService.update(id, expected);
        verify(courseRepository, times(1)).save(expected);
    }

    @DisplayName("CourseServiceImpl::update should throw an exception when Course is not found")
    @Test
    void updateShouldThrowException() {
        // given:
        var id = 1L;
        var course = new Course(id, "Updated name", "Updated description");


        // when:
        when(courseRepository.findById(id)).thenReturn(Optional.empty());

        // then:
        assertThrows(CourseNotFoundException.class,
            () -> courseService.update(id, course),
            "Course with id: " + id + " was not found");
    }

    @DisplayName("CourseServiceImpl::deleteById should invoke CourseRepository::delete with existing Course argument")
    @Test
    void deleteByIdShouldInvokeDeleteMethodInRepo() {
        // given:
        var id = 1L;
        var existingCourse = new Course(id, "Name", "Description");

        // when:
        when(courseRepository.findById(id)).thenReturn(Optional.of(existingCourse));

        // then:
        courseService.deleteById(id);
        verify(courseRepository, times(1)).delete(existingCourse);
    }

    @DisplayName("CourseServiceImpl::deleteById should throw an exception when Course is not found")
    @Test
    void deleteByIdShouldThrowException() {
        // given:
        var id = 1L;

        // when:
        when(courseRepository.findById(id)).thenReturn(Optional.empty());

        // then:
        assertThrows(CourseNotFoundException.class,
            () -> courseService.deleteById(id),
            "Course with id: " + id + " was not found");
    }

    @DisplayName("CourseServiceImpl::getAll should invoke CourseRepository::findAll")
    @Test
    void getAllShouldReturnListOfCourses() {
        // when:
        courseService.getAll();

        // then:
        verify(courseRepository, times(1)).findAll();
    }
}