package com.example.task_management_system_rest.service.serviceImpl;

import com.example.task_management_system_rest.entity.Category;
import com.example.task_management_system_rest.repository.CategoryRepository;
import com.example.task_management_system_rest.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category saveCategoryByName(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        Category category = Category.builder().name(categoryName).build();
        return categoryRepository.save(category);
    }
}
