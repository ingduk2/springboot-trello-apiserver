package com.api.trello.web.list.service;

import com.api.trello.web.board.domain.Board;
import com.api.trello.web.board.service.BoardService;
import com.api.trello.web.list.domain.Lists;
import com.api.trello.web.list.domain.ListsRepository;
import com.api.trello.web.list.dto.ListsIndexUpdateDto;
import com.api.trello.web.list.dto.ListsResponseDto;
import com.api.trello.web.list.dto.ListsSaveRequestDto;
import com.api.trello.web.list.dto.ListsUpdateRequestDto;
import com.api.trello.web.list.exception.CListNotFoundException;
import com.api.trello.web.member.domain.Member;
import com.api.trello.web.member.service.MemberService;
import com.api.trello.web.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ListsService {

    private final ListsRepository listsRepository;

    private final MemberService memberService;

    private final BoardService boardService;

    private final WorkspaceService workspaceService;

    //boardId 만 받아서 workspaceId 까지 가져오는방법.
    //boardId, workspaceId 같이 받는 방법.
    //order 를 정해줘야함 - 해당 보드 아래에 lists가 없으면 0, 있으면 +1
    @Transactional
    public ListsResponseDto save(ListsSaveRequestDto requestDto) {
        Member currentMember = memberService.findCurrentMember();

        Board board = boardService.findById(requestDto.getBoardId());

        //Order
        long idx = listsRepository.findMaxIdxByBoard(board) + 1;

        Lists lists = Lists.builder()
                .member(currentMember)
                .board(board)
                .title(requestDto.getListTitle())
                .idx(idx)
                .build();

        listsRepository.save(lists);
        return ListsResponseDto.of(lists);
    }

    @Transactional(readOnly = true)
    public ListsResponseDto findResponseDto(Long listId) {
        return ListsResponseDto.of(findById(listId));
    }

    @Transactional(readOnly = true)
    public List<ListsResponseDto> findAllIndexAsc(Long boardId) {
        Board board = boardService.findById(boardId);
        return listsRepository.findAllIndexAsc(board).stream()
                .map(ListsResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public ListsResponseDto update(Long listId, ListsUpdateRequestDto requestDto) {
        Lists lists = findById(listId);
        lists.update(requestDto.getListTitle());

        return ListsResponseDto.of(lists);
    }

    //좀더 나은 방법이있으려나. 지금은 fromId idx , toId idx 받아서 index 변경.
    //어차피 조회해야하니 id만 받자.
    @Transactional
    public ListsResponseDto updateOrder(ListsIndexUpdateDto requestDto) {
        Lists fromLists = findById(requestDto.getFromListId());
        Lists toLists = findById(requestDto.getToListId());

        //swap 임!!
        //to를 주면 from 이 to가 되서 dirtychecking 이 안일어남
        Long fromIdx = fromLists.getIdx();
        Long toIdx = toLists.getIdx();
        fromLists.updateIndex(toIdx);
        toLists.updateIndex(fromIdx);

        return ListsResponseDto.of(fromLists);
    }

    @Transactional
    public Long delete(Long listsId) {
        listsRepository.delete(findById(listsId));
        return listsId;
    }

    @Transactional(readOnly = true)
    public Lists findById(Long listsId) {
        return listsRepository.findById(listsId).orElseThrow(CListNotFoundException::new);
    }
}
