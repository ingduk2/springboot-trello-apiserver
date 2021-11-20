package com.api.trello.web.list.dto;

import com.api.trello.web.list.domain.Lists;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ListsResponseDto {

    private Long listId;
    private Long boardId;
    private String listTitle;
    private Long index;

    @Builder
    public ListsResponseDto(Long listId, Long boardId, String listTitle, Long index) {
        this.listId = listId;
        this.boardId = boardId;
        this.listTitle = listTitle;
        this.index = index;
    }

    public static ListsResponseDto of(Lists lists) {
        return ListsResponseDto.builder()
                .listId(lists.getId())
                .boardId(lists.getBoard().getId())
                .listTitle(lists.getTitle())
                .index(lists.getIdx())
                .build();
    }
}
