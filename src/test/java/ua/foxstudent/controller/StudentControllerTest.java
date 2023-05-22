package ua.foxstudent.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ua.foxstudent.dto.person.AddressDto;
import ua.foxstudent.service.CourseService;
import ua.foxstudent.service.GroupService;
import ua.foxstudent.service.StudentService;
import ua.foxstudent.domain.activity.Course;
import ua.foxstudent.dto.person.PersonalInfoDto;
import ua.foxstudent.dto.student.StudentCoursesDto;
import ua.foxstudent.dto.student.StudentCreateDto;

@WebMvcTest(controllers = StudentController.class)
class StudentControllerTest {

    private static final String REDIRECT = "redirect:";
    private static final String STUDENT_HOME_URI = "/student";
    private static final String STUDENT_ADD_URI = "/student/add";
    private static final String STUDENT_DELETE_URI = "/student/delete";
    private static final String STUDENT_UPDATE_ADDRESS_URI = "/student/update/address";
    private static final String STUDENT_UPDATE_COURSES_URI = "/student/update/courses";
    private static final String STUDENT_ADD_COURSE_URI = "/student/courses/add";
    private static final String STUDENT_REMOVE_COURSE_URI = "/student/courses/remove";
    private static final String STUDENT_INFO_URI = "/student/info";
    private static final String STUDENT_ADD_VIEW = "student/add";
    private static final String STUDENT_UPDATE_ADDRESS_VIEW = "student/update-address";
    private static final String STUDENT_UPDATE_COURSES_VIEW = "student/update-courses";
    private static final String PERSONAL_INFO_VIEW = "person/info";
    private static final String STUDENT_LIST_VIEW = "student/list";

    @MockBean
    private StudentService studentService;

    @MockBean
    private CourseService courseService;

    @MockBean
    private GroupService groupService;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("When controller is called, it should return correct view")
    @Test
    void testAddStudentView_shouldViewStudentAddTemplate() throws Exception {
        // expect:
        mockMvc.perform(get(STUDENT_ADD_URI))
            .andExpectAll(
                status().is2xxSuccessful(),
                view().name(STUDENT_ADD_VIEW),
                content().string(containsString("Add Student"))
            );
    }

