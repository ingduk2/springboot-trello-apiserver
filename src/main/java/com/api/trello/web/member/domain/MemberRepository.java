package com.api.trello.web.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    int countByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
}
