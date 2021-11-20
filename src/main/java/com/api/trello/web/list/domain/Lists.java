package com.api.trello.web.list.domain;

import com.api.trello.web.board.domain.Board;
import com.api.trello.web.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Lists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String title;

    private Long idx;

    @Builder
    public Lists(Member member, Board board, String title, Long idx) {
        this.member = member;
        this.board = board;
        this.title = title;
        this.idx = idx;
    }

    //연관관계 메서드
    public void setBoard(Board board) {
        this.board = board;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void update(String listTitle) {
        this.title = listTitle;
    }

    public void updateIndex(Long idx) {
        this.idx = idx;
    }
}
