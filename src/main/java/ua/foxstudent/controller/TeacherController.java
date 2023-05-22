package ua.foxstudent.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxstudent.dto.teacher.TeacherCreateDto;
import ua.foxstudent.dto.teacher.TeacherDegreDto;
import ua.foxstudent.dto.teacher.TeacherPostDto;
import ua.foxstudent.service.CourseService;
import ua.foxstudent.service.GroupService;
import ua.foxstudent.service.TeacherService;
import ua.foxstudent.dto.person.AddressDto;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private static final String REDIRECT = "redirect:";
    private static final String TEACHER_HOME_URI = "/teacher";
    private static final String TEACHER_ADD_URI = "/add";
    private static final String TEACHER_DELETE_URI = "/delete";
    private static final String TEACHER_UPDATE_POST_URI = "/update/post";
    private static final String TEACHER_UPDATE_DEGREE_URI = "/update/degree";
    private static final String TEACHER_UPDATE_ADDRESS_URI = "/update/address";
    private static final String TEACHER_UPDATE_COURSES_URI = "/update/courses";
    private static final String TEACHER_ADD_COURSE_URI = "/courses/add";
    private static final String TEACHER_REMOVE_COURSE_URI = "/courses/remove";
    private static final String TEACHER_INFO_URI = "/info";
    private static final String TEACHER_ADD_VIEW = "teacher/add";
    private static final String TEACHER_UPDATE_COURSES_VIEW = "teacher/update-courses";
    private static final String TEACHER_UPDATE_ADDRESS_VIEW = "teacher/update-address";
    private static final String TEACHER_UPDATE_POST_VIEW = "teacher/update-post";
    private static final String TEACHER_UPDATE_DEGREE_VIEW = "teacher/update-degree";
    private static final String PERSON_INFO_VIEW = "person/info";
    private static final String TEACHER_LIST_VIEW = "teacher/list";
    private static final String TEACHERS_MODEL_ATTR = "teachers";
    private static final String TEACHER_MODEL_ATTR = "teacher";
    private static final String PERSON_MODEL_ATTR = "person";
    private static final String GROUPS_MODEL_ATTR = "groups";
    private static final String COURSES_MODEL_ATTR = "courses";
    private static final String ADDRESS_MODEL_ATTR = "address";
    
    private final TeacherService teacherService;
    private final GroupService groupService;
    private final CourseService courseService;
    
    @GetMapping(TEACHER_ADD_URI)
    public String saveView(Model model) {
        model.addAttribute(TEACHER_MODEL_ATTR, new TeacherCreateDto());
        model.addAttribute(GROUPS_MODEL_ATTR, groupService.getAll());
        model.addAttribute(COURSES_MODEL_ATTR, courseService.getAll());
        
        return TEACHER_ADD_VIEW;
    }
    
    @PostMapping(TEACHER_ADD_URI)
    public String save(
        @Valid @ModelAttribute(TEACHER_MODEL_ATTR) TeacherCreateDto teacherCreateDto,
        Errors errors
    ) {
        if (errors.hasErrors()) {
            return TEACHER_ADD_VIEW;
        }
        var teacherId = teacherService.add(teacherCreateDto);
        
        return REDIRECT + TEACHER_HOME_URI + TEACHER_INFO_URI + "/" + teacherId;
    }
    
    @GetMapping(TEACHER_INFO_URI + "/{id}")
    public String infoView(@NotNull @PathVariable Long id, Model model) {
        var teacherInfo = teacherService.getPersonalInfoById(id);
        
        model.addAttribute(PERSON_MODEL_ATTR, teacherInfo);
        
        return PERSON_INFO_VIEW;
    }
    
    @GetMapping
    public String listView(Model model) {
        var allTeacherDTO = teacherService.getAllTeacherListViewDtos();
        model.addAttribute(TEACHERS_MODEL_ATTR, allTeacherDTO);
        model.addAttribute(TEACHER_MODEL_ATTR, new TeacherCreateDto());
        model.addAttribute(GROUPS_MODEL_ATTR, groupService.getAll());
        model.addAttribute(COURSES_MODEL_ATTR, courseService.getAll());
        
        return TEACHER_LIST_VIEW;
    }
    
    @GetMapping(TEACHER_UPDATE_COURSES_URI + "/{id}")
    public String updateCoursesView(@NotNull @PathVariable Long id, Model model) {
        var teacherDto = teacherService.getTeacherCoursesDtoById(id);
        var courses = courseService.getAll();
        courses.removeAll(teacherDto.getCourses());
        
        model.addAttribute(TEACHER_MODEL_ATTR, teacherDto);
        model.addAttribute(COURSES_MODEL_ATTR, courses);
        
        return TEACHER_UPDATE_COURSES_VIEW;
    }
    
    @GetMapping(TEACHER_ADD_COURSE_URI + "/{teacherId}/{courseId}")
    public String addCourse(
        @NotNull @PathVariable Long teacherId,
        @NotNull @PathVariable Long courseId
    ) {
        teacherService.addCourse(teacherId, courseId);
        
        return REDIRECT + TEACHER_HOME_URI + TEACHER_UPDATE_COURSES_URI + "/" + teacherId;
    }
    
    @GetMapping(TEACHER_REMOVE_COURSE_URI + "/{teacherId}/{courseId}")
    public String removeCourse(
        @NotNull @PathVariable Long teacherId,
        @NotNull @PathVariable Long courseId
    ) {
        teacherService.removeCourse(teacherId, courseId);
        
        return REDIRECT + TEACHER_HOME_URI + TEACHER_UPDATE_COURSES_URI + "/" + teacherId;
    }

    @GetMapping(TEACHER_UPDATE_POST_URI + "/{id}")
    public String updatePostView(@NotNull @PathVariable Long id, Model model) {
        model.addAttribute(TEACHER_MODEL_ATTR, new TeacherPostDto(id));

        return TEACHER_UPDATE_POST_VIEW;
    }

    @PostMapping(TEACHER_UPDATE_POST_URI + "/{id}")
    public String updatePost(
        @NotNull @PathVariable Long id,
        @NotNull @ModelAttribute(TEACHER_MODEL_ATTR) TeacherPostDto post
    ) {
        teacherService.updatePost(id, post);

        return REDIRECT + TEACHER_HOME_URI + TEACHER_UPDATE_POST_URI + "/" + id;
    }

    @GetMapping(TEACHER_UPDATE_DEGREE_URI + "/{id}")
    public String updateDegreeView(@NotNull @PathVariable Long id, Model model) {
        model.addAttribute(TEACHER_MODEL_ATTR, new TeacherDegreDto(id));

        return TEACHER_UPDATE_DEGREE_VIEW;
    }

    @PostMapping(TEACHER_UPDATE_DEGREE_URI + "/{id}")
    public String updateDegree(
        @NotNull @PathVariable Long id,
        @NotNull @ModelAttribute(TEACHER_MODEL_ATTR) TeacherDegreDto degree
    ) {
        teacherService.updateDegree(id, degree);

        return REDIRECT + TEACHER_HOME_URI + TEACHER_UPDATE_DEGREE_URI + "/" + id;
    }
    
    @GetMapping(TEACHER_UPDATE_ADDRESS_URI + "/{id}")
    public String updateAddressView(@NotNull @PathVariable Long id, Model model) {
        var teacherAddress = teacherService.getAddressDto(id);
        
        model.addAttribute(ADDRESS_MODEL_ATTR, teacherAddress);
        
        return TEACHER_UPDATE_ADDRESS_VIEW;
    }
    
    @PostMapping(TEACHER_UPDATE_ADDRESS_URI + "/{id}")
    public String updateAddress(
        @NotNull @PathVariable Long id,
        @Valid @ModelAttribute(TEACHER_MODEL_ATTR) AddressDto addressDto,
        Errors errors
    ) {
        if (errors.hasErrors()) {
            return TEACHER_UPDATE_ADDRESS_VIEW;
        }
        teacherService.updateAddress(id, addressDto);
        
        return REDIRECT + TEACHER_HOME_URI + TEACHER_INFO_URI + "/" + id;
    }
    
    @GetMapping(TEACHER_DELETE_URI + "/{id}")
    public String remove(@NotNull @PathVariable Long id) {
        teacherService.deleteById(id);
        
        return REDIRECT + TEACHER_HOME_URI;
    }
}
