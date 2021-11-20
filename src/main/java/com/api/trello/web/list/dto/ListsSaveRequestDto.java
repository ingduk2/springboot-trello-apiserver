package com.api.trello.web.list.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ListsSaveRequestDto {

    @NotNull
    private Long boardId;

    @NotBlank(message = "list Title 은 필수값 입니다.")
    private String listTitle;
}
