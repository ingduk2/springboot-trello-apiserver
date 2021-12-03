package com.api.trello.web.list.controller;

import com.api.trello.web.common.dto.resopnse.SuccessResponse;
import com.api.trello.web.list.dto.ListsIndexUpdateDto;
import com.api.trello.web.list.dto.ListsResponseDto;
import com.api.trello.web.list.dto.ListsSaveRequestDto;
import com.api.trello.web.list.dto.ListsUpdateRequestDto;
import com.api.trello.web.list.service.ListsService;
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
public class ListsController {

    private final ListsService listsService;

    @GetMapping("/lists/{listId}")
    public ResponseEntity<SuccessResponse> findById(@PathVariable Long listId) {
        ListsResponseDto responseDto = listsService.findResponseDto(listId);
        return ResponseEntity.ok(SuccessResponse.success(responseDto));
    }

    ///lists/all/1 보다는 나아보임
    @GetMapping("/boards/{boardId}/lists")
    public ResponseEntity<SuccessResponse> findAllIndexAsc(@PathVariable Long boardId) {
        List<ListsResponseDto> responseDtos = listsService.findAllIndexAsc(boardId);
        return ResponseEntity.ok(SuccessResponse.success(responseDtos));
    }

    @PostMapping("/lists")
    public ResponseEntity<SuccessResponse> save(@RequestBody @Valid ListsSaveRequestDto requestDto,
                                                UriComponentsBuilder uriBuilder) {
        ListsResponseDto responseDto = listsService.save(requestDto);

        URI location = uriBuilder.path("/lists/{id}")
                .buildAndExpand(responseDto.getListId())
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity<>(SuccessResponse.created(responseDto), headers, HttpStatus.CREATED);
    }

    @PutMapping("/lists/{listId}")
    public ResponseEntity<SuccessResponse> update(@PathVariable Long listId,
                                                  @RequestBody ListsUpdateRequestDto requestDto) {
        ListsResponseDto responseDto = listsService.update(listId, requestDto);
        return ResponseEntity.ok(SuccessResponse.success(responseDto));
    }

    @PutMapping("/lists/index")
    public ResponseEntity<SuccessResponse> updateIndex(@RequestBody ListsIndexUpdateDto requestDto) {
        ListsResponseDto responseDto = listsService.updateOrder(requestDto);
        return ResponseEntity.ok(SuccessResponse.success(responseDto));
    }

    @DeleteMapping("/lists/{listId}")
    public ResponseEntity<SuccessResponse> delete(@PathVariable Long listId) {
        Long deletedId = listsService.delete(listId);
        return ResponseEntity.ok(SuccessResponse.delete(deletedId));
    }

}
