package com.api.trello.web.list.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ListsUpdateRequestDto {

    private String listTitle;
}
