package com.api.trello.web.board.domain;

import com.api.trello.web.workspace.domain.Workspace;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workSpace;

    @Column
    private String title;

    public Board(Workspace workSpace, String title) {
        this.workSpace = workSpace;
        this.title = title;
    }

    //연관관계
    public void setWorkSpace(Workspace workSpace) {
        this.workSpace = workSpace;
    }
}
