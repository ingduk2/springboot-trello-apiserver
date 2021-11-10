package com.api.trello.web.member.controller;

import com.api.trello.web.member.dto.MemberRequestDto;
import com.api.trello.web.member.dto.MemberResponseDto;
import com.api.trello.web.member.dto.MemberSignInRequestDto;
import com.api.trello.web.member.exception.CEmailExistException;
import com.api.trello.web.member.exception.CEmailNotFoundException;
import com.api.trello.web.member.exception.CPasswordNotMatchException;
import com.api.trello.web.member.service.SignService;
import com.api.trello.web.util.ControllerTestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SignController.class)
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

    private MemberRequestDto getMemberRequestDto(String name, String email, String password) {
        return MemberRequestDto.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
    }

    private MemberResponseDto getMemberResponseDto(Long id, String name, String email, String password) {
        return MemberResponseDto.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .build();
    }

    private MemberSignInRequestDto getMemberSignInRequestDto(String email, String password) {
        return MemberSignInRequestDto.builder()
                .email(email)
                .password(password)
                .build();
    }


    @Test
    void signup_가입_테스트_email형식안맞음() throws Exception {
        //given
        String name = "name";
        String email = "email.com";
        String password = "12345678";

        MemberRequestDto memberRequestDto = getMemberRequestDto(name, email, password);

        //when
        final ResultActions actions = mvc.perform(
                post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(getJsonStringByDto(memberRequestDto)));

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("email"));
    }

    @Test
    void signup_가입_테스트_비밀번호최소8자리안됨() throws Exception {
        //given
        String name = "name";
        String email = "email@email.com";
        String password = "12345";

        MemberRequestDto memberRequestDto = getMemberRequestDto(name, email, password);

        //when
        final ResultActions actions = mvc.perform(
                post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(getJsonStringByDto(memberRequestDto)));

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("password"));
    }

    @Test
    void signup_가입_테스트_전부빈값테스트() throws Exception {
        //given
        MemberRequestDto member = MemberRequestDto.builder().build();

        //when
        final ResultActions actions = mvc.perform(
                post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(getJsonStringByDto(member)));

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field").value(hasItems("name", "email", "password")));
    }

    @Test
    void signup_가입_테스트_성공() throws Exception {
        String name = "ingduk2";
        String email = "ingduk2@gmail.com";
        String password = "123456789";
        MemberRequestDto requestDto = getMemberRequestDto(name, email, password);

        MemberResponseDto responseDto = getMemberResponseDto(1L, name, email, password);

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
        MemberSignInRequestDto requestDto = MemberSignInRequestDto.builder().build();

        //when
        final ResultActions actions = mvc.perform(
                post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(getJsonStringByDto(requestDto)));

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field").value(hasItems("email", "password")));
    }

    @Test
    void signin_로그인_테스트_이메일과비밀번호_성공() throws Exception {
        String email = "ingduk2@gmail.com";
        String password = "123456789";
        MemberSignInRequestDto requestDto = getMemberSignInRequestDto(email, password);

        Long id = 1L;
        String name = "ingduk2";
        MemberResponseDto responseDto = getMemberResponseDto(id, name, email, password);

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

    @Test
    void signin_로그인_테스트_EmailNotFoundException() throws Exception {
        String email = "ingduk2@gmail.com";
        String password = "123456789";
        MemberSignInRequestDto requestDto = getMemberSignInRequestDto(email, password);

        Long id = 1L;
        String name = "ingduk2";
        MemberResponseDto responseDto = getMemberResponseDto(id, name, email, password);

        //given
        given(signService.signIn(requestDto))
                .willThrow(new CEmailNotFoundException());

        //when
        final ResultActions actions = mvc.perform(
                post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(getJsonStringByDto(requestDto)));

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(
                        ControllerTestUtil.getApiResultExceptionClass(result), CEmailNotFoundException.class));
    }

    @Test
    void signUp_CEmailExistException() throws Exception {
        //given
        String name = "ingduk2";
        String email = "ingduk2@gmail.com";
        String password = "123456789";
        MemberRequestDto requestDto = getMemberRequestDto(name, email, password);

        given(signService.signUp(any(MemberRequestDto.class)))
                .willThrow(new CEmailExistException());

        //then
        final ResultActions actions = mvc.perform(
                post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(getJsonStringByDto(requestDto)));

        //when
        actions
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertThat(ControllerTestUtil.getApiResultExceptionClass(result))
                                .isEqualTo(CEmailExistException.class));
    }

    @Test
    void signIn_CPasswordNotMatchException() throws Exception {
        //given
        String email = "ingduk2@gmail.com";
        String password = "123456789";
        MemberSignInRequestDto requestDto = getMemberSignInRequestDto(email, password);

        given(signService.signIn(requestDto))
                .willThrow(new CPasswordNotMatchException());

        //when
        final ResultActions actions = mvc.perform(
                post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(getJsonStringByDto(requestDto)));

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertEquals(ControllerTestUtil.getApiResultExceptionClass(result) , CPasswordNotMatchException.class));
    }

}