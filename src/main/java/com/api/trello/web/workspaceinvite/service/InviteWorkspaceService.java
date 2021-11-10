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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InviteWorkspaceService {

    private final InviteWorkspaceRepository inviteWorkspaceRepository;
    private final MemberService memberService;
    private final WorkspaceService workspaceService;

    @Transactional
    public InviteWorkspaceResponseDto invite(Long workspaceId, Long invitedMemberId) {
        Member member = memberService.findById(invitedMemberId);
        //EAGER 일 경우 join
        Workspace workspace = workspaceService.findById(workspaceId);

        //초대할 member와 초대할 workspace의 member 가 같으면 초대할 필요가 없다.
        if (member.getId().equals(workspace.getMember().getId())) {
            throw new CInvitedWorkspaceMemberIsWorkspaceOwnerException();
        }

        //이미 초대했으면 초대할 수 없다.
        if (inviteWorkspaceRepository.countByMemberAndWorkspace(member, workspace) > 0) {
            throw new CInvitedWorkspaceAlreadyInviteException();
        }

        InviteWorkspace save = inviteWorkspaceRepository.save(InviteWorkspace.builder()
                .member(member)
                .workspace(workspace)
                .build());

        //후에 invitedMember 에게 초대되었다는거 알릴 수 있게 push?

        return InviteWorkspaceResponseDto.of(save);
    }

    //WorkspaceResponseDto 인데 여기있는게 안맞는듯.
    //아래 방법은 일단 쿼리가 많이나감. n + 1
    //member밖에 아는게 없기때문에.. workspace도 찾고 원래 주인 member도 또 찾음
//    @Transactional(readOnly = true)
//    public List<WorkspaceResponseDto> findAllByMy() {
//        Member member = memberService.findCurrentMember();
//        return inviteWorkspaceRepository.findAllByMember(member).stream()
//                .map(e -> WorkspaceResponseDto.of(e.getWorkspace()))
//                .collect(Collectors.toList());
//    }

}
