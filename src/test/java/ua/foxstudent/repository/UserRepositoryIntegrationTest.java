package ua.foxstudent.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.foxstudent.domain.user.Role;
import ua.foxstudent.domain.user.User;


@DataJpaTest(properties = "spring.datasource.url=jdbc:tc:postgresql:12:///ums_db")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;


    @DisplayName("UserRepository::Save should save new User to DB if it valid")
    @Test
    void userRepositorySaveShouldSaveNewUserToDatabaseWhenPassValidUser() {
        // given:
        var user = new User("valid_email@gmail.com", "password");
        user.addRole(Role.ROLE_STUDENT);

        // when:
        var savedUser = userRepository.save(user);

        // expect:
        Assertions.assertEquals(user, savedUser);
    }

    @DisplayName("UserRepository::Save should trow validation exception if User.email or User.password is invalid")
    @ParameterizedTest
    @CsvSource(
        {
            "invalid_email, valid_password",
            "valid@email.com, ",
            ",",
            " , "
        }
    )
    void userRepositorySaveShouldTrowConstraintViolationExceptionWhenPassUserWithInvalidEmailAndPassword(
        String email,
        String password) {
        // given:
        var user = new User(email, password);
        user.addRole(Role.ROLE_TEACHER);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            userRepository.save(user);
            userRepository.flush();
        });
    }

    @DisplayName("UserRepository::Save should trow validation exception User.role is null")
    @Test
    void userRepositorySaveShouldTrowConstraintViolationExceptionWhenPassUserWithEmptyRole() {
        // given:
        var user = new User("valid@email.com", "valid_password");

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            userRepository.save(user);
            userRepository.flush();
        });
    }

    @DisplayName("UserRepository::FindById should return existing user by User.id rom DB")
    @Test
    void userRepositoryFindByIdShouldReturnExistingUserByIdFromDatabase() {
        // when:
        var existingUser = userRepository.findById(1L).orElseThrow();

        // expect:
        Assertions.assertEquals(1L, existingUser.getId());
    }

    @DisplayName("UserRepository::FindById should return Optional.empty if User is not found")
    @Test
    void userRepositoryFindByIdShouldReturnOptionalEmptyIfUserIsNotExistsInDB() {
        // given:
        long id = 100L;

        // when:
        var optionalUserById = userRepository.findById(id);

        // expect:
        Assertions.assertTrue(optionalUserById.isEmpty());
    }

    @DisplayName("UserRepository::Update should update existing User if User.password is valid")
    @Test
    void userRepositoryUpdateShouldUpdateExistingUserFromDB() {
        // given:
        long id = 1L;
        var userPasswordToUpdate = "password";

        // when:
        var userToUpdate = userRepository.findById(id).orElseThrow();
        userToUpdate.setPassword(userPasswordToUpdate);
        userRepository.save(userToUpdate);
        userRepository.flush();

        // then:
        var updatedUser = userRepository.findById(id);
        var updatedUserEmail = updatedUser.map(User::getPassword).orElseThrow();

        // expect:
        Assertions.assertEquals(userPasswordToUpdate, updatedUserEmail);
    }

    @DisplayName("UserRepository::Update should throw validation exception if User.password is invalid")
    @Test
    void userRepositoryUpdateShouldTrowConstraintViolationExceptionIfPasswordIsBlank() {
        // given:
        long id = 1L;
        var userPasswordToUpdate = "";

        // when:
        var userToUpdate = userRepository.findById(id).orElseThrow();
        userToUpdate.setPassword(userPasswordToUpdate);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            userRepository.save(userToUpdate);
            userRepository.flush();
        });
    }

    @DisplayName("UserRepository::DeleteById should remove existing User from DB")
    @Test
    void userRepositoryDeleteByIdShouldRemoveExistingUserFromDB() {
        // given:
        long id = 1L;

        // when:
        userRepository.deleteById(id);
        var deletedUser = userRepository.findById(id);

        // expect:
        Assertions.assertTrue(deletedUser.isEmpty());
    }
}