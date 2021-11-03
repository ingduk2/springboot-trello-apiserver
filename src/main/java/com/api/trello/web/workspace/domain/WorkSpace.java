package com.api.trello.web.workspace.domain;

import com.api.trello.web.common.domain.BaseTimeEntity;
import com.api.trello.web.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class WorkSpace extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workspace_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String name;

    @Builder
    public WorkSpace(Member member, String name) {
        this.member = member;
        this.name = name;
    }

    //연관관계
    public void setMember(Member member) {
        this.member = member;
    }

}
