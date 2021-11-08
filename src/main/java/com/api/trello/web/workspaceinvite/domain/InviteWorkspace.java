package com.api.trello.web.workspaceinvite.domain;

import com.api.trello.web.member.domain.Member;
import com.api.trello.web.workspace.domain.Workspace;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "inviteworkspace")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InviteWorkspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @Builder
    public InviteWorkspace(Member member, Workspace workspace) {
        this.member = member;
        this.workspace = workspace;
    }
}
