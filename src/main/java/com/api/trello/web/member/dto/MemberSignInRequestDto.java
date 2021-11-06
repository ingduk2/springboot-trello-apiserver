package com.api.trello.web.member.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSignInRequestDto {

    @Email(message = "email형식이 아닙니다.")
    @NotBlank(message = "email은 필수 값입니다.")
    private String email;

    @Size(min = 8, message = "비밀번호는 8자 이상입니다.")
    @NotBlank(message = "password는 필수 값입니다.")
    private String password;

    @Builder
    public MemberSignInRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
