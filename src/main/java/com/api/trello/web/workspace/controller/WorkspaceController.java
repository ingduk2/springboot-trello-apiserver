package com.api.trello.web.workspace.controller;

import com.api.trello.web.common.dto.resopnse.SuccessResponse;
import com.api.trello.web.workspace.dto.WorkspaceResponseDto;
import com.api.trello.web.workspace.dto.WorkspaceSaveRequestDto;
import com.api.trello.web.workspace.dto.WorkspaceUpdateRequestDto;
import com.api.trello.web.workspace.service.WorkspaceService;
import com.api.trello.web.workspaceinvite.service.InviteWorkspaceService;
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
public class WorkspaceController {

    private final WorkspaceService workspaceService;
    private final InviteWorkspaceService inviteWorkspaceService;

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
                .buildAndExpand(savedWorkspace.getId())
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity<>(SuccessResponse.created(savedWorkspace), headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/workspaces/{workspaceId}")
    public ResponseEntity<SuccessResponse> deleteWorkspace(@PathVariable Long workspaceId) {
        Long delete = workspaceService.delete(workspaceId);
        //HttpStatus.NO_CONTENT 는 Body를 보내주지 않음.
        return new ResponseEntity<>(SuccessResponse.delete(delete), HttpStatus.OK);
    }

    @PutMapping("/workspaces/{workspaceId}")
    public ResponseEntity<SuccessResponse> updateWorkspace(@PathVariable Long workspaceId,
                                                           @RequestBody @Valid WorkspaceUpdateRequestDto requestDto) {
        return ResponseEntity.ok()
                .body(SuccessResponse.success(workspaceService.update(workspaceId, requestDto)));
    }
}
