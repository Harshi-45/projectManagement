package com.projectManagement.project.repository;

import com.projectManagement.project.entity.WorkspaceMembershipEntity;
import com.projectManagement.project.model.WorkspaceMembership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMembershipEntity, WorkspaceMembership> {

    boolean existsByWorkspaceIdAndUserId(Long workspaceId, Long userId);

    Optional<WorkspaceMembershipEntity> findByWorkspaceIdAndUserId(Long workspaceId, Long userId);

    List<WorkspaceMembershipEntity> findAllByWorkspaceId(Long workspaceId);
}
