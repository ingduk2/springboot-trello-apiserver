package com.api.trello.web.workspace.dto;

import com.api.trello.web.workspace.domain.Workspace;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkspaceSaveRequestDto {

    @NotBlank(message = "name은 필수 값입니다.")
    private String name;

    private String shortName;

    private String description;

    @Builder
    public WorkspaceSaveRequestDto(String name, String shortName, String description) {
        this.name = name;
        this.shortName = shortName;
        this.description = description;
    }

    public Workspace toEntity() {
        return Workspace.builder()
                .name(name)
                .shortName(shortName)
                .description(description)
                .build();
    }
}
