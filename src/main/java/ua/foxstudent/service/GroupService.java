package ua.foxstudent.service;

import ua.foxstudent.domain.activity.Group;

import java.util.List;

public interface GroupService {

    Long add(Group group);

    Group getById(Long id);

    List<Group> getAll();

    void update(Long id, Group group);

    void deleteById(Long id);
}
