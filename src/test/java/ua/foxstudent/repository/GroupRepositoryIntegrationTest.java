package ua.foxstudent.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.validation.ConstraintViolationException;
import ua.foxstudent.domain.activity.Group;

@DataJpaTest(properties = "spring.datasource.url=jdbc:tc:postgresql:12:///ums_db")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GroupRepositoryIntegrationTest {

    @Autowired
    private GroupRepository groupRepository;


    @DisplayName("GroupRepository::Save should save new Group to DB it is valid")
    @Test
    void groupRepositorySaveShouldSaveNewGroupToDatabaseWhenPassValidGroup() {
        // given:
        var group = new Group("Valid_name");

        // when:
        var savedGroup = groupRepository.save(group);

        // expect:
        Assertions.assertEquals(group, savedGroup);
        Assertions.assertNotNull(savedGroup.getId());
    }

    @DisplayName("GroupRepository::Save should throw validation exception Group.name is blank or empty")
    @ParameterizedTest
    @ValueSource(strings = {
        "   ",
        " ",
        ""
    })
    void groupRepositorySaveShouldThrowConstraintViolationExceptionWhenPassInvalidGroupParameters(
        String name) {
        // given:
        var group = new Group(name);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            groupRepository.save(group);
            groupRepository.flush();
        });
    }

    @DisplayName("GroupRepository::Save should throw validation exception Group.name is null")
    @Test
    void groupRepositorySaveShouldThrowConstraintViolationExceptionWhenPassNullInsteadGroupParameters() {
        // given:
        var group = new Group(null);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            groupRepository.save(group);
            groupRepository.flush();
        });
    }

    @DisplayName("GroupRepository::FindById should return existing Group from DB")
    @Test
    void groupRepositoryFindByIdShouldReturnExistingGroupFromDatabase() {
        // given:
        long id = 1L;

        // when:
        var group = groupRepository.findById(id).orElseThrow();

        // expect:
        Assertions.assertEquals(id, group.getId());
    }

    @DisplayName("GroupRepository::FindById should return Optional.empty if Group is not found")
    @Test
    void groupRepositoryFindByIdShouldReturnOptionalEmptyFromDatabaseIfGroupIsNotExists() {
        // given:
        long id = 100L;

        // when:
        var optionalGroup = groupRepository.findById(id);

        // expect:
        Assertions.assertTrue(optionalGroup.isEmpty());
    }

    @DisplayName("GroupRepository::Update should update existing Group if Group.name is valid")
    @Test
    void groupRepositoryUpdateShouldUpdateExistingGroupInDatabase() {
        // given:
        long id = 1L;
        var nameForUpdate = "UpdatedName";

        // when:
        var groupToUpdate = groupRepository.findById(id).orElseThrow();
        groupToUpdate.setName(nameForUpdate);
        groupRepository.save(groupToUpdate);
        groupRepository.flush();

        // then:
        var updatedGroup = groupRepository.findById(id).orElseThrow();

        // expect:
        Assertions.assertEquals(nameForUpdate, updatedGroup.getName());
    }

    @DisplayName("GroupRepository::Update should throw validation exception if Group.name is blank")
    @Test
    void groupRepositoryUpdateShouldThrowConstraintViolationExceptionIfNameIsBlank() {
        // given:
        long id = 1L;
        var nameForUpdate = "";

        // when:
        var groupToUpdate = groupRepository.findById(id).orElseThrow();
        groupToUpdate.setName(nameForUpdate);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            groupRepository.save(groupToUpdate);
            groupRepository.flush();
        });
    }

    @DisplayName("GroupRepository::Update should throw validation exception if Group.name is null")
    @Test
    void groupRepositoryUpdateShouldThrowConstraintViolationExceptionIfNameIsNull() {
        // given:
        long id = 1L;
        String nameForUpdate = null;

        // when:
        var groupToUpdate = groupRepository.findById(id).orElseThrow();
        groupToUpdate.setName(nameForUpdate);

        // expect:
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            groupRepository.save(groupToUpdate);
            groupRepository.flush();
        });
    }

    @DisplayName("GroupRepository::DeleteById should remove existing group from DB")
    @Test
    void groupRepositoryDeleteByIdShouldRemoveExistingGroupFromDatabase() {
        // given:
        long id = 1L;

        // when:
        groupRepository.deleteById(id);
        var deletedGroup = groupRepository.findById(id);

        // expect:
        Assertions.assertTrue(deletedGroup.isEmpty());
    }
}