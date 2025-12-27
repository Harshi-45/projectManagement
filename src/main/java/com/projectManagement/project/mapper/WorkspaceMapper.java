package com.projectManagement.project.mapper;

import com.projectManagement.project.entity.WorkSpaceEntity;
import com.projectManagement.project.model.WorkSpace;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WorkspaceMapper extends BaseMapper<WorkSpaceEntity, WorkSpace> {

    @Override
    @Mapping(target = "ownerId",ignore = true)
    WorkSpace toDto(WorkSpaceEntity entity);

    @AfterMapping
    default void  afterMappingToDto(@MappingTarget WorkSpace dto, WorkSpaceEntity entity)
    {
        if(ObjectUtils.isNotEmpty(entity.getOwner())){
            dto.setOwnerId(entity.getOwner().getId());
        }
    }

}
