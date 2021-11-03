package com.api.trello.web.member.service;

import com.api.trello.web.member.domain.Member;
import com.api.trello.web.member.domain.MemberRepository;
import com.api.trello.web.member.dto.MemberRequestDto;
import com.api.trello.web.member.dto.MemberResponseDto;
import com.api.trello.web.member.exception.CUserNotFoundException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long save(MemberRequestDto requestDto) {
        return memberRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberResponseDto::of)
                .collect(Collectors.toList());
    }

    public MemberResponseDto findById(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(CUserNotFoundException::new);
        return MemberResponseDto.of(member);
    }

}
