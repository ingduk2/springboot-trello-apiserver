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

    @GetMapping("/members/{memberId}")
    public ResponseEntity<SuccessResponse> findMemberById(@PathVariable long memberId) {
        MemberResponseDto responseDto = memberService.findMemberResponseDtoById(memberId);
        return ResponseEntity.ok().body(SuccessResponse.success(responseDto));
    }

    @GetMapping("/members")
    public ResponseEntity<SuccessResponse> findAllMember() {
        return ResponseEntity.ok().body(SuccessResponse.success(memberService.findAll()));
    }

    @PutMapping("/members/{memberId}")
    public ResponseEntity<SuccessResponse> modifyMember(@PathVariable Long memberId, @RequestParam String name) {
        MemberResponseDto responseDto = memberService.update(memberId, name);
        return ResponseEntity.ok().body(SuccessResponse.success(responseDto));
    }

}
