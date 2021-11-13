package com.api.trello.web.board.dto;

import com.api.trello.web.board.domain.Board;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardResponseDto {

    private Long boardId;

    private String title;

    @Builder
    public BoardResponseDto(Long boardId, String title) {
        this.boardId = boardId;
        this.title = title;
    }

    public static BoardResponseDto of(Board board) {
        return BoardResponseDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .build();
    }
}
