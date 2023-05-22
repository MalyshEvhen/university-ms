package ua.foxstudent.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxstudent.domain.activity.Group;
import ua.foxstudent.service.GroupService;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = GroupController.class)
class GroupControllerTest {
    private static final String REDIRECT = "redirect:";
    private static final String GROUP_HOME_URI = "/group";
    private static final String GROUP_ADD_URI = "/group/add";
    private static final String GROUP_UPDATE_URI = "/group/update";
    private static final String GROUP_DELETE_URI = "/group/delete";
    private static final String GROUP_INFO_URI = "/group/info";
    private static final String GROUP_LIST_VIEW = "group/list";
    private static final String GROUP_ADD_VIEW = "group/add";
    private static final String GROUP_UPDATE_VIEW = "group/update";
    private static final String GROUP_INFO_VIEW = "group/info";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @DisplayName("When controller is called, it should return correct view")
    @Test
    void testAddGroupView_shouldViewGroupAddTemplate() throws Exception {
        // expect:
        mockMvc.perform(get(GROUP_ADD_URI))
            .andExpectAll(
                status().is2xxSuccessful(),
                view().name(GROUP_ADD_VIEW),
                content().string(containsString("Add Group"))
            );
    }

    @DisplayName("When a valid group is submitted, it should be passed to the service")
    @Test
    void testAddGroup_validGroup_shouldPassToService() throws Exception {
        // given:
        var group = new Group();
        group.setName("Test Group");

        // when:
        mockMvc.perform(post(GROUP_ADD_URI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", group.getName()))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(REDIRECT + GROUP_INFO_URI + "/" + groupService.add(group)))
            .andReturn();

        // then:
        verify(groupService, times(1)).add(group);
    }

    @DisplayName("When an invalid group is submitted, it should view group/add page")
    @Test
    void testAddGroup_invalidGroup_shouldRedirectToAddGroup() throws Exception {
        // given:
        var group = new Group();
        group.setName("");

        // expect:
        mockMvc.perform(post(GROUP_ADD_URI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", group.getName()))
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name(GROUP_ADD_VIEW))
            .andReturn();
    }


    @DisplayName("When controller is called, it should return correct view")
    @Test
    void testUpdateGroupView() throws Exception {
        // given:
        var group = new Group(1L, "Test Group");

        // when:
        when(groupService.getById(group.getId())).thenReturn(group);

        // then:
        mockMvc.perform(get(GROUP_UPDATE_URI + "/{id}", group.getId()))
            .andExpectAll(
                status().is2xxSuccessful(),
                view().name(GROUP_UPDATE_VIEW),
                content().string(containsString("Update Group"))
            );
    }

    @DisplayName("When a valid group is submitted, it should be passed to the service")
    @Test
    void testUpdateGroup_validGroup_shouldPassToService() throws Exception {
        // given:
        var id = 1L;
        var group = new Group();
        group.setId(id);
        group.setName("Test Group");

        // when:
        mockMvc.perform(post(GROUP_UPDATE_URI + "/{id}", id)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", group.getName()))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(REDIRECT + GROUP_INFO_URI + "/" + id))
            .andReturn();

        // then:
        verify(groupService, times(1)).update(id, group);
    }

    @DisplayName("When an invalid group is submitted, it should view group/update page")
    @Test
    void testUpdateGroup_invalidGroup_redirectToUpdateGroup() throws Exception {
        // given:
        var id = 1L;
        var group = new Group();
        group.setId(id);
        group.setName("");

        // expect:
        mockMvc.perform(post(GROUP_UPDATE_URI + "/{id}", id)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", group.getName()))
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name(GROUP_UPDATE_VIEW))
            .andReturn();
    }

    @DisplayName("When controller is called, it should return correct view")
    @Test
    void testInfoGroupView() throws Exception {
        // given:
        var group = new Group(1L, "Test Group");

        // when:
        when(groupService.getById(group.getId())).thenReturn(group);

        // then:
        mockMvc.perform(get(GROUP_INFO_URI + "/{id}", group.getId()))
            .andExpectAll(
                status().is2xxSuccessful(),
                view().name(GROUP_INFO_VIEW),
                content().string(containsString("Group Info")),
                content().string(containsString(group.getId().toString())),
                content().string(containsString(group.getName()))
            );
    }

    @DisplayName("When controller is called, it should return correct view")
    @Test
    void testGetAllGroupView() throws Exception {
        // given:
        var group = new Group(1L, "Test Group");

        // when:
        when(groupService.getAll()).thenReturn(List.of(group));

        // then:
        mockMvc.perform(get(GROUP_HOME_URI))
            .andExpectAll(
                status().is2xxSuccessful(),
                view().name(GROUP_LIST_VIEW),
                content().string(containsString("Groups")),
                content().string(containsString(group.getId().toString())),
                content().string(containsString(group.getName()))
            );
    }

    @DisplayName("When a valid id is submitted, it should be passed to the service")
    @Test
    void testRemoveGroup() throws Exception {
        // given:
        var id = 1L;

        // when:
        mockMvc.perform(get(GROUP_DELETE_URI + "/{id}", id)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(REDIRECT + GROUP_HOME_URI))
            .andReturn();

        // then:
        verify(groupService, times(1)).deleteById(id);
    }

}