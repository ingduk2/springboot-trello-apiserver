package com.api.trello.web.member.controller;

import com.api.trello.web.common.dto.resopnse.SuccessResponse;
import com.api.trello.web.member.dto.MemberResponseDto;
import com.api.trello.web.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/{memberId}")
    public ResponseEntity<SuccessResponse> findMemberById(@PathVariable long memberId) {
        MemberResponseDto responseDto = memberService.findById(memberId);
        return ResponseEntity.ok().body(SuccessResponse.of(responseDto));
    }

    @GetMapping("/members")
    public ResponseEntity<SuccessResponse> findAllMember() {
        return ResponseEntity.ok().body(SuccessResponse.of(memberService.findAll()));
    }

    @PutMapping("/member")
    public ResponseEntity<SuccessResponse> modifyMember(Long memberId, @RequestParam String name) {
        MemberResponseDto responseDto = memberService.update(memberId, name);
        return ResponseEntity.ok().body(SuccessResponse.of(responseDto));
    }

}
