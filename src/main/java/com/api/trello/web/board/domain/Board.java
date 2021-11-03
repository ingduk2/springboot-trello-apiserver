package com.api.trello.web.board.domain;

import com.api.trello.web.workspace.domain.WorkSpace;
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
    private WorkSpace workSpace;

    @Column
    private String title;

    public Board(WorkSpace workSpace, String title) {
        this.workSpace = workSpace;
        this.title = title;
    }

    //연관관계
    public void setWorkSpace(WorkSpace workSpace) {
        this.workSpace = workSpace;
    }
}
