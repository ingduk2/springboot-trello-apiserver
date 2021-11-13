package com.api.trello.web.board.domain;

import com.api.trello.web.member.domain.Member;
import com.api.trello.web.workspace.domain.Workspace;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workSpace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String title;

    @Builder
    public Board( Workspace workSpace, Member member, String title) {
        this.workSpace = workSpace;
        this.member = member;
        this.title = title;
    }


    //연관관계
    public void setWorkSpace(Workspace workSpace) {
        this.workSpace = workSpace;
    }

    public void update(String title) {
        this.title = title;
    }
}
