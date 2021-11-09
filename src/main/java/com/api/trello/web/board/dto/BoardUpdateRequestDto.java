package com.api.trello.web.board.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardUpdateRequestDto {

    private String title;

    @Builder
    public BoardUpdateRequestDto(String title) {
        this.title = title;
    }
}
