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
import ua.foxstudent.domain.activity.Course;
import ua.foxstudent.service.CourseService;

@Slf4j
@Controller
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {
    private static final String COURSE_HOME_URI = "/course";
    private static final String COURSE_ADD_URI = "/add";
    private static final String COURSE_DELETE_URI = "/delete";
    private static final String COURSE_UPDATE_URI = "/update";
    private static final String COURSE_INFO_URI = "/info";
    private static final String COURSES_MODEL_ATTRIBUTE = "courses";
    private static final String COURSE_MODEL_ATTRIBUTE = "course";
    private static final String COURSE_LIST_VIEW = "course/list";
    private static final String COURSE_ADD_VIEW = "course/add";
    private static final String COURSE_UPDATE_VIEW = "course/update";
    private static final String COURSE_INFO_VIEW = "course/info";
    private static final String REDIRECT = "redirect:";

    private final CourseService courseService;

    @GetMapping(COURSE_ADD_URI)
    public String saveView(Model model) {
        log.info("Processing view 'course/add' template...");

        model.addAttribute(COURSE_MODEL_ATTRIBUTE, new Course());

        return COURSE_ADD_VIEW;
    }

    @PostMapping(COURSE_ADD_URI)
    public String save(@Valid @ModelAttribute(COURSE_MODEL_ATTRIBUTE) Course course,
                       Errors errors) {
        log.info("Processing save new Course: {} ", course.toString());

        if (errors.hasErrors()) {
            log.error("{} has validation errors: {}", course, errors.getAllErrors());

            return COURSE_ADD_VIEW;
        }

        log.debug("{} is valid, and passing to the service", course);

        var savedCourseId = courseService.add(course);

        log.info("Redirect to course info");

        return REDIRECT + COURSE_HOME_URI + COURSE_INFO_URI + "/" + savedCourseId;
    }

    @GetMapping
    public String listView(Model model) {
        log.info("Processing view 'course/list' template...");

        var allCourses = courseService.getAll();

        model.addAttribute(COURSES_MODEL_ATTRIBUTE, allCourses);

        log.debug("{} courses was retrieve", allCourses.size());

        return COURSE_LIST_VIEW;
    }

    @GetMapping(COURSE_INFO_URI + "/{id}")
    public String infoView(@PathVariable Long id, Model model) {
        log.info("Processing view 'course/info' template...");

        var course = courseService.getById(id);

        model.addAttribute(COURSE_MODEL_ATTRIBUTE, course);

        log.debug("Course was found: {}", course);

        return COURSE_INFO_VIEW;
    }

    @GetMapping(COURSE_UPDATE_URI + "/{id}")
    public String updateView(@PathVariable Long id, Model model) {
        log.info("Processing view 'course/update' template...");

        model.addAttribute(COURSE_MODEL_ATTRIBUTE, courseService.getById(id));

        return COURSE_UPDATE_VIEW;
    }

    @PostMapping(COURSE_UPDATE_URI + "/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute(COURSE_MODEL_ATTRIBUTE) Course course,
                         Errors errors) {
        log.info("Processing updating the Course: {} ", course);

        if (errors.hasErrors()) {
            log.error("{} has validation errors: {}", course, errors.getAllErrors());

            return COURSE_UPDATE_VIEW;
        }

        log.debug("{} is valid", course);

        courseService.update(id, course);

        log.debug("{} pass to service", course);

        return REDIRECT + COURSE_HOME_URI + COURSE_INFO_URI + "/" + id;
    }

    @GetMapping(COURSE_DELETE_URI + "/{id}")
    public String remove(@NotNull @PathVariable Long id) {
        log.info("Passing id: {} to the service for delete Course", id);

        courseService.deleteById(id);

        log.info("Redirect to /course");

        return REDIRECT + COURSE_HOME_URI;
    }
}
