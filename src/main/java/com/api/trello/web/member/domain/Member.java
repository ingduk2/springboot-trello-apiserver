package com.api.trello.web.member.domain;

import com.api.trello.web.common.domain.BaseTimeEntity;
import com.api.trello.web.workspace.domain.WorkSpace;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Builder
    public Member(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void setWorkspace(WorkSpace workSpace) {
        workSpace.setMember(this);
    }
}
