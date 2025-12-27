package com.projectManagement.project.mapper;

import com.projectManagement.project.entity.ProjectEntity;
import com.projectManagement.project.entity.WorkSpaceEntity;
import com.projectManagement.project.model.Project;
import com.projectManagement.project.model.WorkSpace;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProjectMapper extends BaseMapper<ProjectEntity, Project> {


}
