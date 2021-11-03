package com.api.trello;

import com.api.trello.web.member.domain.Member;
import com.api.trello.web.member.domain.MemberRepository;
import com.api.trello.web.workspace.domain.WorkSpace;
import com.api.trello.web.workspace.domain.WorkspaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
class TrelloApplicationTests {

	@Autowired
	MemberRepository memberRepository;
	@Autowired
	WorkspaceRepository workspaceRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void relationTest() {
		Member member = Member.builder()
				.email("member@email.com")
				.name("member1")
				.build();
		memberRepository.save(member);

		WorkSpace workSpace = WorkSpace.builder()
				.name("workspace1")
				.build();


//		workSpace.setMember(member);
		member.setWorkspace(workSpace);
		workspaceRepository.save(workSpace);

		System.out.println(workSpace.getMember().getEmail());
	}

}
