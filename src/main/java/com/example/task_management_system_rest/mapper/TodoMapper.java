package com.example.task_management_system_rest.mapper;

import com.example.task_management_system_rest.dto.categoriesDTO.CreateTodoRequestDTO;
import com.example.task_management_system_rest.dto.categoriesDTO.CreateTodoResponseDTO;
import com.example.task_management_system_rest.entity.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    Todo mapTodo(CreateTodoRequestDTO request);

    @Mapping(target = "createCategoryRequestDTO", source = "category")
    @Mapping(target = "updateUserRequestDTO", source = "user")
    CreateTodoResponseDTO mapToResponseDTO(Todo todo);
}
