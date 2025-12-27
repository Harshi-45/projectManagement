package com.projectManagement.project.controller;

import com.projectManagement.project.Page.PageResponse;
import com.projectManagement.project.model.Project;
import com.projectManagement.project.service.impl.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping()
    public ResponseEntity<Project> create(@RequestBody Project project){
        return new ResponseEntity<>(projectService.create(project), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getById(@PathVariable(value = "id") Long id){
        return new ResponseEntity<>(projectService.getById(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<PageResponse<Project>> getAll(Pageable pageable,
                                                        @RequestParam(name = "q", required = false) String query){
        return new ResponseEntity<>(projectService.getAll(pageable,query), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> update(@PathVariable(value = "id") Long id,
                                          @RequestBody Project project){
        return new ResponseEntity<>(projectService.update(id, project), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(HttpServletRequest request,
                                       @PathVariable(value = "id") Long id){
        projectService.delete(id, request);
        return ResponseEntity.noContent().build();
    }
}
