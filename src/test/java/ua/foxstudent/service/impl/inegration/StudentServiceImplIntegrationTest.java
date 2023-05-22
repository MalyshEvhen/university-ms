package ua.foxstudent.service.impl.inegration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxstudent.domain.person.Address;
import ua.foxstudent.dto.person.AddressDto;
import ua.foxstudent.dto.student.StudentCreateDto;
import ua.foxstudent.service.CourseService;
import ua.foxstudent.service.GroupService;
import ua.foxstudent.service.StudentService;
import ua.foxstudent.service.exceptions.StudentNotFoundException;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = "spring.datasource.url=jdbc:tc:postgresql:15:///university_ms_db"
)
//@Transactional
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentServiceImplIntegrationTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ModelMapper modelMapper;

    @DisplayName("StudentService::add should add valid new Student")
    @Test
    void add() {
        // given:
        var group = groupService.getById(1L);
        var studentDto = new StudentCreateDto();
        studentDto.setFirstName("First Name");
        studentDto.setLastName("Last Name");
        studentDto.setPhone("+123456789");
        studentDto.setUserEmail("email@gmail.com");
        studentDto.setUserPassword("password");
        studentDto.setGroup(group);
        studentDto.setAddressCity("Kyiv");
        studentDto.setAddressStreet("Grushevskogo");
        studentDto.setAddressNumber("2-B");
        studentDto.setAddressApt("23");
        studentDto.setAddressZip("987342");

        int expected = studentService.getAll().size() + 1;

        // when:
        studentService.add(studentDto);
        int actual = studentService.getAll().size();

        // then:
        Assertions.assertEquals(expected, actual);
    }

    @Disabled
    @Test
    void getById() {
    }

    @Disabled
    @Test
    void getActivitiesById() {
    }

    @Disabled
    @Test
    void getPersonalInfoById() {
    }

    @Disabled
    @Test
    void getAll() {
    }

    @Disabled
    @Test
    void update() {
    }

    @Disabled
    @Test
    void updateActivities() {
    }

    @DisplayName("StudentService::updateAddress should update address fields")
    @Test
    void updateAddress() {
        // given:
        long id = 2L;
        var student = studentService.getById(id);
        var addressDto = modelMapper.map(student, AddressDto.class);

        // when:
        var newCity = "New City";
        var newStreet = "New Street";
        var newNumber = "9A";
        var newApt = "New Apt";
        var newZip = "456564";

        addressDto.setCity(newCity);
        addressDto.setStreet(newStreet);
        addressDto.setNumber(newNumber);
        addressDto.setApt(newApt);
        addressDto.setZip(newZip);

        studentService.updateAddress(id, addressDto);
        

        // then:
        Address updatedAddress = studentService.getById(id).getAddress();
        
        Assertions.assertEquals(newCity, updatedAddress.getCity());
        Assertions.assertEquals(newStreet, updatedAddress.getStreet());
        Assertions.assertEquals(newNumber, updatedAddress.getNumber());
        Assertions.assertEquals(newApt, updatedAddress.getApt());
        Assertions.assertEquals(newZip, updatedAddress.getZip());
    }

    @DisplayName("StudentService::deleteById delete only targeted Student record")
    @Test
    void deleteById() {
        // given:
        long id = 1L;

        // when:
        var student = studentService.getById(id);
        var groupBefore = student.getGroup();
        var coursesBefore = student.getCourses();

        // then:
        Assertions.assertEquals(id, student.getId());

        // when:
        studentService.deleteById(id);
        var groupAfter = groupService.getById(groupBefore.getId());
        var allCursesAfter = courseService.getAll();

        // then:
        Assertions.assertThrows(StudentNotFoundException.class, () -> studentService.getById(id));
        Assertions.assertEquals(groupBefore, groupAfter);
        Assertions.assertTrue(allCursesAfter.containsAll(coursesBefore));
    }
}