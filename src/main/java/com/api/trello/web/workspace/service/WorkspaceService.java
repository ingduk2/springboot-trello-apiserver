package com.api.trello.web.workspace.service;

import com.api.trello.web.member.domain.Member;
import com.api.trello.web.member.service.MemberService;
import com.api.trello.web.workspace.domain.Workspace;
import com.api.trello.web.workspace.domain.WorkspaceRepository;
import com.api.trello.web.workspace.dto.WorkspaceResponseDto;
import com.api.trello.web.workspace.dto.WorkspaceSaveRequestDto;
import com.api.trello.web.workspace.dto.WorkspaceUpdateRequestDto;
import com.api.trello.web.workspace.exception.CWorkspaceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public List<WorkspaceResponseDto> findAllWorkspace() {
        Member member = memberService.findCurrentMember();

        List<Workspace> myWorkspaces = workspaceRepository.findAllByMember(member);
        List<Workspace> inviteWorkspaces = workspaceRepository.findAllFetchInvite(member);

        List<Workspace> workspaces = Stream.of(myWorkspaces, inviteWorkspaces)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return workspaces.stream()
                .map(WorkspaceResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WorkspaceResponseDto findWorkspaceResponseDtoById(Long workspaceId) {
        Workspace workspace = findById(workspaceId);

        return WorkspaceResponseDto.of(workspace);
    }

    @Transactional(readOnly = true)
    public Workspace findById(Long workspaceId) {
        return workspaceRepository.findById(workspaceId).orElseThrow(CWorkspaceNotFoundException::new);
    }

    @Transactional
    public WorkspaceResponseDto save(WorkspaceSaveRequestDto requestDto) {
        Member member = memberService.findCurrentMember();

        Workspace workspace = requestDto.toEntity();
//        workspace.setMember(member); //????????????
        member.addWorkspace(workspace); //????????????

        Workspace save = workspaceRepository.save(workspace);
        return WorkspaceResponseDto.of(save);
    }

    @Transactional
    public Long delete(Long workspaceId) {
        Workspace workspace = findById(workspaceId);
        workspaceRepository.delete(workspace);
        return workspaceId;
    }

    @Transactional
    public WorkspaceResponseDto update(Long workspaceId, WorkspaceUpdateRequestDto requestDto) {
        Workspace workspace = findById(workspaceId);

        workspace.update(requestDto.getName(), requestDto.getShortName(), requestDto.getDescription());
        return WorkspaceResponseDto.of(workspace);
    }


}
