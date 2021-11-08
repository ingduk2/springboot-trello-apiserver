package com.api.trello.web.workspaceinvite.domain;

import com.api.trello.web.member.domain.Member;
import com.api.trello.web.workspace.domain.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InviteWorkspaceRepository extends JpaRepository<InviteWorkspace,Long> {
    List<InviteWorkspace> findAllByMember(Member member);

    int countByMemberAndWorkspace(Member member, Workspace workspace);
}
