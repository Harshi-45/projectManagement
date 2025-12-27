package com.projectManagement.project.service;

import com.projectManagement.project.model.InviteCollaboratorRequest;
import com.projectManagement.project.model.WorkSpace;
import com.projectManagement.project.model.WorkspaceMembership;

import java.util.List;

public interface WorkspaceService {

    WorkSpace create(WorkSpace request);

    String invite(Long id, InviteCollaboratorRequest request);

}
