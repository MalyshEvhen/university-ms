package ua.foxstudent.dto.teacher;

import lombok.Data;
import ua.foxstudent.domain.person.ProfessionalPost;

@Data
public class TeacherPostDto {
    private Long id;
    private ProfessionalPost professionalPost;
    
    public TeacherPostDto() {
    }
    
    public TeacherPostDto(Long id) {
        this.id = id;
    }
}
