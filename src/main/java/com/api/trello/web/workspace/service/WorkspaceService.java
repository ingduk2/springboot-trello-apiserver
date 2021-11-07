package com.api.trello.web.workspace.service;

import com.api.trello.util.SecurityUtil;
import com.api.trello.web.member.domain.Member;
import com.api.trello.web.member.domain.MemberRepository;
import com.api.trello.web.member.exception.CEmailNotFoundException;
import com.api.trello.web.workspace.domain.Workspace;
import com.api.trello.web.workspace.domain.WorkspaceRepository;
import com.api.trello.web.workspace.dto.WorkspaceResponseDto;
import com.api.trello.web.workspace.dto.WorkspaceSaveRequestDto;
import com.api.trello.web.workspace.dto.WorkspaceUpdateRequestDto;
import com.api.trello.web.workspace.exception.CWorkspaceNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<WorkspaceResponseDto> findAllWorkspace() {
        Member member = findByCurrentMember();

        return workspaceRepository.findAllByMember(member).stream()
                .map(WorkspaceResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WorkspaceResponseDto findById(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(CWorkspaceNotFound::new);

        return WorkspaceResponseDto.of(workspace);
    }

    @Transactional
    public WorkspaceResponseDto save(WorkspaceSaveRequestDto requestDto) {
        Member member = findByCurrentMember();

        Workspace workspace = requestDto.toEntity();
//        workspace.setMember(member); //연관관계
        member.addWorkspace(workspace); //연관관계

        Workspace save = workspaceRepository.save(workspace);
        return WorkspaceResponseDto.of(save);
    }

    @Transactional
    public Long delete(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(CWorkspaceNotFound::new);

        workspaceRepository.delete(workspace);
        return workspaceId;
    }

    @Transactional
    public WorkspaceResponseDto update(Long workspaceId, WorkspaceUpdateRequestDto requestDto) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(CWorkspaceNotFound::new);

        workspace.update(requestDto.getName(), requestDto.getShortName(), requestDto.getDescription());
        return WorkspaceResponseDto.of(workspace);
    }

    private Member findByCurrentMember() {
        String userEmail = SecurityUtil.getCurrentUsername().orElseThrow(CEmailNotFoundException::new);
        return memberRepository.findByEmail(userEmail).orElseThrow(CEmailNotFoundException::new);
    }

}
