package com.api.trello.web.workspace.domain;

import com.api.trello.web.common.domain.BaseTimeEntity;
import com.api.trello.web.member.domain.Member;
import com.api.trello.web.workspaceinvite.domain.InviteWorkspace;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class Workspace extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workspace_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "workspace")
    Set<InviteWorkspace> inviteWorkspaces = new HashSet<>();

    @Column
    private String name;

    @Column
    private String shortName;

    @Column
    private String description;

    @Builder
    public Workspace(Member member, String name, String shortName, String description) {
        this.member = member;
        this.name = name;
        this.shortName = shortName;
        this.description = description;
    }

    //연관관계
    public void setMember(Member member) {
        this.member = member;
    }

    public void update(String name, String shortName, String description) {
        this.name = name;
        this.shortName = shortName;
        this.description = description;
    }

}
