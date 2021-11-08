package com.api.trello.web.member.service;

import com.api.trello.util.SecurityUtil;
import com.api.trello.web.member.domain.Member;
import com.api.trello.web.member.domain.MemberRepository;
import com.api.trello.web.member.dto.MemberResponseDto;
import com.api.trello.web.member.exception.CEmailNotFoundException;
import com.api.trello.web.member.exception.CMemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findMemberResponseDtoById(Long memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(CMemberNotFoundException::new);

        return MemberResponseDto.of(member);
    }

    @Transactional
    public MemberResponseDto update(Long memberId, String name) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(CMemberNotFoundException::new);

        member.update(name);
        return MemberResponseDto.of(member);
    }

    @Transactional(readOnly = true)
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(CMemberNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(CMemberNotFoundException::new);
    }

    public Member findCurrentMember() {
        String userEmail = SecurityUtil.getCurrentUsername().orElseThrow(CEmailNotFoundException::new);
        return memberRepository.findByEmail(userEmail).orElseThrow(CEmailNotFoundException::new);
    }
}
