package com.api.trello.web.workspace.dto;

import com.api.trello.web.workspace.domain.Workspace;
import lombok.Builder;
import lombok.Getter;

@Getter
public class WorkspaceResponseDto {

    private Long workspaceId;
    private String name;
    private String shortName;
    private String description;

    private Long memberId;
    private String email;

    @Builder
    public WorkspaceResponseDto(Long workspaceId, String name, String shortName, String description, Long memberId, String email) {
        this.workspaceId = workspaceId;
        this.name = name;
        this.shortName = shortName;
        this.description = description;
        this.memberId = memberId;
        this.email = email;
    }

    public static WorkspaceResponseDto of(Workspace workspace) {
        return WorkspaceResponseDto.builder()
                .workspaceId(workspace.getId())
                .name(workspace.getName())
                .shortName(workspace.getShortName())
                .description(workspace.getDescription())
                .memberId(workspace.getMember().getId())
                .email(workspace.getMember().getEmail())
                .build();
    }
}
