package ua.foxstudent.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxstudent.domain.activity.Group;
import ua.foxstudent.service.GroupService;

@Slf4j
@Controller
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {
    private static final String GROUP_HOME_URI = "/group";
    private static final String GROUP_ADD_URI = "/add";
    private static final String GROUP_UPDATE_URI = "/update";
    private static final String GROUP_DELETE_URI = "/delete";
    private static final String GROUP_INFO_URI = "/info";
    private static final String GROUP_LIST_VIEW = "group/list";
    private static final String GROUP_ADD_VIEW = "group/add";
    private static final String GROUP_UPDATE_VIEW = "group/update";
    private static final String GROUP_INFO_VIEW = "group/info";
    private static final String GROUP_MODEL_ATR = "group";
    private static final String GROUPS_MODEL_ATR = "groups";
    private static final String REDIRECT = "redirect:";

    private final GroupService groupService;

    @GetMapping(GROUP_ADD_URI)
    public String saveView(Model model) {
        log.info("Processing group/add template...");

        model.addAttribute(GROUP_MODEL_ATR, new Group());

        return GROUP_ADD_VIEW;
    }

    @PostMapping(GROUP_ADD_URI)
    public String save(@Valid @ModelAttribute(GROUP_MODEL_ATR) Group group, Errors errors) {
        log.info("Processing save {}...", group);

        if (errors.hasErrors()) {
            log.error("{} has validation errors: {}", group, errors.getAllErrors());

            return GROUP_ADD_VIEW;
        }

        log.debug("{} is valid, and passing to the service", group);

        var savedGroupId = groupService.add(group);

        log.info("Redirect to group info");

        return REDIRECT + GROUP_HOME_URI + GROUP_INFO_URI + "/" + savedGroupId;
    }

    @GetMapping(GROUP_INFO_URI + "/{id}")
    public String infoView(@NotNull @PathVariable Long id, Model model) {
        log.info("Retrieving Group with id: {}...", id);

        var groupById = groupService.getById(id);

        log.debug("{} was found", groupById);

        model.addAttribute(GROUP_MODEL_ATR, groupById);

        return GROUP_INFO_VIEW;
    }

    @GetMapping
    public String getAll(Model model) {
        log.info("Retrieving all Groups...");

        var allGroups = groupService.getAll();

        log.debug("{} groups was retrieved", allGroups.size());

        model.addAttribute(GROUPS_MODEL_ATR, allGroups);

        return GROUP_LIST_VIEW;
    }

    @PostMapping(GROUP_UPDATE_URI + "/{id}")
    public String updateName(@NotNull @PathVariable Long id,
                              @Valid @ModelAttribute(GROUP_MODEL_ATR) Group group,
                              Errors errors
    ) {
        if (errors.hasErrors()) {
            log.error("{} has validation errors: {}", group, errors.getAllErrors());

            return GROUP_UPDATE_VIEW;
        }

        log.info("Updating Group with id: {} processing...", id);

        groupService.update(id, group);

        log.info("Redirect to /group/info");

        return REDIRECT + GROUP_HOME_URI + GROUP_INFO_URI + "/" + id;
    }

    @GetMapping(GROUP_UPDATE_URI + "/{id}")
    public String updateNameView(@NotNull @PathVariable Long id, Model model) {
        log.info("Processing group/info template...");

        var groupById = groupService.getById(id);

        model.addAttribute(GROUP_MODEL_ATR, groupById);

        return GROUP_UPDATE_VIEW;
    }

    @GetMapping(GROUP_DELETE_URI + "/{id}")
    public String remove(@NotNull @PathVariable Long id) {
        log.info("Passing id: {} to the service for delete Group", id);

        groupService.deleteById(id);

        log.info("Redirect to /group");

        return REDIRECT + GROUP_HOME_URI;
    }
}
