package com.api.trello.web.member.controller;

import com.api.trello.web.member.domain.Member;
import com.api.trello.web.member.dto.MemberRequestDto;
import com.api.trello.web.member.dto.MemberResponseDto;
import com.api.trello.web.member.dto.MemberSignInRequestDto;
import com.api.trello.web.member.service.SignService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SignController.class)
@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
class SignControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    MockMvc mvc;

    @MockBean
    SignService signService;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    private String getJsonStringByDto(Object dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    @Test
    void signup_가입_테스트_email형식안맞음() throws Exception {
        //given
        String request = getJsonStringByDto(MemberRequestDto.builder()
                .name("name")
                .email("email.com")
                .password("12345678")
                .build());

        //when
        final ResultActions actions = mvc.perform(
                post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(request));

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("email"));
    }

    @Test
    void signup_가입_테스트_비밀번호최소8자리안됨() throws Exception {
        //given
        String request = getJsonStringByDto(MemberRequestDto.builder()
                .name("name")
                .email("email@email.com")
                .password("12345")
                .build());

        //when
        final ResultActions actions = mvc.perform(
                post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(request));

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("password"));
    }

    @Test
    void signup_가입_테스트_전부빈값테스트() throws Exception {
        //given
        String request = getJsonStringByDto(MemberRequestDto.builder().build());

        //when
        final ResultActions actions = mvc.perform(
                post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(request));

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field").value(hasItems("name", "email", "password")));
    }

    @Test
    void signup_가입_테스트_성공() throws Exception {
        MemberRequestDto requestDto = MemberRequestDto.builder()
                .name("ingduk2")
                .email("ingduk2@gmail.com")
                .password("123456789")
                .build();

        MemberResponseDto responseDto = MemberResponseDto.of(Member.builder()
                .id(1L)
                .name("ingduk2")
                .email("ingduk2@gmail.com")
                .password("123456789")
                .build());

        //given
        //any(), requestDto에 hashcode 할 경우 willreturn 됨.
        given(signService.signUp(any(MemberRequestDto.class)))
                .willReturn(responseDto);

        //when
        final ResultActions actions = mvc.perform(
                post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(getJsonStringByDto(requestDto)));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("ingduk2"))
                .andExpect(jsonPath("$.data.email").value("ingduk2@gmail.com"));
    }

    @Test
    void signin_로그인_테스트_이메일과비밀번호_빈값() throws Exception{
        //given
        String request = getJsonStringByDto(MemberSignInRequestDto.builder().build());

        //when
        final ResultActions actions = mvc.perform(
                post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(request));

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field").value(hasItems("email", "password")));
    }

    @Test
    void signin_로그인_테스트_이메일과비밀번호_성공() throws Exception {
        MemberSignInRequestDto requestDto = MemberSignInRequestDto.builder()
                .email("ingduk2@gmail.com")
                .password("123456789")
                .build();

        MemberResponseDto responseDto = MemberResponseDto.of(Member.builder()
                .id(1L)
                .name("ingduk2")
                .email("ingduk2@gmail.com")
                        .password("123456789")
                .build());

        //given
        given(signService.signIn(requestDto)).willReturn(responseDto);

        //when
        final ResultActions actions = mvc.perform(
                post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(getJsonStringByDto(requestDto)));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.email").value("ingduk2@gmail.com"))
                .andExpect(jsonPath("$.data.name").value("ingduk2"));
    }

}