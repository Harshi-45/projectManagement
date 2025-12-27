package com.projectManagement.project.mapper;

import java.util.List;

public interface BaseMapper<E, D> {

    E toEntity(D d);

    D toDto(E e);

    List<D> toDtoList(List<E> entitieList);

}
