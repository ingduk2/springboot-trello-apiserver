package com.api.trello.web.member.dto;

import com.api.trello.web.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponseDto {

    private Long memberId;
    private String name;
    private String email;
    @JsonIgnore
    private String password;

    @Builder
    public MemberResponseDto(Long memberId, String name, String email, String password) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static MemberResponseDto of(Member member) {
        return MemberResponseDto.builder()
                .memberId(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .password(member.getPassword())
                .build();
    }
}
