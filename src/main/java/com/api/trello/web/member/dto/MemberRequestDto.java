package com.api.trello.web.member.dto;

import com.api.trello.web.member.domain.Member;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class MemberRequestDto {

    @NotBlank(message = "name은 필수 값입니다.")
    private String name;

    @Email(message = "email형식이 아닙니다.")
    @NotBlank(message = "email은 필수 값입니다.")
    private String email;

    @Size(min = 8, message = "비밀번호는 8자 이상입니다.")
    @NotBlank(message = "password는 필수 값입니다.")
    private String password;

    @Builder
    public MemberRequestDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
    }
}
