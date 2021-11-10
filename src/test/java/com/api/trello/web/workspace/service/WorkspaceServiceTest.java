package com.api.trello.web.workspace.service;

import com.api.trello.web.member.domain.Member;
import com.api.trello.web.member.service.MemberService;
import com.api.trello.web.workspace.domain.Workspace;
import com.api.trello.web.workspace.domain.WorkspaceRepository;
import com.api.trello.web.workspace.dto.WorkspaceResponseDto;
import com.api.trello.web.workspace.dto.WorkspaceSaveRequestDto;
import com.api.trello.web.workspace.dto.WorkspaceUpdateRequestDto;
import com.api.trello.web.workspace.exception.CWorkspaceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceTest {

    @InjectMocks
    WorkspaceService workspaceService;

    @Mock
    WorkspaceRepository workspaceRepository;

    @Mock
    MemberService memberService;

    @BeforeEach
    void setUp() {
    }

    private Member getMember() {
        return  Member.builder()
                .id(1L)
                .name("ingduk2")
                .email("ingduk2@gmail.com")
                .password("qwer1234")
                .build();
    }

    private List<Workspace> getWorkspaces() {
        List<Workspace> workspaces = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            workspaces.add(getWorkspace((long) i));
        }

        return workspaces;
    }

    private List<Workspace> getInvitedWorkspaces() {
        List<Workspace> workspaces = new ArrayList<>();

        for (int i = 11; i < 15; i++) {
            workspaces.add(getWorkspace((long) i));
        }

        return workspaces;
    }

    private Workspace getWorkspace(Long id) {
        Workspace workspace = Workspace.builder()
                .name("workspace" + id)
                .member(getMember())
                .build();

        ReflectionTestUtils.setField(workspace, "id", 1L);
        return workspace;
    }

    @Test
    void findAllWorkspace_성공() {
        //given
        given(memberService.findCurrentMember()).willReturn(getMember());
        given(workspaceRepository.findAllByMember(any(Member.class))).willReturn(getWorkspaces());

        //when
        List<WorkspaceResponseDto> allWorkspace = workspaceService.findAllWorkspace();

        //then
        assertEquals(allWorkspace.size(), 10);
        assertEquals(allWorkspace.get(0).getName(), "workspace1");
        assertEquals(allWorkspace.get(0).getId(), 1L);
    }

    @Test
    void findAllWorkspace_초대_성공() {
        //given
        given(memberService.findCurrentMember()).willReturn(getMember());
        given(workspaceRepository.findAllByMember(any(Member.class))).willReturn(getWorkspaces());
        given(workspaceRepository.findAllFetchInvite(any(Member.class))).willReturn(getInvitedWorkspaces());

        //when
        List<WorkspaceResponseDto> allWorkspace = workspaceService.findAllWorkspace();

        //then
        assertEquals(allWorkspace.size(), 14);
    }


    @Test
    void findWorkspaceResponseDtoById_workspaceId_없는경우_실패_CWorkspaceNotFoundException() {
        //given
        Long workspaceId = 1L;
        given(workspaceRepository.findById(workspaceId)).willReturn(Optional.empty());

        //when
        //then
        assertThrows(CWorkspaceNotFoundException.class,
                () -> workspaceService.findWorkspaceResponseDtoById(workspaceId));
    }

    @Test
    void findWorkspaceResponseDtoById_workspaceId_성공() {
        //givwn
        Long workspaceId = 1L;
        given(workspaceRepository.findById(workspaceId))
                .willReturn(Optional.of(getWorkspace(workspaceId)));

        //then
        WorkspaceResponseDto dto = workspaceService.findWorkspaceResponseDtoById(workspaceId);

        //when
        assertThat(dto).isInstanceOf(WorkspaceResponseDto.class);
        assertThat(dto.getId()).isEqualTo(1L);
        assertEquals(dto.getId(), 1);
        assertEquals(dto.getName(), "workspace1");
    }

    @Test
    void findById_workspaceId_없는경우_실패_CWorkspaceNotFoundException() {
        //given
        Long workspaceId = 1L;
        given(workspaceRepository.findById(workspaceId)).willReturn(Optional.empty());

        //when
        //then
        assertThrows(CWorkspaceNotFoundException.class,
                () -> workspaceService.findById(workspaceId));
    }

    @Test
    void findById_성공() {
        //given
        Long workspaceId = 1L;
        given(workspaceRepository.findById(workspaceId))
                .willReturn(Optional.of(getWorkspace(workspaceId)));

        //when
        Workspace workspace = workspaceService.findById(workspaceId);

        //then
        assertThat(workspace).isInstanceOf(Workspace.class);
        assertEquals(workspace.getId(), workspaceId);
        assertThat(workspace.getName()).isEqualTo("workspace1");
    }


    @Test
    void workspaceSave_성공() {
        //given
        WorkspaceSaveRequestDto requestDto = WorkspaceSaveRequestDto.builder()
                .name("workspace1")
                .build();
        given(memberService.findCurrentMember())
                .willReturn(getMember());

        Long workspaceId = 1L;
        given(workspaceRepository.save(any(Workspace.class)))
                .willReturn(getWorkspace(workspaceId));

        //when
        WorkspaceResponseDto savedDto = workspaceService.save(requestDto);

        //then
        assertEquals(savedDto.getId(), workspaceId);
        assertEquals(savedDto.getEmail(), "ingduk2@gmail.com");
    }

    //delete 테스트는.. 애매하네

    @Test
    void update_없는_workspaceId_CWorkspaceNotFoundException() {
        //given
        Long workspaceId = 1L;
        WorkspaceUpdateRequestDto updateRequestDto = WorkspaceUpdateRequestDto.builder()
                .name("workspace2")
                .build();

        given(workspaceRepository.findById(workspaceId))
                .willReturn(Optional.empty());

        //when
        //then
        assertThrows(CWorkspaceNotFoundException.class,
                () -> workspaceService.update(workspaceId, updateRequestDto));
    }

    @Test
    void update_성공() {
        //given
        Long workspaceId = 1L;
        WorkspaceUpdateRequestDto updateRequestDto = WorkspaceUpdateRequestDto.builder()
                .name("workspace2")
                .build();

        given(workspaceRepository.findById(workspaceId))
                .willReturn(Optional.ofNullable(getWorkspace(workspaceId)));

        //when
        WorkspaceResponseDto updatedDto = workspaceService.update(workspaceId, updateRequestDto);

        //then
        assertThat(updatedDto.getName()).isEqualTo(updateRequestDto.getName());
    }

}