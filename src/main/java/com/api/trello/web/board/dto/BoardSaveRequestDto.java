package com.api.trello.web.board.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardSaveRequestDto {

    @NotNull
    private Long workspaceId;

    @NotEmpty
    private String title;

    @Builder
    public BoardSaveRequestDto(Long workspaceId, String title) {
        this.workspaceId = workspaceId;
        this.title = title;
    }
}
