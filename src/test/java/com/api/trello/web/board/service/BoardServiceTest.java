package com.api.trello.web.board.service;

import com.api.trello.web.board.domain.Board;
import com.api.trello.web.board.domain.BoardRepository;
import com.api.trello.web.board.dto.BoardResponseDto;
import com.api.trello.web.board.dto.BoardSaveRequestDto;
import com.api.trello.web.board.dto.BoardUpdateRequestDto;
import com.api.trello.web.board.exception.CBoardNotFoundException;
import com.api.trello.web.member.domain.Member;
import com.api.trello.web.member.service.MemberService;
import com.api.trello.web.workspace.domain.Workspace;
import com.api.trello.web.workspace.exception.CWorkspaceNotFoundException;
import com.api.trello.web.workspace.service.WorkspaceService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    BoardService boardService;

    @Mock
    BoardRepository boardRepository;
    @Mock
    WorkspaceService workspaceService;
    @Mock
    MemberService memberService;

    private Member getMember(Long id, String name, String email, String password) {
        return Member.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .build();
    }

    private Workspace getWorkspace(Long id) {
        Workspace workspace = Workspace.builder()
                .name("workspace" + id)
                .build();

        ReflectionTestUtils.setField(workspace, "id", id);
        return workspace;
    }

    private List<Board> getBoards() {
        List<Board> boards = new ArrayList<>();
        Member member = getMember(1L, "member1", "member1@gmail.com", "qwer1234");
        Workspace workspace = getWorkspace(10L);
        member.addWorkspace(workspace);

        for (int i = 1; i <= 10; i++) {
            Board board = getBoard((long) i);
            workspace.addBoard(board);
            boards.add(board);
        }

        return boards;
    }

    private Board getBoard(Long id) {
        Board board = Board.builder()
                .title("Board" + id)
                .build();

        ReflectionTestUtils.setField(board, "id", id);
        return board;
    }

    @Test
    void findAll_성공() {
        //given
        Member member = Member.builder().build();

        given(memberService.findCurrentMember())
                .willReturn(member);
        given(boardRepository.findAllByMember(any(Member.class)))
                .willReturn(getBoards());

        //when
        List<BoardResponseDto> responseDtos = boardService.findAll();

        //then
        assertThat(responseDtos.size()).isEqualTo(10);
    }

    @Test
    void findBoard_단건조회_성공() {
        //given
        Long boardId = 1L;

        given(boardRepository.findById(boardId))
                .willReturn(Optional.ofNullable(getBoard(boardId)));

        //when
        BoardResponseDto responseDto = boardService.findBoardResponseDtoById(boardId);

        //then
        assertEquals(responseDto.getBoardId(), boardId);
    }

    @Test
    void findBoard_단건조회_Board_가_없다() {
        //given
        Long boardId = 10L;

        given(boardRepository.findById(boardId))
                .willReturn(Optional.empty());

        //when
        //then
        assertThrows(CBoardNotFoundException.class,
                () -> boardService.findBoardResponseDtoById(boardId));
    }

    @Test
    void board_save_성공() {
        //given
        Long memberId = 1L;
        Long workspaceId = 2L;
        Long boardId = 3L;
        String boardTitle = "boardTitle";

        Member member = getMember(memberId, "member1", "member1@gmail.com", "qwer1234");
        Workspace workspace = getWorkspace(workspaceId);
        Board board = getBoard(boardId);
        board.update(boardTitle);

        member.addWorkspace(workspace);
        workspace.addBoard(board);

        BoardSaveRequestDto saveRequestDto = BoardSaveRequestDto.builder()
                .workspaceId(workspaceId)
                .title(boardTitle)
                .build();


        given(memberService.findCurrentMember())
                .willReturn(member);

        given(workspaceService.findById(workspaceId))
                .willReturn(workspace);

        given(boardRepository.save(any(Board.class)))
                .willReturn(board);

        //when
        BoardResponseDto responseDto = boardService.save(saveRequestDto);

        //then
        assertThat(responseDto.getBoardId()).isEqualTo(boardId);
        assertThat(responseDto.getTitle()).isEqualTo(boardTitle);
    }

    @Test
    void board_save_workspace_가_없다() {
        //given
        Long memberId = 1L;
        Long workspaceId = 2L;
        Long boardId = 3L;
        String boardTitle = "boardTitle";

        Member member = getMember(memberId, "member1", "member1@gmail.com", "qwer1234");
        Workspace workspace = getWorkspace(workspaceId);
        Board board = getBoard(boardId);
        board.update(boardTitle);

        member.addWorkspace(workspace);
        workspace.addBoard(board);

        BoardSaveRequestDto saveRequestDto = BoardSaveRequestDto.builder()
                .workspaceId(workspaceId)
                .title(boardTitle)
                .build();


        given(memberService.findCurrentMember())
                .willReturn(member);

        given(workspaceService.findById(workspaceId))
                .willThrow(new CWorkspaceNotFoundException());

        //when
        //then
        assertThrows(CWorkspaceNotFoundException.class,
                () -> boardService.save(saveRequestDto));
    }

    @Test
    void board_update_성공() {
        //given
        Long boardId = 1L;
        String boardUpdateTitle = "updateBoard!";
        BoardUpdateRequestDto requestDto = BoardUpdateRequestDto.builder()
                .title(boardUpdateTitle)
                .build();

        given(boardRepository.findById(boardId))
                .willReturn(Optional.ofNullable(getBoard(boardId)));

        //when
        BoardResponseDto responseDto = boardService.update(boardId, requestDto);

        //then
        assertEquals(responseDto.getBoardId(), boardId);
        assertEquals(responseDto.getTitle(), boardUpdateTitle);
    }

    @Test
    void board_update_실패_board가_없다() {
        //given
        Long boardId = 7L;
        String boardUpdateTitle = "updatedBoard";
        BoardUpdateRequestDto requestDto = BoardUpdateRequestDto.builder()
                .title(boardUpdateTitle)
                .build();

        given(boardRepository.findById(boardId))
                .willReturn(Optional.empty());

        //when
        //then
        assertThrows(CBoardNotFoundException.class,
                () -> boardService.update(boardId, requestDto));
    }

    @Test
    void board_delete_성공() {
        //given
        Long deleteBoardId = 4L;

        Board board = getBoard(deleteBoardId);

        given(boardRepository.findById(deleteBoardId))
                .willReturn(Optional.ofNullable(board));

        //void..?
        doNothing().when(boardRepository).delete(board);

        //when
        Long deletedId = boardService.delete(deleteBoardId);

        //then
        assertThat(deletedId).isEqualTo(deleteBoardId);
    }

    @Test
    void board_delete_board가_없다_실패() {
        //given
        Long deleteBoardId = 5L;
        Board board = getBoard(deleteBoardId);

        given(boardRepository.findById(deleteBoardId))
                .willReturn(Optional.empty());

        //when
        //then
        assertThrows(CBoardNotFoundException.class,
                () -> boardService.delete(deleteBoardId));
    }

}