package com.api.trello.web.board.domain;

import com.api.trello.web.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByMember(Member member);
}
