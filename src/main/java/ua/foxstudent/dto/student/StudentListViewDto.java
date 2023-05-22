package ua.foxstudent.dto.student;

import lombok.Data;
import ua.foxstudent.domain.activity.Course;
import ua.foxstudent.domain.activity.Group;

import java.util.ArrayList;
import java.util.List;

@Data
public class StudentListViewDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Group group;
    private List<Course> courses = new ArrayList<>();
    
    public StudentListViewDto() {
    }
    
    public StudentListViewDto(
        Long id,
        String firstName,
        String lastName,
        Group group,
        List<Course> courses
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
        this.courses = courses;
    }
}
