package com.api.trello.web.member.domain;

import com.api.trello.web.common.domain.BaseTimeEntity;
import com.api.trello.web.workspace.domain.Workspace;
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

    @Column
    private String password;

    @Builder
    public Member(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void update(String name) {
        this.name = name;
    }

    /**
     * 연관관계 메서드
     */
    public void addWorkspace(Workspace workSpace) {
        workSpace.setMember(this);
    }


}
