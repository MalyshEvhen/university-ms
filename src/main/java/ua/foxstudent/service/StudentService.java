package ua.foxstudent.service;

import ua.foxstudent.domain.person.Student;
import ua.foxstudent.dto.person.AddressDto;
import ua.foxstudent.dto.person.PersonalInfoDto;
import ua.foxstudent.dto.student.StudentListViewDto;
import ua.foxstudent.dto.student.StudentCoursesDto;
import ua.foxstudent.dto.student.StudentCreateDto;

import java.util.List;

public interface StudentService {
    Long add(StudentCreateDto student);

    Student getById(Long id);

    StudentCoursesDto getStudentCoursesDtoById(Long id);

    PersonalInfoDto getPersonalInfoById(Long id);

    List<Student> getAll();

    void updateAddress(Long id, AddressDto addressDto);

    void deleteById(Long id);
    
    AddressDto getAddressDto(Long id);
    
    void addCourse(Long studentId, Long courseId);
    
    void removeCourse(Long studentId, Long courseId);
    
    List<StudentListViewDto> getAllStudentListViewDtos();
}
