package com.api.trello.web.workspace.service;

import com.api.trello.web.member.domain.Member;
import com.api.trello.web.member.domain.MemberRepository;
import com.api.trello.web.workspace.domain.Workspace;
import com.api.trello.web.workspace.domain.WorkspaceRepository;
import com.api.trello.web.workspace.dto.WorkspaceResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class WorkspaceServiceTest {

    @Autowired
    WorkspaceService workspaceService;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        Member member = memberRepository.save(Member.builder()
                .name("ingduk2")
                .email("ingduk2@gmail.com")
                .password("qwer1234")
                .build());


        for (int i = 0; i < 5; i++) {
            Workspace work = Workspace.builder()
                    .name("work" + i)
                    .build();
            member.addWorkspace(work);

            workspaceRepository.save(work);
        }

        member = memberRepository.save(Member.builder()
                .name("aaa")
                .email("aaa@gmail.com")
                .password("qwer1234")
                .build());


        for (int i = 0; i < 5; i++) {
            Workspace work = Workspace.builder()
                    .name("aaa" + i)
                    .build();
            member.addWorkspace(work);

            workspaceRepository.save(work);
        }
    }

    @Test
    void test_n_플러스_1() {
        System.out.println("====test start====");
        List<WorkspaceResponseDto> all = workspaceService.findAllWorkspace();
//        List<Workspace> all = workspaceRepository.findAll();
        Assertions.assertEquals(all.size(), 5);
    }

}