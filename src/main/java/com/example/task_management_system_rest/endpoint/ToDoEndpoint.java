package com.example.task_management_system_rest.endpoint;


import com.example.task_management_system_rest.dto.categoriesDTO.ChangeTodoStatusRequestDto;
import com.example.task_management_system_rest.dto.categoriesDTO.CreateTodoRequestDTO;
import com.example.task_management_system_rest.dto.categoriesDTO.CreateTodoResponseDTO;
import com.example.task_management_system_rest.entity.Category;
import com.example.task_management_system_rest.entity.Status;
import com.example.task_management_system_rest.entity.Todo;
import com.example.task_management_system_rest.mapper.TodoMapper;
import com.example.task_management_system_rest.security.CurrentUser;
import com.example.task_management_system_rest.service.CategoryService;
import com.example.task_management_system_rest.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todo")
public class ToDoEndpoint {
    private final TodoService todoService;
    private final TodoMapper todoMapper;
    private final CategoryService categoryService;


    @PostMapping()
    public ResponseEntity<CreateTodoResponseDTO> createCategory(
            @RequestBody CreateTodoRequestDTO createTodoRequestDTO,
            @AuthenticationPrincipal CurrentUser currentUser) {

        Category saveCategory = categoryService.saveCategoryByName(createTodoRequestDTO.getCategoryName());

        Todo todo = todoMapper.mapTodo(createTodoRequestDTO);
        todo.setCategory(saveCategory);
        todo.setUser(currentUser.getUser());
        todo.setStatus(Status.NOT_STARTED);

        CreateTodoResponseDTO createTodoResponseDTO = todoMapper.mapToResponseDTO(todoService.saveToto(todo));
        return ResponseEntity.ok(createTodoResponseDTO);
    }

    @GetMapping()
    public ResponseEntity<List<CreateTodoResponseDTO>> findCurrentUserTodos(
            @AuthenticationPrincipal CurrentUser currentUser) {

        List<Todo> todos = todoService.findCurrentUserTodos(currentUser.getUser().getId());
        if (todos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        List<CreateTodoResponseDTO> collect = todos.stream()
                .map(todoMapper::mapToResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }

    @GetMapping("/byStatus")
    public ResponseEntity<List<CreateTodoResponseDTO>> findCurrentUserTodosByStatus(
            @RequestParam("todoStatus") String status,
            @AuthenticationPrincipal CurrentUser currentUser) {

        List<Todo> todos = todoService.findCurrentUserTodos(currentUser.getUser().getId());
        if (todos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<CreateTodoResponseDTO> collect = todos.stream()
                .map(todoMapper::mapToResponseDTO)
                .filter(todo -> todo.getStatus().equals(status))
                .collect(Collectors.toList());

        return ResponseEntity.ok(collect);
    }

    @GetMapping("/byCategory")
    public ResponseEntity<List<CreateTodoResponseDTO>> findCurrentUserTodosByCategory(
            @RequestParam("todoCategory") String category,
            @AuthenticationPrincipal CurrentUser currentUser) {

        List<Todo> todos = todoService.findCurrentUserTodos(currentUser.getUser().getId());
        if (todos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<CreateTodoResponseDTO> collect = todos.stream()
                .map(todoMapper::mapToResponseDTO)
                .filter(todo -> todo.getCreateCategoryRequestDTO().getName().equals(category))
                .collect(Collectors.toList());

        return ResponseEntity.ok(collect);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changeTodoStatus(
            @PathVariable("id") int todoId,
            @RequestBody ChangeTodoStatusRequestDto changeTodoStatusRequestDto) {

        Todo todo = todoService.findTodoById(todoId);
        String upperCase = changeTodoStatusRequestDto.getChangeStatus().toUpperCase(Locale.ROOT).trim();

        if (upperCase.equals("NOT_STARTED") || upperCase.equals("IN_PROGRESS") || upperCase.equals("DONE")) {
            todo.setStatus(Status.valueOf(upperCase));
            todoService.saveToto(todo);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletCurrentUserTodoById(
            @PathVariable("id") int todoId,
            @AuthenticationPrincipal CurrentUser currentUser) {

        Todo todo = todoService.findTodoById(todoId);

        if (todo != null) {
            if (todo.getUser().getId() == currentUser.getUser().getId()) {
                todoService.deleteTodoById(todoId);
                return ResponseEntity.ok().build();
            }

        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

}
