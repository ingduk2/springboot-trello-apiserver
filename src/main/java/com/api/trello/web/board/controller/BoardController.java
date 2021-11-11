package com.api.trello.web.board.controller;

import com.api.trello.web.board.dto.BoardResponseDto;
import com.api.trello.web.board.dto.BoardSaveRequestDto;
import com.api.trello.web.board.dto.BoardUpdateRequestDto;
import com.api.trello.web.board.service.BoardService;
import com.api.trello.web.common.dto.resopnse.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/boards")
    public ResponseEntity<SuccessResponse> findAllBoard() {
        List<BoardResponseDto> boards = boardService.findAll();
        return ResponseEntity.ok(SuccessResponse.success(boards));
    }

    @GetMapping("/boards/{boardId}")
    public ResponseEntity<SuccessResponse> findBoardById(@PathVariable Long boardId) {
        BoardResponseDto board = boardService.findBoardResponseDtoById(boardId);
        return ResponseEntity.ok(SuccessResponse.success(board));
    }

    @PostMapping("/boards")
    public ResponseEntity<SuccessResponse> saveBoard(@RequestBody @Valid BoardSaveRequestDto requestDto,
                                                     UriComponentsBuilder uriBuilder) {
        BoardResponseDto savedBoard = boardService.save(requestDto);

        URI location = uriBuilder.path("/boards/{id}")
                .buildAndExpand(savedBoard.getId())
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity<>(SuccessResponse.created(savedBoard), headers, HttpStatus.CREATED);
    }

    @PutMapping("/boards/{boardId}")
    public ResponseEntity<SuccessResponse> updateBoard(@PathVariable Long boardId,
                                                       @RequestBody @Valid BoardUpdateRequestDto requestDto) {
        BoardResponseDto updatedBoard = boardService.update(boardId, requestDto);
        return ResponseEntity.ok(SuccessResponse.success(updatedBoard));
    }

    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<SuccessResponse> deleteBoard(@PathVariable Long boardId) {
        Long deletedId = boardService.delete(boardId);
        return ResponseEntity.ok(SuccessResponse.delete(deletedId));
    }

}
