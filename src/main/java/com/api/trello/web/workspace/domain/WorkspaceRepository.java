package com.api.trello.web.workspace.domain;

import com.api.trello.web.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    List<Workspace> findAllByMember(final Member member);
}
