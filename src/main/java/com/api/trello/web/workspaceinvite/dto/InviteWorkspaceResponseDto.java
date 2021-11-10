package com.api.trello.web.workspaceinvite.dto;

import com.api.trello.web.workspaceinvite.domain.InviteWorkspace;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InviteWorkspaceResponseDto {

    private Long workspaceId;
    private String name;

    private Long memberId;
    private String email;

    @Builder
    public InviteWorkspaceResponseDto(Long workspaceId, String name, Long memberId, String email) {
        this.workspaceId = workspaceId;
        this.name = name;
        this.memberId = memberId;
        this.email = email;
    }

    public static InviteWorkspaceResponseDto of(InviteWorkspace save) {
        return InviteWorkspaceResponseDto.builder()
                .workspaceId(save.getWorkspace().getId())
                .name(save.getWorkspace().getName())
                .memberId(save.getMember().getId())
                .email(save.getMember().getEmail())
                .build();
    }
}
