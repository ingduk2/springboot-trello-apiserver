package com.api.trello.web.member.controller;

import com.api.trello.web.common.dto.resopnse.SuccessResponse;
import com.api.trello.web.member.dto.MemberRequestDto;
import com.api.trello.web.member.dto.MemberResponseDto;
import com.api.trello.web.member.dto.MemberSignInRequestDto;
import com.api.trello.web.member.service.SignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SignController {

    private final SignService signService;

    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse> signUp(@RequestBody @Valid MemberRequestDto requestDto) {
        MemberResponseDto responseDto = signService.signUp(requestDto);
        return ResponseEntity.ok().body(SuccessResponse.success(responseDto));
    }

    @PostMapping("/signin")
    public ResponseEntity<SuccessResponse> signIn(@RequestBody @Valid MemberSignInRequestDto requestDto) {
        return ResponseEntity.ok(SuccessResponse.success(signService.signIn(requestDto)));
    }

}
