package com.example.task_management_system_rest.mapper;


import com.example.task_management_system_rest.dto.userDTO.CreateUserRequestDTO;
import com.example.task_management_system_rest.dto.userDTO.CreateUserResponseDTO;
import com.example.task_management_system_rest.dto.userDTO.UpdateUserRequestDTO;
import com.example.task_management_system_rest.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User mapToUser(CreateUserRequestDTO request);

    User mapToUser(UpdateUserRequestDTO request);

    @Mapping(target = )
    CreateUserResponseDTO mapToDto(User user);
}
