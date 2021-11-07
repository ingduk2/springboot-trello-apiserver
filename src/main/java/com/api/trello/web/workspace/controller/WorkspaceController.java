package com.api.trello.web.workspace.controller;

import com.api.trello.web.common.dto.resopnse.SuccessResponse;
import com.api.trello.web.workspace.dto.WorkspaceSaveRequestDto;
import com.api.trello.web.workspace.dto.WorkspaceUpdateRequestDto;
import com.api.trello.web.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @GetMapping("/workspaces")
    public ResponseEntity<SuccessResponse> findAllWorkspace() {
        return ResponseEntity.ok().body(SuccessResponse.success(workspaceService.findAllWorkspace()));
    }

    @GetMapping("/workspaces/{workspaceId}")
    public ResponseEntity<SuccessResponse> findWorkspaceById(@PathVariable Long workspaceId) {
        return ResponseEntity.ok().body(SuccessResponse.success(workspaceService.findById(workspaceId)));
    }

    @PostMapping("/workspaces")
    public ResponseEntity<SuccessResponse> saveWorkspace(@RequestBody @Valid WorkspaceSaveRequestDto requestDto) {
        return ResponseEntity.ok().body(SuccessResponse.success(workspaceService.save(requestDto)));
    }

    @DeleteMapping("/workspaces/{workspaceId}")
    public ResponseEntity<SuccessResponse> deleteWorkspace(@PathVariable Long workspaceId) {
        return ResponseEntity.ok().body(SuccessResponse.success(workspaceService.delete(workspaceId)));
    }

    @PutMapping("/workspaces/{workspaceId}")
    public ResponseEntity<SuccessResponse> updateWorkspace(@PathVariable Long workspaceId,
                                                           @RequestBody @Valid WorkspaceUpdateRequestDto requestDto) {
        return ResponseEntity.ok().body(SuccessResponse.success(workspaceService.update(workspaceId, requestDto)));
    }
}
