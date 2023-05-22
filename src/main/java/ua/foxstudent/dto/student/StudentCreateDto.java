package ua.foxstudent.dto.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.foxstudent.domain.activity.Group;
import ua.foxstudent.domain.activity.Course;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCreateDto {

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    @NotBlank
    private String phone;

    @Email
    @NotNull
    @NotBlank
    private String userEmail;

    @NotNull
    @NotBlank
    private String userPassword;

    private Group group;

    private List<Course> courses = new ArrayList<>();

    @NotNull
    @NotBlank
    private String addressCity;

    @NotNull
    @NotBlank
    private String addressStreet;

    @NotNull
    @NotBlank
    private String addressNumber;

    private String addressApt;

    @NotNull
    @NotBlank
    private String addressZip;
}
