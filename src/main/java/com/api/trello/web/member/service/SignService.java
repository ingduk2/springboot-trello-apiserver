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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponseDto signUp(MemberRequestDto requestDto) {

        if (memberRepository.countByEmail(requestDto.getEmail()) > 0) {
            throw new CEmailExistException();
        }

        Member member = requestDto.toEntity(passwordEncoder);
        return MemberResponseDto.of(memberRepository.save(member));
    }

    @Transactional
    public MemberResponseDto signIn(MemberSignInRequestDto requestDto) {
        //email 존재하는지 확인.
        Member member = memberRepository
                .findByEmail(requestDto.getEmail())
                .orElseThrow(CEmailNotFoundException::new);

        //password 확인
        if (!PasswordUtil.equalPassword(requestDto.getPassword(), member.getPassword())) {
            throw new CPasswordNotMatchException();
        }

        return MemberResponseDto.of(member);
    }
}
