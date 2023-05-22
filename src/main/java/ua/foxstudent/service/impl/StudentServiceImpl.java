package ua.foxstudent.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxstudent.domain.person.Address;
import ua.foxstudent.domain.user.Role;
import ua.foxstudent.dto.person.AddressDto;
import ua.foxstudent.dto.student.StudentListViewDto;
import ua.foxstudent.service.CourseService;
import ua.foxstudent.service.StudentService;
import ua.foxstudent.service.UserService;
import ua.foxstudent.service.exceptions.TeacherNotFoundException;
import ua.foxstudent.domain.person.Student;
import ua.foxstudent.dto.person.PersonalInfoDto;
import ua.foxstudent.dto.student.StudentCreateDto;
import ua.foxstudent.dto.student.StudentCoursesDto;
import ua.foxstudent.repository.StudentRepository;
import ua.foxstudent.service.exceptions.StudentNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private static final String NOT_FOUND = "Student with id: %d not found";
    
    private final StudentRepository studentRepository;
    private final CourseService courseService;
    private final UserService userService;
    private final ModelMapper mapper;
    
    @Override
    public Long add(StudentCreateDto studentCreateDto) {
        var student = Optional.of(studentCreateDto)
            .map(dto -> mapper.map(dto, Student.class))
            .orElseThrow();
        
        var studentUser = student.getUser();
        studentUser.addRole(Role.ROLE_STUDENT);
        
        userService.save(studentUser);
        
        return studentRepository.save(student).getId();
    }
    
    @Override
    public Student getById(Long id) {
        return studentRepository.findById(id)
            .orElseThrow(() -> new StudentNotFoundException(String.format(NOT_FOUND, id)));
    }
    
    @Override
    public StudentCoursesDto getStudentCoursesDtoById(Long id) {
        return studentRepository.findById(id)
            .map(s -> mapper.map(s, StudentCoursesDto.class))
            .orElseThrow(() -> new StudentNotFoundException(String.format(NOT_FOUND, id)));
    }
    
    @Override
    public PersonalInfoDto getPersonalInfoById(Long id) {
        return studentRepository.findPersonalInfoDtoById(id)
            .orElseThrow(() -> new StudentNotFoundException(String.format(NOT_FOUND, id)));
    }

    @Override
    public List<Student> getAll() {
        return studentRepository.findAll();
    }
    
    @Override
    public AddressDto getAddressDto(Long id) {
        return studentRepository.findAddressDtoById(id)
            .orElseThrow(() -> new StudentNotFoundException(String.format(NOT_FOUND, id)));
    }

    @Override
    public List<StudentListViewDto> getAllStudentListViewDtos() {
        return studentRepository.findAll()
            .stream()
            .map(s -> mapper.map(s, StudentListViewDto.class))
            .toList();
    }

    @Override
    public void updateAddress(Long id, AddressDto addressDto) {
        if (studentRepository.existsById(id)) {
            var address = mapper.map(addressDto, Address.class);

            studentRepository.updateAddressById(address, id);
        } else {
            throw new TeacherNotFoundException(String.format(NOT_FOUND, id));
        }
    }

    @Override
    public void addCourse(Long studentId, Long courseId) {
        var course = courseService.getById(courseId);

        studentRepository.findById(studentId).ifPresentOrElse(
            s -> s.addCourses(Set.of(course)),
            () ->
                {
                    throw new StudentNotFoundException(String.format(
                        NOT_FOUND,
                        studentId
                    ));
                }
        );
    }

    @Override
    public void removeCourse(Long studentId, Long courseId) {
        var course = courseService.getById(courseId);

        studentRepository.findById(studentId).ifPresentOrElse(
            s -> s.removeCourses(Set.of(course)),
            () ->
                {
                    throw new StudentNotFoundException(String.format(
                        NOT_FOUND,
                        studentId
                    ));
                }
        );
    }

    @Override
    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }
}
