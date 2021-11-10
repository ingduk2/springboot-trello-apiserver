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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SignServiceTest {

//    @InjectMocks
//    SignService signService;
//    @Mock
//    MemberRepository memberRepository;
//    @Mock
//    private BCryptPasswordEncoder passwordEncoder;

    SignService signService;

    @Mock
    MemberRepository memberRepository;

    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.signService = new SignService(memberRepository, passwordEncoder);
    }

    @Test()
    void signUp_이메일_이미_등록되어있을경우_CEmailExistException() {
        String email = "ingduk2@gmail.com";

        MemberRequestDto requestDto = MemberRequestDto.builder()
                .name("ingduk2")
                .email(email)
                .password("123456789")
                .build();

        //given
        given(memberRepository.countByEmail(email)).willReturn(1);

        //when
        //then
        assertThrows(CEmailExistException.class, () -> signService.signUp(requestDto));
    }

    @Test()
    void signUp_테스트_성공() {
        String name = "ingduk2";
        String email = "ingduk2@gmail.com";
        String password = "123456789";

        MemberRequestDto requestDto = MemberRequestDto.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();

        //given
        given(memberRepository.countByEmail(email)).willReturn(0);
        given(memberRepository.save(any(Member.class)))
                .willReturn(Member.builder()
                        .id(1L)
                        .name(name)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .build());

        //when
        MemberResponseDto responseDto = signService.signUp(requestDto);

        //then
        assertEquals(1L, responseDto.getId());
        assertEquals(name, responseDto.getName());
        assertEquals(email, responseDto.getEmail());
        assertTrue(passwordEncoder.matches(password, responseDto.getPassword()));
        assertTrue(PasswordUtil.equalPassword(password, responseDto.getPassword()));
    }

    @Test
    void signIn_email없는경우_CEmailNotFoundException() {
        //given
        String email = "ingduk2@gmail.com";
        MemberSignInRequestDto requestDto = MemberSignInRequestDto.builder()
                .email(email)
                .password("123456789")
                .build();

        given(memberRepository.findByEmail(email)).willReturn(Optional.empty());

        //when
        //then
        assertThrows(CEmailNotFoundException.class, () -> signService.signIn(requestDto));
    }

    @Test
    void signIn_Password틀린경우_CPasswordNotMatchException() {
        //given
        String email = "ingduk2@gmail.com";
        MemberSignInRequestDto requestDto = MemberSignInRequestDto.builder()
                .email(email)
                .password("123456788")
                .build();

        given(memberRepository.findByEmail(email))
                .willReturn(Optional.of(Member.builder()
                        .id(1L)
                        .name("ingduk2")
                        .email(email)
                        .password(passwordEncoder.encode("123456789"))
                        .build()));

        //when
        //then
        assertThrows(CPasswordNotMatchException.class, () -> signService.signIn(requestDto));
    }

    @Test
    void SignIn_성공() {
        //given
        String email = "ingduk2@gmail.com";
        String password = "123456789";

        MemberSignInRequestDto requestDto = MemberSignInRequestDto.builder()
                .email(email)
                .password(password)
                .build();


        given(memberRepository.findByEmail(email))
                .willReturn(Optional.of(Member.builder()
                        .id(1L)
                        .name("ingduk2")
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .build()));

        //when
        MemberResponseDto responseDto = signService.signIn(requestDto);

        //then
        assertEquals(1L, responseDto.getId());
        assertEquals(email, responseDto.getEmail());
        assertTrue(PasswordUtil.equalPassword(password, responseDto.getPassword()));
    }

}