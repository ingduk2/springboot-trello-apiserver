package com.api.trello.web.member.controller;

import com.api.trello.web.board.exception.CBoardNotFoundException;
import com.api.trello.web.common.dto.resopnse.SuccessResponse;
import com.api.trello.web.member.dto.MemberRequestDto;
import com.api.trello.web.member.dto.MemberResponseDto;
import com.api.trello.web.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/v1/member")
    public ResponseEntity<SuccessResponse> saveMember(@RequestBody @Valid MemberRequestDto requestDto) {
        Long save = memberService.save(requestDto);
        return ResponseEntity.ok().body(SuccessResponse.success());
    }

    @GetMapping("/v1/member/{memberId}")
    public ResponseEntity<SuccessResponse> findMemberById(@PathVariable long memberId) {
        MemberResponseDto responseDto = memberService.findById(memberId);
        return ResponseEntity.ok().body(SuccessResponse.of(responseDto));
    }

    @GetMapping("/v1/members")
    public ResponseEntity<SuccessResponse> findAllMember() {
        return ResponseEntity.ok().body(SuccessResponse.of(memberService.findAll()));
    }

    @GetMapping("/v1/exception")
    public void exception() throws Exception {
        throw new Exception();
    }

    @GetMapping("/v1/custom")
    public void custom() {
        throw new CBoardNotFoundException();
    }
}
