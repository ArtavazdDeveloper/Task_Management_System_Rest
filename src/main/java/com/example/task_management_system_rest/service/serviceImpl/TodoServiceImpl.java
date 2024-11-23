package com.example.task_management_system_rest.service.serviceImpl;

import com.example.task_management_system_rest.entity.Todo;
import com.example.task_management_system_rest.repository.TodoRepository;
import com.example.task_management_system_rest.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;

    @Override
    public Todo saveToto(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public List<Todo> findCurrentUserTodos(int currentUserId) {
        return todoRepository.findAllByUserId(currentUserId);
    }

    @Override
    public Todo findTodoById(int todoId) {
        Optional<Todo> byId = todoRepository.findById(todoId);
        return byId.get();
    }

    @Override
    public void deleteTodoById(int todoId) {
        todoRepository.deleteById(todoId);
    }
}
