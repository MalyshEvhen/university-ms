package ua.foxstudent.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxstudent.service.StudentService;
import ua.foxstudent.dto.person.AddressDto;
import ua.foxstudent.dto.student.StudentCreateDto;
import ua.foxstudent.service.CourseService;
import ua.foxstudent.service.GroupService;

@Slf4j
@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private static final String REDIRECT = "redirect:";
    private static final String STUDENT_HOME_URI = "/student";
    private static final String STUDENT_ADD_URI = "/add";
    private static final String STUDENT_DELETE_URI = "/delete";
    private static final String STUDENT_UPDATE_ADDRESS_URI = "/update/address";
    private static final String STUDENT_UPDATE_COURSES_URI = "/update/courses";
    private static final String STUDENT_ADD_COURSE_URI = "/courses/add";
    private static final String STUDENT_REMOVE_COURSE_URI = "/courses/remove";
    private static final String STUDENT_INFO_URI = "/info";
    private static final String STUDENT_ADD_VIEW = "student/add";
    private static final String STUDENT_UPDATE_COURSES_VIEW = "student/update-courses";
    private static final String STUDENT_UPDATE_ADDRESS_VIEW = "student/update-address";
    private static final String PERSON_INFO_VIEW = "person/info";
    private static final String STUDENT_LIST_VIEW = "student/list";
    private static final String STUDENTS_MODEL_ATTR = "students";
    private static final String STUDENT_MODEL_ATTR = "student";
    private static final String PERSON_MODEL_ATTR = "person";
    private static final String GROUPS_MODEL_ATTR = "groups";
    private static final String COURSES_MODEL_ATTR = "courses";
    private static final String ADDRESS_MODEL_ATTR = "address";

    private final StudentService studentService;
    private final GroupService groupService;
    private final CourseService courseService;

    @GetMapping(STUDENT_ADD_URI)
    public String saveView(Model model) {
        model.addAttribute(STUDENT_MODEL_ATTR, new StudentCreateDto());
        model.addAttribute(GROUPS_MODEL_ATTR, groupService.getAll());
        model.addAttribute(COURSES_MODEL_ATTR, courseService.getAll());

        log.debug("Displayed the student save view");

        return STUDENT_ADD_VIEW;
    }

    @PostMapping(STUDENT_ADD_URI)
    public String save(
        @Valid @ModelAttribute(STUDENT_MODEL_ATTR) StudentCreateDto studentRequest,
        Errors errors
    ) {
        if (errors.hasErrors()) {
            log.error("{} has validation errors: {}", studentRequest, errors.getAllErrors());

            return STUDENT_ADD_VIEW;
        }
        var studentId = studentService.add(studentRequest);

        log.debug("Saved the student with id {}", studentId);

        return REDIRECT + STUDENT_HOME_URI + STUDENT_INFO_URI + "/" + studentId;
    }

    @GetMapping(STUDENT_INFO_URI + "/{id}")
    public String infoView(@NotNull @PathVariable Long id, Model model) {
        var studentInfo = studentService.getPersonalInfoById(id);

        model.addAttribute(PERSON_MODEL_ATTR, studentInfo);

        log.debug("Displayed the student info view for student with id {}", id);

        return PERSON_INFO_VIEW;
    }

    @GetMapping
    public String listView(Model model) {
        var allStudentDTO = studentService.getAllStudentListViewDtos();
        model.addAttribute(STUDENTS_MODEL_ATTR, allStudentDTO);
        model.addAttribute(STUDENT_MODEL_ATTR, new StudentCreateDto());
        model.addAttribute(GROUPS_MODEL_ATTR, groupService.getAll());
        model.addAttribute(COURSES_MODEL_ATTR, courseService.getAll());

        log.debug("Displayed the student list view");

        return STUDENT_LIST_VIEW;
    }

    @GetMapping(STUDENT_UPDATE_COURSES_URI + "/{id}")
    public String updateCoursesView(@NotNull @PathVariable Long id, Model model) {
        var studentDto = studentService.getStudentCoursesDtoById(id);
        var courses = courseService.getAll();
        courses.removeAll(studentDto.getCourses());

        model.addAttribute(STUDENT_MODEL_ATTR, studentDto);
        model.addAttribute(COURSES_MODEL_ATTR, courses);

        log.debug("Displayed the student update courses view for student with id {}", id);

        return STUDENT_UPDATE_COURSES_VIEW;
    }

    @GetMapping(STUDENT_ADD_COURSE_URI + "/{studentId}/{courseId}")
    public String addCourse(
        @NotNull @PathVariable Long studentId,
        @NotNull @PathVariable Long courseId
    ) {
        studentService.addCourse(studentId, courseId);

        log.debug("Added the course with id {} to the student with id {}", courseId, studentId);

        return REDIRECT + STUDENT_HOME_URI + STUDENT_UPDATE_COURSES_URI + "/" + studentId;
    }

    @GetMapping(STUDENT_REMOVE_COURSE_URI + "/{studentId}/{courseId}")
    public String removeCourse(
        @NotNull @PathVariable Long studentId,
        @NotNull @PathVariable Long courseId
    ) {
        studentService.removeCourse(studentId, courseId);

        log.debug("Removed the course with id {} from the student with id {}", courseId, studentId);

        return REDIRECT + STUDENT_HOME_URI + STUDENT_UPDATE_COURSES_URI + "/" + studentId;
    }

    @GetMapping(STUDENT_UPDATE_ADDRESS_URI + "/{id}")
    public String updateAddressView(@NotNull @PathVariable Long id, Model model) {
        var studentAddress = studentService.getAddressDto(id);

        model.addAttribute(ADDRESS_MODEL_ATTR, studentAddress);

        log.debug("Displayed the student update address view for student with id {}", id);

        return STUDENT_UPDATE_ADDRESS_VIEW;
    }

    @PostMapping(STUDENT_UPDATE_ADDRESS_URI + "/{id}")
    public String updateAddress(
        @NotNull @PathVariable Long id,
        @Valid @ModelAttribute(STUDENT_MODEL_ATTR) AddressDto addressDto,
        Errors errors
    ) {
        if (errors.hasErrors()) {
            log.error("{} has validation errors: {}", addressDto, errors.getAllErrors());

            return STUDENT_UPDATE_ADDRESS_VIEW;
        }
        studentService.updateAddress(id, addressDto);

        log.debug("Updated address for student with id {}", id);

        return REDIRECT + STUDENT_HOME_URI + STUDENT_INFO_URI + "/" + id;
    }

    @GetMapping(STUDENT_DELETE_URI + "/{id}")
    public String remove(@NotNull @PathVariable Long id) {
        studentService.deleteById(id);

        log.debug("Removed student with id {}", id);

        return REDIRECT + STUDENT_HOME_URI;
    }
}
