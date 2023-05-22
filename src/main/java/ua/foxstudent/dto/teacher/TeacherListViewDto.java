package ua.foxstudent.dto.teacher;

import lombok.Data;
import ua.foxstudent.domain.person.AcademicDegree;
import ua.foxstudent.domain.person.ProfessionalPost;
import ua.foxstudent.domain.activity.Course;

import java.util.ArrayList;
import java.util.List;

@Data
public class TeacherListViewDto {
    private Long id;
    private String firstName;
    private String lastName;
    private AcademicDegree academicDegree;
    private ProfessionalPost professionalPost;
    private List<Course> courses = new ArrayList<>();

    public TeacherListViewDto() {
    }

    public TeacherListViewDto(Long id,
                              String firstName,
                              String lastName,
                              AcademicDegree academicDegree,
                              ProfessionalPost professionalPost,
                              List<Course> courses) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.academicDegree = academicDegree;
        this.professionalPost = professionalPost;
        this.courses = courses;
    }
}
