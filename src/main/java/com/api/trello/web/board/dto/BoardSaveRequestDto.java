package com.api.trello.web.board.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardSaveRequestDto {

    @NotNull
    private Long workspaceId;

    @NotBlank(message = "board Title 은 필수 값입니다.")
    private String title;

    @Builder
    public BoardSaveRequestDto(Long workspaceId, String title) {
        this.workspaceId = workspaceId;
        this.title = title;
    }
}