    @DisplayName("When a valid student is submitted, it should be passed to the service")
    @Test
    void testAddStudentSuccess() throws Exception {
        // given:
        long id = 1L;

        var student = new StudentCreateDto();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setPhone("1234567890");
        student.setUserEmail("some.email@email.com");
        student.setUserPassword("1234567890");
        student.setAddressCity("New York");
        student.setAddressStreet("123 Main St");
        student.setAddressNumber("5A");
        student.setAddressZip("12345");

        // when:
        when(studentService.add(student)).thenReturn(id);

        mockMvc.perform(post("/student/add")
                .flashAttr("student", student))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.redirectedUrl("/student/info/1"));
    }

    @DisplayName("When an invalid Student is submitted, it should view student/add page")
    @Test
    void testAddStudentValidationErrors() throws Exception {
        mockMvc.perform(post("/student/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", "")
                .param("lastName", "")
                .param("userEmail", "")
                .param("userPassword", "")
                .param("addressCity", "")
                .param("addressStreet", "")
                .param("addressNumber", "")
                .param("addressZip", "")
                .param("phone", ""))
            .andExpect(status().isOk())
            .andExpect(view().name("student/add"))
            .andExpect(model().attributeHasFieldErrors("student", "firstName"))
            .andExpect(model().attributeHasFieldErrors("student", "lastName"))
            .andExpect(model().attributeHasFieldErrors("student", "userEmail"))
            .andExpect(model().attributeHasFieldErrors("student", "userPassword"))
            .andExpect(model().attributeHasFieldErrors("student", "addressCity"))
            .andExpect(model().attributeHasFieldErrors("student", "addressStreet"))
            .andExpect(model().attributeHasFieldErrors("student", "addressNumber"))
            .andExpect(model().attributeHasFieldErrors("student", "addressZip"))
            .andExpect(model().attributeHasFieldErrors("student", "phone"));
    }

    @DisplayName("When controller is called, it should return correct view")
    @Test
    void testStudentListView_shouldViewStudentListTemplate() throws Exception {
        // expect:
        mockMvc.perform(get(STUDENT_HOME_URI))
            .andExpectAll(
                status().is2xxSuccessful(),
                view().name(STUDENT_LIST_VIEW),
                content().string(containsString("Students"))
            );
    }

    @DisplayName("When controller is called, it should return correct view")
    @Test
    void testInfoStudentView_shouldViewStudentInfoTemplate() throws Exception {
        // given:
        Long id = 1L;

        var studentInfo = new PersonalInfoDto();
        studentInfo.setId(id);
        studentInfo.setFirstName("John");
        studentInfo.setLastName("Doe");
        studentInfo.setAddressCity("Kiyv");
        studentInfo.setAddressStreet("Bogdana Hmelnytskogo");
        studentInfo.setAddressNumber("8-A");
        studentInfo.setAddressZip("123456");
        studentInfo.setPhone("+3801234567");

        // when:
        when(studentService.getPersonalInfoById(id)).thenReturn(studentInfo);

        // then:
        mockMvc.perform(get(STUDENT_INFO_URI + "/{id}", id))
            .andExpectAll(
                status().is2xxSuccessful(),
                view().name(PERSONAL_INFO_VIEW),
                content().string(containsString("Personal Info")),
                content().string(containsString(studentInfo.getFirstName())),
                content().string(containsString(studentInfo.getLastName())),
                content().string(containsString(studentInfo.getPhone())),
                content().string(containsString(studentInfo.getAddressCity())),
                content().string(containsString(studentInfo.getAddressStreet())),
                content().string(containsString(studentInfo.getAddressNumber())),
                content().string(containsString(studentInfo.getAddressZip()))
            );
    }

    @DisplayName("When controller is called, it should return correct view")
    @Test
    void testUpdateCoursesView_shouldViewStudentUpdateCoursesTemplate() throws Exception {
        // given:
        var id = 1L;
        var student = new StudentCoursesDto();
        var course = new Course("Test course", "Great course");
        var courses = new ArrayList<>(List.of(course));
        student.setId(id);
        student.setCourses(courses);

        // when:
        when(courseService.getAll()).thenReturn(courses);
        when(studentService.getStudentCoursesDtoById(id)).thenReturn(student);

        // then:
        mockMvc.perform(get(STUDENT_UPDATE_COURSES_URI + "/{id}", id))
            .andExpectAll(
                status().is2xxSuccessful(),
                view().name(STUDENT_UPDATE_COURSES_VIEW),
                content().string(containsString("Update Student Courses"))
            );
    }

    @DisplayName("When a valid student courses is submitted, it should be passed to the service")
    @Test
    void testAddStudentCoursesSuccess() throws Exception {
        long studentId = 1L;
        long courseId = 1L;
        var studentCourses = new StudentCoursesDto();
        studentCourses.setId(studentId);
        studentCourses.setFirstName("John");
        studentCourses.setLastName("Doe");
        studentCourses.setCourses(new ArrayList<Course>());

        mockMvc.perform(get(STUDENT_ADD_COURSE_URI + "/{studentId}/{courseId}",
                studentId,
                courseId)
            .flashAttr("student", studentCourses))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.redirectedUrl(STUDENT_UPDATE_COURSES_URI + "/" + studentId));

        verify(studentService, times(1)).addCourse(studentId, courseId);
    }

    @DisplayName("When a valid student courses is submitted, it should be passed to the service")
    @Test
    void testRemoveStudentCoursesSuccess() throws Exception {
        long studentId = 1L;
        long courseId = 1L;
        var studentCourses = new StudentCoursesDto();
        studentCourses.setId(studentId);
        studentCourses.setFirstName("John");
        studentCourses.setLastName("Doe");
        studentCourses.setCourses(new ArrayList<Course>());

        mockMvc.perform(get(STUDENT_REMOVE_COURSE_URI + "/{studentId}/{courseId}",
                studentId,
                courseId)
            .flashAttr("student", studentCourses))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.redirectedUrl(STUDENT_UPDATE_COURSES_URI + "/" + studentId));

        verify(studentService, times(1)).removeCourse(studentId, courseId);
    }

    @DisplayName("When controller is called, it should return correct view")
    @Test
    void testUpdateStudentAddressView_shouldViewUpdateStudentTemplate() throws Exception {
        // given:
        var id = 1L;
        var addressDto = new AddressDto();
        addressDto.setId(id);
        addressDto.setCity("Kyiv");
        addressDto.setStreet("Khreschatyk");
        addressDto.setNumber("1");
        addressDto.setZip("19611");

        // when:
        when(studentService.getAddressDto(id)).thenReturn(addressDto);

        // then:
        mockMvc.perform(get(STUDENT_UPDATE_ADDRESS_URI + "/{id}", id))
            .andExpectAll(
                status().is2xxSuccessful(),
                view().name(STUDENT_UPDATE_ADDRESS_VIEW),
                content().string(containsString("Update Address"))
            );
    }

    @DisplayName("When an invalid address is submitted, it should view student/update-address page")
    @Test
    void testUpdateStudentAddressValidationError() throws Exception {
        // given:
        Long id = 1L;
        var address = new AddressDto();
        address.setId(id);
        address.setCity("");
        address.setStreet("");
        address.setNumber("");
        address.setZip("");

        // expect:
        mockMvc.perform(post(STUDENT_UPDATE_ADDRESS_URI + "/{id}", id)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("address", address))
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name(STUDENT_UPDATE_ADDRESS_VIEW));
    }

    @DisplayName("When a valid student address is submitted, it should be passed to the service")
    @Test
    void testUpdateStudentAddressSuccess() throws Exception {
        // given:
        long id = 1L;
        var addressDto = new AddressDto();
        addressDto.setId(id);
        addressDto.setCity("Kyiv");
        addressDto.setStreet("Khreschatyk");
        addressDto.setNumber("1");
        addressDto.setZip("19611");

        // expect:
        mockMvc.perform(post(STUDENT_UPDATE_ADDRESS_URI + "/{id}", id)
                .flashAttr("student", addressDto))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.redirectedUrl(STUDENT_INFO_URI + "/" + id));
    }

    @DisplayName("When a valid id is submitted, it should be passed to the service")
    @Test
    void testRemoveGroup() throws Exception {
        // given:
        var id = 1L;

        // when:
        mockMvc.perform(get(STUDENT_DELETE_URI + "/{id}", id)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(REDIRECT + STUDENT_HOME_URI))
            .andReturn();

        // then:
        verify(studentService, times(1)).deleteById(id);
    }
}