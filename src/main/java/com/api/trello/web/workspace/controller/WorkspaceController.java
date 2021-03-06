package com.api.trello.web.workspace.controller;

import com.api.trello.web.common.dto.resopnse.SuccessResponse;
import com.api.trello.web.workspace.dto.WorkspaceResponseDto;
import com.api.trello.web.workspace.dto.WorkspaceSaveRequestDto;
import com.api.trello.web.workspace.dto.WorkspaceUpdateRequestDto;
import com.api.trello.web.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @GetMapping("/workspaces")
    public ResponseEntity<SuccessResponse> findAllWorkspace() {
        List<WorkspaceResponseDto> myWorkspaces = workspaceService.findAllWorkspace();

        return ResponseEntity.ok()
                .body(SuccessResponse.success(myWorkspaces));
    }

    @GetMapping("/workspaces/{workspaceId}")
    public ResponseEntity<SuccessResponse> findWorkspaceById(@PathVariable Long workspaceId) {
        return ResponseEntity.ok()
                .body(SuccessResponse.success(workspaceService.findWorkspaceResponseDtoById(workspaceId)));
    }

    @PostMapping("/workspaces")
    public ResponseEntity<SuccessResponse> saveWorkspace(@RequestBody @Valid WorkspaceSaveRequestDto requestDto,
                                                         UriComponentsBuilder uriBuilder) {
        WorkspaceResponseDto savedWorkspace = workspaceService.save(requestDto);

        URI location = uriBuilder.path("/workspaces/{id}")
                .buildAndExpand(savedWorkspace.getWorkspaceId())
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(SuccessResponse.created(savedWorkspace), headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/workspaces/{workspaceId}")
    public ResponseEntity<SuccessResponse> deleteWorkspace(@PathVariable Long workspaceId) {
        Long delete = workspaceService.delete(workspaceId);
        //HttpStatus.NO_CONTENT ??? Body??? ???????????? ??????.
        return new ResponseEntity<>(SuccessResponse.delete(delete), HttpStatus.OK);
    }

    @PutMapping("/workspaces/{workspaceId}")
    public ResponseEntity<SuccessResponse> updateWorkspace(@PathVariable Long workspaceId,
                                                           @RequestBody @Valid WorkspaceUpdateRequestDto requestDto) {

        WorkspaceResponseDto updated = workspaceService.update(workspaceId, requestDto);
        return ResponseEntity.ok().body(SuccessResponse.success(updated));
    }
}
