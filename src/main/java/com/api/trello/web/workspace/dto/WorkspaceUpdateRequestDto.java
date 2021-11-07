package com.api.trello.web.workspace.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkspaceUpdateRequestDto {

    @NotBlank(message = "name은 필수 값입니다.")
    private String name;

    private String shortName;

    private String description;

    @Builder
    public WorkspaceUpdateRequestDto(String name, String shortName, String description) {
        this.name = name;
        this.shortName = shortName;
        this.description = description;
    }
}
