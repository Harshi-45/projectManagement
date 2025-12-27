package com.projectManagement.project.controller;

import com.projectManagement.project.model.InviteCollaboratorRequest;
import com.projectManagement.project.model.WorkSpace;
import com.projectManagement.project.model.WorkspaceMembership;
import com.projectManagement.project.service.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping
    public ResponseEntity<WorkSpace> create(@RequestBody WorkSpace request){
        return new ResponseEntity<>(workspaceService.create(request), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<String> inviteCollaborator(@PathVariable Long id,@RequestBody InviteCollaboratorRequest request){
        return new ResponseEntity<>(workspaceService.invite(id,request), HttpStatus.CREATED);
    }


}
