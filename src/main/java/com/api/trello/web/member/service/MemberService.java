package com.api.trello.web.member.service;

import com.api.trello.util.PasswordUtil;
import com.api.trello.web.member.domain.Member;
import com.api.trello.web.member.domain.MemberRepository;
import com.api.trello.web.member.dto.MemberRequestDto;
import com.api.trello.web.member.dto.MemberResponseDto;
import com.api.trello.web.member.dto.MemberSignInRequestDto;
import com.api.trello.web.member.exception.CEmailExistException;
import com.api.trello.web.member.exception.CEmailNotFoundException;
import com.api.trello.web.member.exception.CPasswordNotMatchException;
import com.api.trello.web.member.exception.CUserNotFoundException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Transactional
    public MemberResponseDto findById(Long memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(CUserNotFoundException::new);

        return MemberResponseDto.of(member);
    }

    @Transactional
    public MemberResponseDto update(Long memberId, String name) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(CUserNotFoundException::new);

        member.update(name);
        return MemberResponseDto.of(member);
    }

    @Transactional
    public void signIn(MemberSignInRequestDto requestDto) {
        //email 존재하는지 확인.
        Member member = memberRepository
                .findByEmail(requestDto.getEmail())
                .orElseThrow(CEmailNotFoundException::new);

        //password 확인
        if (!PasswordUtil.equalPassword(requestDto.getPassword(), member.getPassword())) {
            throw new CPasswordNotMatchException();
        }
    }
}
