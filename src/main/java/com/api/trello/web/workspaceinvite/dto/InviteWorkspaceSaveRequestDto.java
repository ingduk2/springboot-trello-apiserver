package com.api.trello.web.workspaceinvite.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InviteWorkspaceSaveRequestDto {

    @NotNull(message = "초대할 workspaceId 는 필수입니다.")
    private Long workspaceId;

    @NotNull(message = " 초대받을 memberId 는 필수입니다.")
    private Long invitedMemberId;

    @Builder
    public InviteWorkspaceSaveRequestDto(Long workspaceId, Long invitedMemberId) {
        this.workspaceId = workspaceId;
        this.invitedMemberId = invitedMemberId;
    }
}
