package com.api.trello.web.member.dto;

import com.api.trello.web.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MemberRequestDto {

    @NotBlank(message = "name은 필수 값입니다.")
    private String name;

    @Email(message = "email형식이 아닙니다.")
    @NotBlank(message = "email은 필수 값입니다.")
    private String email;

    @Builder
    public MemberRequestDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .email(email)
                .build();
    }
}
