package ua.foxstudent.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.foxstudent.service.GroupService;
import ua.foxstudent.domain.activity.Group;
import ua.foxstudent.repository.GroupRepository;
import ua.foxstudent.service.exceptions.GroupAlreadyExistsException;
import ua.foxstudent.service.exceptions.GroupNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;
    
    private GroupService groupService;

    @BeforeEach
    void setUp() {
        groupService = new GroupServiceImpl(groupRepository);
    }

    @DisplayName("GroupServiceImpl::add should pass Group to GroupRepository::save")
    @Test
    void addShouldPassGroupToRepo() {
        // given:
        var group = new Group(1L, "New Group");

        // when:
        when(groupRepository.save(group)).thenReturn(group);

        // then:
        groupService.add(group);
        verify(groupRepository, times(1)).findByName(group.getName());
        verify(groupRepository, times(1)).save(group);
    }

    @DisplayName("GroupServiceImpl::add should throw an exception when Group is already exists")
    @Test
    void addShouldThrowException() {
        // given:
        var group = new Group("New Group");

        // when:
        when(groupRepository.findByName(group.getName())).thenReturn(Optional.of(group));

        // then:
        assertThrows(GroupAlreadyExistsException.class,
            () -> groupService.add(group),
            "Group with name: " + group.getName() + " already exists");
    }

    @DisplayName("GroupServiceImpl::getById should should return existing Group")
    @Test
    void getByIdShouldPassIdToRepoAndReturnGroup() {
        // given:
        var expected = new Group(1L, "Group");

        // when:
        when(groupRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        var actual = groupService.getById(expected.getId());

        // then:
        assertEquals(expected, actual);
    }

    @DisplayName("GroupServiceImpl::getById should throw an exception when Group is not found")
    @Test
    void getByIdShouldThrowException() {
        // given:
        var id = 1L;

        // when:
        when(groupRepository.findById(id)).thenReturn(Optional.empty());

        // then:
        assertThrows(GroupNotFoundException.class,
            () -> groupService.getById(id),
            "Group with id: " + id + " was not found");
    }

    @DisplayName("GroupServiceImpl::update should pass updated Group to repository")
    @Test
    void updateShouldPassUpdatedGroupToRepo() {
        // given:
        var id = 1L;
        var existingGroup = new Group(id, "Name");
        var expected = new Group(id, "Updated name");

        // when:
        when(groupRepository.findById(id)).thenReturn(Optional.of(existingGroup));

        // then:
        groupService.update(id, expected);
        verify(groupRepository, times(1)).save(expected);
    }

    @DisplayName("GroupServiceImpl::update should throw an exception when Group is not found")
    @Test
    void updateShouldThrowException() {
        // given:
        var id = 1L;
        var group = new Group(id, "Updated name");


        // when:
        when(groupRepository.findById(id)).thenReturn(Optional.empty());

        // then:
        assertThrows(GroupNotFoundException.class,
            () -> groupService.update(id, group),
            "Group with id: " + id + " was not found");
    }

    @DisplayName("GroupServiceImpl::deleteById should invoke GroupRepository::delete with existing Group argument")
    @Test
    void deleteByIdShouldInvokeDeleteMethodInRepo() {
        // given:
        var id = 1L;
        var existingGroup = new Group(id, "Name");

        // when:
        when(groupRepository.findById(id)).thenReturn(Optional.of(existingGroup));

        // then:
        groupService.deleteById(id);
        verify(groupRepository, times(1)).delete(existingGroup);
    }

    @DisplayName("GroupServiceImpl::deleteById should throw an exception when Group is not found")
    @Test
    void deleteByIdShouldThrowException() {
        // given:
        var id = 1L;

        // when:
        when(groupRepository.findById(id)).thenReturn(Optional.empty());

        // then:
        assertThrows(GroupNotFoundException.class,
            () -> groupService.deleteById(id),
            "Group with id: " + id + " was not found");
    }

    @DisplayName("GroupServiceImpl::getAll should invoke GroupRepository::findAll")
    @Test
    void getAllShouldReturnListOfGroups() {
        // when:
        groupService.getAll();

        // then:
        verify(groupRepository, times(1)).findAll();
    }
}