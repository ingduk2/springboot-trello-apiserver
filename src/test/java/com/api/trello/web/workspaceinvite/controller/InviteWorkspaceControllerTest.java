package com.api.trello.web.workspaceinvite.controller;

import com.api.trello.web.util.ControllerTestUtil;
import com.api.trello.web.workspaceinvite.dto.InviteWorkspaceResponseDto;
import com.api.trello.web.workspaceinvite.dto.InviteWorkspaceSaveRequestDto;
import com.api.trello.web.workspaceinvite.exception.CInvitedWorkspaceAlreadyInviteException;
import com.api.trello.web.workspaceinvite.exception.CInvitedWorkspaceMemberIsWorkspaceOwnerException;
import com.api.trello.web.workspaceinvite.service.InviteWorkspaceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InviteWorkspaceController.class)
class InviteWorkspaceControllerTest {

    private static String INVITE_WORKSPACE_URL = "/workspaces/invite";

    @Autowired
    MockMvc mvc;

    @MockBean
    InviteWorkspaceService inviteWorkspaceService;

    @Autowired
    ObjectMapper objectMapper;

    private String getJsonStringByDto(Object dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    @Test
    void POST_inviteWorkspace_성공() throws Exception {
        //given
        Long workspaceId = 1L;
        Long invitedMemberId = 3L;
        InviteWorkspaceSaveRequestDto requestDto = InviteWorkspaceSaveRequestDto.builder()
                .workspaceId(workspaceId)
                .invitedMemberId(invitedMemberId)
                .build();

        given(inviteWorkspaceService.invite(workspaceId, invitedMemberId))
                .willReturn(InviteWorkspaceResponseDto.builder()
                        .workspaceId(workspaceId)
                        .name("workspace")
                        .memberId(invitedMemberId)
                        .email("ingduk2@gmail.com")
                        .build());

        //when
        final ResultActions actions = mvc.perform(
                post(INVITE_WORKSPACE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(getJsonStringByDto(requestDto)))
                .andDo(print());

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.workspaceId").value(workspaceId))
                .andExpect(jsonPath("$.data.memberId").value(invitedMemberId));
    }

    @Test
    void POST_inviteWorkspace_초대할_member와_workspace의_member가_같으면_초대못함() throws Exception {
        //given
        Long workspaceId = 1L;
        Long invitedMemberId = 1L;
        InviteWorkspaceSaveRequestDto requestDto = InviteWorkspaceSaveRequestDto.builder()
                .workspaceId(workspaceId)
                .invitedMemberId(invitedMemberId)
                .build();

        given(inviteWorkspaceService.invite(workspaceId, invitedMemberId))
                .willThrow(new CInvitedWorkspaceMemberIsWorkspaceOwnerException());

        //when
        final ResultActions actions = mvc.perform(
                post(INVITE_WORKSPACE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(getJsonStringByDto(requestDto)))
                .andDo(print());

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertThat(ControllerTestUtil.getApiResultExceptionClass(result))
                                .isEqualTo(CInvitedWorkspaceMemberIsWorkspaceOwnerException.class));
    }

    @Test
    void POST_inviteWorkspace_이미_초대했으면_초대할_수_없다() throws Exception {
        //given
        Long workspaceId = 1L;
        Long invitedMemberId = 1L;
        InviteWorkspaceSaveRequestDto requestDto = InviteWorkspaceSaveRequestDto.builder()
                .workspaceId(workspaceId)
                .invitedMemberId(invitedMemberId)
                .build();

        given(inviteWorkspaceService.invite(workspaceId, invitedMemberId))
                .willThrow(new CInvitedWorkspaceAlreadyInviteException());

        //when
        final ResultActions actions = mvc.perform(
                post(INVITE_WORKSPACE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(getJsonStringByDto(requestDto)))
                .andDo(print());

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertThat(ControllerTestUtil.getApiResultExceptionClass(result))
                                .isEqualTo(CInvitedWorkspaceAlreadyInviteException.class));
    }



}