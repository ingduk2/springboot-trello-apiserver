package com.api.trello.web.member.dto;

import com.api.trello.web.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponseDto {

    private Long id;
    private String name;
    private String email;

    @Builder
    public MemberResponseDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static MemberResponseDto of(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }
}
