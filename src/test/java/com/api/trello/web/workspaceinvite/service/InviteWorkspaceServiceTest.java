package com.api.trello.web.workspaceinvite.service;

import com.api.trello.web.member.domain.Member;
import com.api.trello.web.member.service.MemberService;
import com.api.trello.web.workspace.domain.Workspace;
import com.api.trello.web.workspace.service.WorkspaceService;
import com.api.trello.web.workspaceinvite.domain.InviteWorkspace;
import com.api.trello.web.workspaceinvite.domain.InviteWorkspaceRepository;
import com.api.trello.web.workspaceinvite.dto.InviteWorkspaceResponseDto;
import com.api.trello.web.workspaceinvite.exception.CInvitedWorkspaceAlreadyInviteException;
import com.api.trello.web.workspaceinvite.exception.CInvitedWorkspaceMemberIsWorkspaceOwnerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class InviteWorkspaceServiceTest {

    @InjectMocks
    InviteWorkspaceService inviteWorkspaceService;

    @Mock
    InviteWorkspaceRepository inviteWorkspaceRepository;

    @Mock
    MemberService memberService;

    @Mock
    WorkspaceService workspaceService;

    private Member getMember(Long id) {
        return Member.builder()
                .id(id)
                .name("ingduk2")
                .email("ingduk2@gmail.com")
                .password("qwer1234")
                .build();
    }

    private Workspace getWorkspace(Long workspaceId, Long memberId) {
        Workspace workspace = Workspace.builder()
                .member(getMember(memberId))
                .name("workspace" + workspaceId)
                .build();

        ReflectionTestUtils.setField(workspace, "id", workspaceId);
        return workspace;
    }

    @Test
    void invite_?????????_member???_?????????_workspace???_member_???_?????????_?????????_?????????_??????() {
        //given
        Long workspaceId = 1L;
        Long memberId = 1L;
        given(memberService.findById(memberId))
                .willReturn(getMember(memberId));
        given(workspaceService.findById(workspaceId))
                .willReturn(getWorkspace(workspaceId, memberId));
        //when
        //then
        assertThrows(CInvitedWorkspaceMemberIsWorkspaceOwnerException.class,
                () -> inviteWorkspaceService.invite(workspaceId, memberId));
    }

    @Test
    void invite_??????_???????????????_?????????_???_??????() {
        //given
        Long invitedMemberId = 2L;

        Long memberId = 1L;
        Long workspaceId = 1L;

        Member member = getMember(invitedMemberId);
        Workspace workspace = getWorkspace(workspaceId, memberId);

        given(memberService.findById(invitedMemberId))
                .willReturn(member);

        given(workspaceService.findById(workspaceId))
                .willReturn(workspace);

        given(inviteWorkspaceRepository.countByMemberAndWorkspace(any(Member.class), any(Workspace.class)))
                .willReturn(1);

        //when
        //then
        assertThrows(CInvitedWorkspaceAlreadyInviteException.class,
                () -> inviteWorkspaceService.invite(workspaceId, invitedMemberId));
    }

    @Test
    void invite_??????() {
        //given
        Long invitedMemberId = 2L;

        Long memberId = 1L;
        Long workspaceId = 1L;

        Member member = getMember(invitedMemberId);
        Workspace workspace = getWorkspace(workspaceId, memberId);

        given(memberService.findById(invitedMemberId))
                .willReturn(member);

        given(workspaceService.findById(workspaceId))
                .willReturn(workspace);

        given(inviteWorkspaceRepository.countByMemberAndWorkspace(any(Member.class), any(Workspace.class)))
                .willReturn(0);

        given(inviteWorkspaceRepository.save(any(InviteWorkspace.class)))
                .willReturn(InviteWorkspace.builder()
                        .workspace(workspace)
                        .member(member)
                        .build());

        //when
        //then
        InviteWorkspaceResponseDto responseDto = inviteWorkspaceService.invite(workspaceId, invitedMemberId);

        assertEquals(responseDto.getWorkspaceId(), 1);
        assertEquals(responseDto.getMemberId(), 2);
    }


}