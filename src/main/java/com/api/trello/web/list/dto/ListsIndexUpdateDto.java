package com.api.trello.web.list.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ListsIndexUpdateDto {

    @NotNull
    private Long fromListId;
    @NotNull
    private Long toListId;
}
