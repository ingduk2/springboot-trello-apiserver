package com.api.trello.web.member.controller;

import com.api.trello.web.common.dto.ListResult;
import com.api.trello.web.common.dto.ResponseResult;
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
    private final ResponseResult responseResult;

    @PostMapping("/v1/member")
    public ResponseEntity<SuccessResponse> saveMember(@RequestBody @Valid MemberRequestDto requestDto) {
        log.info("{} {}", requestDto.getEmail(), requestDto.getName());

        Long save = memberService.save(requestDto);
        return ResponseEntity.ok().body(SuccessResponse.success());
    }

    @GetMapping("/v1/member/{memberId}")
    public ResponseEntity<SuccessResponse> findMemberById(@PathVariable long memberId) {
        MemberResponseDto responseDto = memberService.findById(memberId);
        return ResponseEntity.ok().body(SuccessResponse.of(responseDto));
    }

    @GetMapping("/v1/members")
    public ListResult<MemberResponseDto> findAllMember() {
        return responseResult.getListResult(memberService.findAll());
    }

    @GetMapping("/v2/members")
    public ResponseEntity<SuccessResponse> findAllMember2() {
        return ResponseEntity.ok().body(SuccessResponse.of(memberService.findAll()));
    }

    @GetMapping("/v1/exception")
    public void exception() throws Exception {
        throw new Exception();
    }
}
