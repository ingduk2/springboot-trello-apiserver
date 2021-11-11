package com.api.trello.web.workspace.controller;

import com.api.trello.web.util.ControllerTestUtil;
import com.api.trello.web.workspace.dto.WorkspaceResponseDto;
import com.api.trello.web.workspace.dto.WorkspaceSaveRequestDto;
import com.api.trello.web.workspace.dto.WorkspaceUpdateRequestDto;
import com.api.trello.web.workspace.exception.CWorkspaceNotFoundException;
import com.api.trello.web.workspace.service.WorkspaceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkspaceController.class)
class WorkspaceControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    WorkspaceService workspaceService;

    @Autowired
    ObjectMapper objectMapper;

    private String getJsonStringByDto(Object dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    @Test
    void get_workspaces_findAllWorkspace_성공() throws Exception {
        //given
        List<WorkspaceResponseDto> myWorkspaces = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            myWorkspaces.add(WorkspaceResponseDto.builder()
                    .id((long) i)
                    .name("workspace" + i)
                    .build());
        }

        given(workspaceService.findAllWorkspace())
                .willReturn(myWorkspaces);

        //when
        final ResultActions actions = mvc.perform(
                        get("/workspaces"))
                .andDo(print());

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(10));
    }

    @Test
    void get_workspaces_id_findWorkspaceById_실패_CWorkspaceNotFoundException() throws Exception {
        //given
        Long workspaceId = 1L;
        given(workspaceService.findWorkspaceResponseDtoById(workspaceId))
                .willThrow(new CWorkspaceNotFoundException());

        //when
        final ResultActions actions = mvc.perform(
                        get("/workspaces/" + workspaceId))
                .andDo(print());

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(
                        ControllerTestUtil.getApiResultExceptionClass(result), CWorkspaceNotFoundException.class))
                .andExpect(result ->
                        assertThat(ControllerTestUtil.getApiResultExceptionClass(result))
                                .isEqualTo(CWorkspaceNotFoundException.class));
    }

    @Test
    void get_workspace_id_성공() throws Exception {
        //given
        Long workspaceId = 1L;

        given(workspaceService.findWorkspaceResponseDtoById(workspaceId))
                .willReturn(WorkspaceResponseDto.builder()
                        .id(workspaceId)
                        .name("workspace")
                        .email("ingduk2@gmail.com")
                        .memberId(10L)
                        .build());

        //when
        final ResultActions actions = mvc.perform(
                        get("/workspaces/" + workspaceId))
                .andDo(print());

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.memberId").value(10));
    }

    @Test
    void POST_workspaces_성공() throws Exception {
        //given
        String name = "workspace";
        String shortName = "ws";
        String description = "description";
        WorkspaceSaveRequestDto requestDto = WorkspaceSaveRequestDto.builder()
                .name("workspace")
                .shortName("ws")
                .description("description")
                .build();

        Long workspaceId = 1L;
        given(workspaceService.save(any(WorkspaceSaveRequestDto.class)))
                .willReturn(WorkspaceResponseDto.builder()
                        .id(workspaceId)
                        .name(name)
                        .shortName(shortName)
                        .description(description)
                        .email("ingduk2@gmail.com")
                        .memberId(10L)
                        .build());

        //when
        final ResultActions actions = mvc.perform(
                        post("/workspaces")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(getJsonStringByDto(requestDto)))
                .andDo(print());

        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("Location", "http://localhost/workspaces/" + workspaceId))
                .andExpect(jsonPath("$.message").value("success created"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.memberId").value(10L));
    }

    @Test
    void POST_workspaces_valid_name_빈값() throws Exception {
        //given
        WorkspaceSaveRequestDto requestDto = WorkspaceSaveRequestDto.builder().build();

        //when
        final ResultActions actions = mvc.perform(
                        post("/workspaces")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(getJsonStringByDto(requestDto)))
                .andDo(print());

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field").value(Matchers.hasItem("name")));
    }

    @Test
    void DELETE_workspaces_성공() throws Exception {
        //given
        Long workspaceId = 1L;
        given(workspaceService.delete(workspaceId))
                .willReturn(workspaceId);

        //when
        final ResultActions actions = mvc.perform(
                        delete("/workspaces/" + workspaceId))
                .andDo(print());

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success delete"))
                .andExpect(jsonPath("$.status").value(204));
    }

    @Test
    void DELETE_workspaces_CWorkspaceNotFoundException() throws Exception {
        //given
        Long workspaceId = 1L;
        given(workspaceService.delete(workspaceId))
                .willThrow(new CWorkspaceNotFoundException());

        //then
        final ResultActions actions = mvc.perform(
                        delete("/workspaces/" + workspaceId))
                .andDo(print());

        //when
        actions
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertThat(ControllerTestUtil.getApiResultExceptionClass(result))
                                .isEqualTo(CWorkspaceNotFoundException.class));
    }

    @Test
    void PUT_workspaces_성공() throws Exception {
        //given
        Long workspaceId = 1L;
        String workspaceUpdateName = "workspace_update";
        WorkspaceUpdateRequestDto requestDto = WorkspaceUpdateRequestDto.builder()
                .name(workspaceUpdateName)
                .build();

        //dto equalshashcode or eq, any 사용
        //안그러면 null 떨어짐
        given(workspaceService.update(eq(workspaceId), any(WorkspaceUpdateRequestDto.class)))
                .willReturn(WorkspaceResponseDto.builder()
                        .id(workspaceId)
                        .name(workspaceUpdateName)
                        .memberId(1L)
                        .email("ingduk2@gmail.com")
                        .build());

        //when
        final ResultActions actions = mvc.perform(
                        put("/workspaces/" + workspaceId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(getJsonStringByDto(requestDto)))
                .andDo(print());

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(workspaceId))
                .andExpect(jsonPath("$.data.name").value(workspaceUpdateName));
    }

    @Test
    void PUT_workspaces_name빈값_Exception() throws Exception {
        //given
        Long workspaceId = 1L;
        WorkspaceUpdateRequestDto requestDto = WorkspaceUpdateRequestDto.builder().build();

        //when
        final ResultActions actions = mvc.perform(
                        put("/workspaces/" + workspaceId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(getJsonStringByDto(requestDto)))
                .andDo(print());

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("name"));
    }

    @Test
    void PUT_workspaces_CWorkspaceNotFoundException() throws Exception {
        //given
        Long workspaceId = 1L;
        WorkspaceUpdateRequestDto requestDto = WorkspaceUpdateRequestDto.builder()
                .name("new_workspace")
                .build();

        given(workspaceService.update(eq(workspaceId), any(WorkspaceUpdateRequestDto.class)))
                .willThrow(new CWorkspaceNotFoundException());

        //when
        final ResultActions actions = mvc.perform(
                        put("/workspaces/" + workspaceId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(getJsonStringByDto(requestDto)))
                .andDo(print());

        //then
        actions
                .andExpect(status().is4xxClientError())
                .andExpect(result ->
                        assertEquals(ControllerTestUtil.getApiResultExceptionClass(result)
                                , CWorkspaceNotFoundException.class));
    }

}