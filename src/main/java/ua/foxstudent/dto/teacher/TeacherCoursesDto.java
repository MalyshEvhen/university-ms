package ua.foxstudent.dto.teacher;

import lombok.Data;
import ua.foxstudent.domain.activity.Course;

import java.util.ArrayList;
import java.util.List;

@Data
public class TeacherCoursesDto {
    private Long id;
    private String firstName;
    private String lastName;
    private List<Course> courses = new ArrayList<>();
    
    public TeacherCoursesDto() {
    }
    
    public TeacherCoursesDto(Long id) {
        this.id = id;
    }
    
    public TeacherCoursesDto(
        Long id,
        String firstName,
        String lastName,
        List<Course> courses
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.courses = courses;
    }
}
