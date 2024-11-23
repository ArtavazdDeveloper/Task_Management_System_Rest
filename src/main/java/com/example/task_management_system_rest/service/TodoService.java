package com.example.task_management_system_rest.service;

import com.example.task_management_system_rest.entity.Todo;

import java.util.List;

public interface TodoService {
    Todo saveToto(Todo todo);

    List<Todo> findCurrentUserTodos(int currentUserId);

    Todo findTodoById(int todoId);

    void deleteTodoById(int todoId);
}
