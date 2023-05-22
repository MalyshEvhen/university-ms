package ua.foxstudent.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxstudent.domain.person.Address;
import ua.foxstudent.domain.person.Teacher;
import ua.foxstudent.domain.user.Role;
import ua.foxstudent.dto.teacher.TeacherDegreDto;
import ua.foxstudent.dto.teacher.TeacherPostDto;
import ua.foxstudent.service.CourseService;
import ua.foxstudent.service.UserService;
import ua.foxstudent.dto.person.AddressDto;
import ua.foxstudent.dto.person.PersonalInfoDto;
import ua.foxstudent.dto.teacher.TeacherCoursesDto;
import ua.foxstudent.dto.teacher.TeacherCreateDto;
import ua.foxstudent.dto.teacher.TeacherListViewDto;
import ua.foxstudent.repository.TeacherRepository;
import ua.foxstudent.service.TeacherService;
import ua.foxstudent.service.exceptions.TeacherNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private static final String NOT_FOUND = "Teacher with id: %d not found";

    private final TeacherRepository teacherRepository;
    private final CourseService courseService;
    private final UserService userService;
    private final ModelMapper mapper;

    @Override
    public Long add(TeacherCreateDto teacherCreateDto) {
        var teacher = Optional.of(teacherCreateDto)
            .map(dto -> mapper.map(dto, Teacher.class))
            .orElseThrow();

        var user = teacher.getUser();
        user.addRole(Role.ROLE_TEACHER);

        userService.save(user);

        return teacherRepository.save(teacher).getId();
    }

    @Override
    public PersonalInfoDto getPersonalInfoById(Long id) {
        return teacherRepository.findPersonalInfoById(id)
            .orElseThrow(() -> new TeacherNotFoundException(String.format(NOT_FOUND, id)));
    }

    @Override
    public List<TeacherListViewDto> getAllTeacherListViewDtos() {
        return teacherRepository.findAll()
            .stream()
            .map(t -> mapper.map(t, TeacherListViewDto.class))
            .toList();
    }

    @Override
    public TeacherCoursesDto getTeacherCoursesDtoById(Long id) {
        return teacherRepository.findById(id)
            .map(t -> mapper.map(t, TeacherCoursesDto.class))
            .orElseThrow(() -> new TeacherNotFoundException(String.format(NOT_FOUND, id)));
    }

    @Override
    public void addCourse(Long teacherId, Long courseId) {
        var course = courseService.getById(courseId);

        teacherRepository.findById(teacherId).ifPresentOrElse(
            t -> t.addCourses(Set.of(course)),
            () -> {
                throw new TeacherNotFoundException(String.format(NOT_FOUND, teacherId));
            }
        );
    }

    @Override
    public void removeCourse(Long teacherId, Long courseId) {
        var course = courseService.getById(courseId);

        teacherRepository.findById(teacherId).ifPresentOrElse(
            t -> t.removeCourses(Set.of(course)),
            () -> {
                throw new TeacherNotFoundException(String.format(NOT_FOUND, teacherId));
            }
        );
    }

    @Override
    public AddressDto getAddressDto(Long id) {
        return teacherRepository.findAddressDtoById(id)
            .orElseThrow(() -> new TeacherNotFoundException(String.format(NOT_FOUND, id)));
    }

    @Override
    public void updateAddress(Long id, AddressDto addressDto) {
        var address = mapper.map(addressDto, Address.class);

        teacherRepository.updateAddressById(address, id);
    }

    @Override
    public void deleteById(Long id) {
        teacherRepository.deleteById(id);
    }

    @Override
    public void updatePost(Long teacherId, TeacherPostDto post) {
        var postForUpdate = post.getProfessionalPost();

        teacherRepository.updateProfessionalPostById(postForUpdate, teacherId);
    }

    @Override
    public void updateDegree(Long teacherId, TeacherDegreDto degree) {
        var degreeForUpdate = degree.getAcademicDegree();

        teacherRepository.updateAcademicDegreeById(degreeForUpdate, teacherId);
    }
}
