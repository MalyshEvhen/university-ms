package ua.foxstudent.dto.teacher;

import lombok.Data;
import ua.foxstudent.domain.person.AcademicDegree;

@Data
public class TeacherDegreDto {
    private Long id;
    private AcademicDegree academicDegree;

    public TeacherDegreDto() {
    }

    public TeacherDegreDto(Long id) {
        this.id = id;
    }
}
