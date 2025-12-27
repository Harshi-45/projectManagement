package com.projectManagement.project.service.impl;

import com.projectManagement.project.entity.ProjectEntity;
import com.projectManagement.project.entity.WorkSpaceEntity;
import com.projectManagement.project.entity.WorkspaceMembershipEntity;
import com.projectManagement.project.exception.DataValidationException;
import com.projectManagement.project.mapper.ProjectMapper;
import com.projectManagement.project.Page.PageResponse;
import com.projectManagement.project.Page.PageUtil;
import com.projectManagement.project.model.Project;
import com.projectManagement.project.model.Role;
import com.projectManagement.project.repository.ProjectRepository;
import com.projectManagement.project.repository.WorkspaceMemberRepository;
import com.projectManagement.project.repository.WorkspaceRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectMapper mapper;

    private final WorkspaceRepository workspaceRepository;

    private final ProjectRepository repository;

    private final WorkspaceMemberRepository workspaceMemberRepository;

    private final WorkspaceServiceImpl service;

    public ProjectService(ProjectMapper projectMapper, ProjectRepository projectRepository,
                          WorkspaceRepository workspaceRepository, WorkspaceMemberRepository workspaceMemberRepository, WorkspaceServiceImpl service) {
        this.mapper = projectMapper;
        this.repository = projectRepository;
        this.workspaceRepository = workspaceRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.service = service;
    }

    public Project create(Project project) {

        ProjectEntity entity = mapper.toEntity(project);
        Optional<WorkSpaceEntity> workspace = workspaceRepository.findById(project.getWorkspaceId());
        if(ObjectUtils.isNotEmpty(workspace)) {
            entity.setWorkspace(workspace.get());
        }
        entity.setOwnerId(workspace.get().getOwner().getId());
        return mapper.toDto(repository.save(entity));
    }

    public Project getById(Long id) {
        Optional<ProjectEntity> project = repository.findById(id);
        if(project.isEmpty()){
            throw new DataValidationException("Project not Found");
        }
        ProjectEntity entity = project.get();
        return mapper.toDto(entity);
    }

    public PageResponse<Project> getAll(Pageable pageable,String query) {


        Pageable thePageable = pageable.withPage(0);
        if (pageable.getPageNumber() > 0) {
            thePageable = pageable.withPage(pageable.getPageNumber() - 1);
        }
        Page<ProjectEntity> partnerEntityPages = repository.findAll(thePageable);
        List<Project> projects = mapper.toDtoList(partnerEntityPages.getContent());
        PageResponse<Project> response = new PageResponse<>();
        response.setPage(PageUtil.fromDomainPage(partnerEntityPages));
        response.setContent(projects);
        return response;
    }

    public Project update(Long id, Project project) {
        ProjectEntity existingProject = repository.findByIdAndName(id, project.getName());
        if(ObjectUtils.isEmpty(existingProject)){
            throw new DataValidationException("Project Not found");
        }
        ProjectEntity updatedProject = mapper.toEntity(project);
        return mapper.toDto(repository.save(updatedProject));
    }

    @Transactional
    public void delete(Long id, HttpServletRequest request) {
       Optional<ProjectEntity> entity = repository.findById(id);
        ProjectEntity project = entity.get();

        if(ObjectUtils.isEmpty(entity)){
            throw new DataValidationException("Project Not Found");
        }

      if(ObjectUtils.isNotEmpty(project) && project.getWorkspace().getId()!=null){
           Optional<WorkspaceMembershipEntity> workspaceMember=  workspaceMemberRepository.findByWorkspaceIdAndUserId(project.getWorkspace().getId(), service.getUser().getId());
           if(!workspaceMember.isEmpty() && workspaceMember.get().getRole()!= Role.OWNER) {
               throw new DataValidationException("Permission Denied, Contact Administrator");
           }
      }

        if (Boolean.TRUE.equals(project.getDeleted())) {
            return;
        }
        project.setDeleted(true);
        project.setModifiedAt(Instant.now());
        repository.save(project);
    }
}
