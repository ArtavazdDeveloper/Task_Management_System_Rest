package com.example.task_management_system_rest.dto.categoriesDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTodoRequestDTO {
    private String categoryName;
    private String title;
}
