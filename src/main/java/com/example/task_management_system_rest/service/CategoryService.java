package com.example.task_management_system_rest.service;

import com.example.task_management_system_rest.entity.Category;

public interface CategoryService {
    Category saveCategoryByName(String categoryName);
}
