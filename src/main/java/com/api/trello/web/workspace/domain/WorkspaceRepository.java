package com.api.trello.web.workspace.domain;

import com.api.trello.web.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    @Query
    List<Workspace> findAllByMember(final Member member);

    //member를 찾아야하기 때문에 어차피 Query가 2번 날라가게됨
    @Query(value = "select w" +
            " from Workspace w" +
            " join fetch w.member t" +
            " where t.id = :memberId")
    List<Workspace> findAllByMemberId(@Param("memberId") Long memberId);

    //workspace : inviteworkspace (1 : n)
    //workspace : member (n : 1)
    @Query(value = "select w" +
            " from Workspace w" +
            " join fetch w.inviteWorkspaces it" +
            " join fetch w.member m" +
            " where it.member = :member")
    List<Workspace> findAllFetchInvite(@Param("member") Member member);

    @Query(value = "select w" +
            " from Workspace w" +
            " join fetch w.member")
    List<Workspace> findAllFetchManyto();
}
