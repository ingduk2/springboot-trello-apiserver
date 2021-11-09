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
import com.api.trello.web.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final WorkspaceService workspaceService;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public List<BoardResponseDto> findAll() {
        Member currentMember = memberService.findCurrentMember();

        return boardRepository.findAllByMember(currentMember).stream()
                .map(BoardResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BoardResponseDto findBoardResponseDtoById(Long boardId) {
        return BoardResponseDto.of(findById(boardId));
    }

    @Transactional(readOnly = true)
    public Board findById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(CBoardNotFoundException::new);
    }

    @Transactional
    public BoardResponseDto save(BoardSaveRequestDto requestDto) {
        Member currentMember = memberService.findCurrentMember();

        Workspace workspace = workspaceService.findById(requestDto.getWorkspaceId());

        Board savedBoard = boardRepository.save(Board.builder()
                .workSpace(workspace)
                .member(currentMember)
                .title(requestDto.getTitle())
                .build());

        return BoardResponseDto.of(savedBoard);
    }

    @Transactional
    public BoardResponseDto update(Long boardId, BoardUpdateRequestDto requestDto) {
        Board board = findById(boardId);
        board.update(requestDto.getTitle());

        return BoardResponseDto.of(board);
    }

    @Transactional
    public Long delete(Long boardId) {
        boardRepository.delete(findById(boardId));
        return boardId;
    }

}
