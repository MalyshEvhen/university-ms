package ua.foxstudent.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ua.foxstudent.service.CourseService;
import ua.foxstudent.domain.activity.Course;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = CourseController.class)
class CourseControllerTest {
    private static final String REDIRECT = "redirect:";
    private static final String COURSE_HOME_URI = "/course";
    private static final String COURSE_ADD_URI = "/course/add";
    private static final String COURSE_UPDATE_URI = "/course/update";
    private static final String COURSE_INFO_URI = "/course/info";
    private static final String COURSE_DELETE_URI = "/course/delete";
    private static final String COURSE_ADD_VIEW = "course/add";
    private static final String COURSE_UPDATE_VIEW = "course/update";
    private static final String COURSE_INFO_VIEW = "course/info";
    private static final String COURSE_LIST_VIEW = "course/list";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @DisplayName("When controller is called, it should return correct view")
    @Test
    void testAddCourseView_shouldViewCourseAddTemplate() throws Exception {
        // expect:
        mockMvc.perform(get(COURSE_ADD_URI))
            .andExpectAll(
                status().is2xxSuccessful(),
                view().name(COURSE_ADD_VIEW),
                content().string(containsString("Add Course"))
            );
    }

    @DisplayName("When a valid course is submitted, it should be passed to the service")
    @Test
    void testAddCourse_validCourse_shouldPassToService() throws Exception {
        // given:
        var course = new Course();
        course.setName("Test Course");
        course.setDescription("This is a test course.");

        // when:
        mockMvc.perform(post(COURSE_ADD_URI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", course.getName())
                .param("description", course.getDescription()))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(REDIRECT + COURSE_INFO_URI + "/" + courseService.add(course)))
            .andReturn();

        // then:
        verify(courseService, times(1)).add(course);
    }

    @DisplayName("When an invalid course is submitted, it should view course/add page")
    @Test
    void testAddCourse_invalidCourse_shouldRedirectToAddCourse() throws Exception {
        // given:
        var course = new Course();
        course.setName("");
        course.setDescription("This is a test course.");

        // expect:
        mockMvc.perform(post(COURSE_ADD_URI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", course.getName())
                .param("description", course.getDescription()))
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name(COURSE_ADD_VIEW))
            .andReturn();
    }


    @DisplayName("When controller is called, it should return correct view")
    @Test
    void testUpdateCourseView() throws Exception {
        // given:
        var course = new Course(1L, "Math", "Queen of science");

        // when:
        when(courseService.getById(course.getId())).thenReturn(course);

        // then:
        mockMvc.perform(get(COURSE_UPDATE_URI + "/{id}", course.getId()))
            .andExpectAll(
                status().is2xxSuccessful(),
                view().name(COURSE_UPDATE_VIEW),
                content().string(containsString("Update Course"))
            );
    }

    @DisplayName("When a valid course is submitted, it should be passed to the service")
    @Test
    void testUpdateCourse_validCourse_shouldPassToService() throws Exception {
        // given:
        var id = 1L;
        var course = new Course();
        course.setId(id);
        course.setName("Test Course");
        course.setDescription("This is a test course.");

        // when:
        mockMvc.perform(post(COURSE_UPDATE_URI + "/{id}", id)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", course.getName())
                .param("description", course.getDescription()))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(REDIRECT + COURSE_INFO_URI + "/" + id))
            .andReturn();

        // then:
        verify(courseService, times(1)).update(id, course);
    }

    @DisplayName("When an invalid course is submitted, it should view course/update page")
    @Test
    void testUpdateCourse_invalidCourse_redirectToUpdateCourse() throws Exception {
        // given:
        var id = 1L;
        var course = new Course();
        course.setId(id);
        course.setName("");
        course.setDescription("This is a test course.");

        // expect:
        mockMvc.perform(post(COURSE_UPDATE_URI + "/{id}", id)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", course.getName())
                .param("description", course.getDescription()))
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name(COURSE_UPDATE_VIEW))
            .andReturn();
    }

    @DisplayName("When controller is called, it should return correct view")
    @Test
    void testInfoCourseView() throws Exception {
        // given:
        var course = new Course(1L, "Math", "Queen of science");

        // when:
        when(courseService.getById(course.getId())).thenReturn(course);

        // then:
        mockMvc.perform(get(COURSE_INFO_URI + "/{id}", course.getId()))
            .andExpectAll(
                status().is2xxSuccessful(),
                view().name(COURSE_INFO_VIEW),
                content().string(containsString("Course Info")),
                content().string(containsString(course.getId().toString())),
                content().string(containsString(course.getName())),
                content().string(containsString(course.getDescription()))
            );
    }

    @DisplayName("When controller is called, it should return correct view")
    @Test
    void testGetAllCoursesView() throws Exception {
        // given:
        var course = new Course(1L, "Math", "Queen of science");

        // when:
        when(courseService.getAll()).thenReturn(List.of(course));

        // then:
        mockMvc.perform(get(COURSE_HOME_URI))
            .andExpectAll(
                status().is2xxSuccessful(),
                view().name(COURSE_LIST_VIEW),
                content().string(containsString("Courses")),
                content().string(containsString(course.getId().toString())),
                content().string(containsString(course.getName())),
                content().string(containsString(course.getDescription()))
            );
    }

    @DisplayName("When a valid id is submitted, it should be passed to the service")
    @Test
    void testRemoveCourse() throws Exception {
        // given:
        var id = 1L;

        // when:
        mockMvc.perform(get(COURSE_DELETE_URI + "/{id}", id)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(REDIRECT + COURSE_HOME_URI))
            .andReturn();

        // then:
        verify(courseService, times(1)).deleteById(id);
    }
}