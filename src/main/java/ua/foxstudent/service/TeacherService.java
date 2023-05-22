package ua.foxstudent.service;

import ua.foxstudent.dto.teacher.TeacherDegreDto;
import ua.foxstudent.dto.teacher.TeacherPostDto;
import ua.foxstudent.dto.person.AddressDto;
import ua.foxstudent.dto.person.PersonalInfoDto;
import ua.foxstudent.dto.teacher.TeacherCoursesDto;
import ua.foxstudent.dto.teacher.TeacherCreateDto;
import ua.foxstudent.dto.teacher.TeacherListViewDto;

import java.util.List;

public interface TeacherService {
    Long add(TeacherCreateDto teacherCreateDto);

    PersonalInfoDto getPersonalInfoById(Long id);

    List<TeacherListViewDto> getAllTeacherListViewDtos();

    TeacherCoursesDto getTeacherCoursesDtoById(Long id);

    void addCourse(Long teacherId, Long courseId);

    void removeCourse(Long teacherId, Long courseId);

    AddressDto getAddressDto(Long id);

    void updateAddress(Long id, AddressDto addressDto);

    void deleteById(Long id);

    void updatePost(Long teacherId, TeacherPostDto post);

    void updateDegree(Long teacherId, TeacherDegreDto degree);
}
