package com.projectManagement.project.repository;

import com.projectManagement.project.entity.WorkSpaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<WorkSpaceEntity , Long> {

    boolean existsByName(String name);
}
