package com.api.trello.web.workspaceinvite.controller;

import com.api.trello.web.common.dto.resopnse.SuccessResponse;
import com.api.trello.web.workspaceinvite.dto.InviteWorkspaceResponseDto;
import com.api.trello.web.workspaceinvite.dto.InviteWorkspaceSaveRequestDto;
import com.api.trello.web.workspaceinvite.service.InviteWorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class InviteWorkspaceController {

    private final InviteWorkspaceService inviteWorkspaceService;

    @PostMapping("workspaces/invite")
    public ResponseEntity<SuccessResponse> inviteWorkspace(@RequestBody @Valid
                                                           InviteWorkspaceSaveRequestDto requestDto) {
        InviteWorkspaceResponseDto responseDto = inviteWorkspaceService.invite(requestDto.getWorkspaceId(), requestDto.getInvitedMemberId());
        return ResponseEntity.ok(SuccessResponse.success(responseDto));
    }
}
