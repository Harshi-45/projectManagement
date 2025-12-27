package com.projectManagement.project.service.impl;

import com.projectManagement.project.entity.UserEntity;
import com.projectManagement.project.entity.WorkSpaceEntity;
import com.projectManagement.project.entity.WorkspaceMembershipEntity;
import com.projectManagement.project.exception.DataValidationException;
import com.projectManagement.project.mapper.WorkspaceMapper;
import com.projectManagement.project.model.InviteCollaboratorRequest;
import com.projectManagement.project.model.Role;
import com.projectManagement.project.model.WorkSpace;
import com.projectManagement.project.repository.UserRepository;
import com.projectManagement.project.repository.WorkspaceMemberRepository;
import com.projectManagement.project.repository.WorkspaceRepository;
import com.projectManagement.project.service.WorkspaceService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import static com.projectManagement.project.model.Role.OWNER;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository repository;

    private final WorkspaceMemberRepository workspaceMemberRepository;

    private final UserRepository userRepository;

    private final WorkspaceMapper mapper;

    public WorkspaceServiceImpl(WorkspaceRepository repository, WorkspaceMemberRepository workspaceMemberRepository, UserRepository userRepository, WorkspaceMapper mapper) {
        this.repository = repository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public WorkSpace create(WorkSpace request) {
        if (repository.existsByName(request.getName())) {
            throw new DataValidationException("Project Already Exists");
        }
        WorkSpaceEntity entity = mapper.toEntity(request);
        UserEntity user = getUser();
        entity.setOwner(user);
        repository.save(entity);
        workspaceMemberRepository.save(
                WorkspaceMembershipEntity.builder()
                        .workspace(entity)
                        .user(user)
                        .role(OWNER)
                        .build());
        return mapper.toDto(entity);
    }


    public String getCurrentUserEmail() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    public UserEntity getUser() {
        return userRepository.findByEmail(getCurrentUserEmail()).get();
    }

    public void requireOwner(Long workspaceId) {

        Long id = getUser().getId();

        WorkspaceMembershipEntity member =
                workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceId, id)
                        .orElseThrow(() -> new DataValidationException("You are not a member of this workspace"));

        if (member.getRole() != Role.OWNER) {
            throw new DataValidationException("Only workspace owner can manage collaborators");
        }
    }

    @Override
    @Transactional
    public String invite(Long workspaceId, InviteCollaboratorRequest request) {

        if(!isValidRole(request.getRole().toString())){
            throw new DataValidationException("User Role Doesn't exists");
        }

        // Ensure only owner can invite
        requireOwner(workspaceId);

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new DataValidationException("User not found"));

        if (workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId, user.getId())) {
            throw new DataValidationException("User is already a member");
        }

        WorkSpaceEntity workspace = repository.findById(workspaceId)
                .orElseThrow(() -> new DataValidationException("Workspace not found"));

        workspaceMemberRepository.save(
                WorkspaceMembershipEntity.builder()
                        .workspace(workspace)
                        .user(user)
                        .role(request.getRole())
                        .build()
        );
        return "Invited";
    }

    public static boolean isValidRole(String value) {
        try {
            Role.valueOf(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

}

