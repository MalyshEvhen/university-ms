package ua.foxstudent.service.impl;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxstudent.service.GroupService;
import ua.foxstudent.domain.activity.Group;
import ua.foxstudent.repository.GroupRepository;
import ua.foxstudent.service.exceptions.GroupAlreadyExistsException;
import ua.foxstudent.service.exceptions.GroupNotFoundException;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    public static final String GROUP_NOT_FOUNT = "Group with id %d was not fount";
    private final GroupRepository groupRepository;
    
    @Override
    public Long add(@NotNull Group group) {
        log.info("Processing saving group {}", group);
        
        var groupName = group.getName();
        
        groupRepository.findByName(groupName).ifPresent(
            g ->
                {
                    String message = String.format("Group wid name: %s is already exits with id: %d",
                        groupName,
                        g.getId()
                    );
                    log.error(message);
                    
                    throw new GroupAlreadyExistsException(message);
                }
        );
        
        var savedGroup = groupRepository.save(group);
        
        log.debug("{} was saved", savedGroup);
        
        return savedGroup.getId();
    }
    
    @Override
    public void deleteById(Long id) {
        log.info("Processing removing group with id: {}", id);
        
        groupRepository.findById(id).ifPresentOrElse(
            groupRepository::delete,
            () ->
                {
                    var message = String.format(GROUP_NOT_FOUNT, id);
                    log.error(message);
                    
                    throw new GroupNotFoundException(message);
                }
        );
        
        log.debug("Group with id: {} was deleted", id);
    }
    
    @Transactional(readOnly = true)
    @Override
    public Group getById(Long id) {
        log.info("Retrieve group by id: {}", id);
        
        var groupById = groupRepository.findById(id).orElseThrow(
            () ->
                {
                    var message = String.format(GROUP_NOT_FOUNT, id);
                    log.error(message);
                    
                    return new GroupNotFoundException(message);
                }
        );
        
        log.debug("Group with id: {} was retrieved", id);
        
        return groupById;
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<Group> getAll() {
        log.info("Retrieve all groups...");
        
        var allGroups = groupRepository.findAll();
        
        log.debug("{} groups was retrieved", allGroups.size());
        
        return allGroups;
    }
    
    @Override
    public void update(Long id, Group group) {
        log.info("Processing updating group with id: {}", id);
        
        groupRepository.findById(id)
            .ifPresentOrElse(
                g ->
                    {
                        log.info("Group was retrieved: {}", g);
                        
                        g.setName(group.getName());
                        
                        groupRepository.save(g);
                        
                        log.debug("Group with id: {} was updated", id);
                    },
                () ->
                    {
                        var message = String.format(GROUP_NOT_FOUNT, id);
                        log.error(message);
                        
                        throw new GroupNotFoundException(message);
                    }
            );
    }
}
