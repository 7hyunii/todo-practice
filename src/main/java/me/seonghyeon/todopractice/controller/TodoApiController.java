package me.seonghyeon.todopractice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.seonghyeon.todopractice.dto.TodoResponse;
import me.seonghyeon.todopractice.dto.TodoSaveRequest;
import me.seonghyeon.todopractice.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TodoApiController {

    private final TodoService todoService;

    // 할 일 등록
    @PostMapping("/todos")
    public ResponseEntity<TodoResponse> addTodos(@Valid @RequestBody TodoSaveRequest todoSaveRequest) {
        TodoResponse savedTodo = todoService.save(todoSaveRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedTodo);
    }

    // 할 일 목록 전체 조회
    @GetMapping("/todos")
    public ResponseEntity<List<TodoResponse>> getAllTodos() {
        return ResponseEntity.ok()
                .body(todoService.findAll());
    }

    // 할 일 개별 조회
    @GetMapping("/todos/{id}")
    public ResponseEntity<TodoResponse> getTodoById(@PathVariable Long id) {
        TodoResponse todo = todoService.findById(id);

        return ResponseEntity.ok()
                .body(todo);
    }

    // 할 일 수정
    @PutMapping("/todos/{id}")
    public ResponseEntity<TodoResponse> updateTodoById(@PathVariable Long id, @Valid @RequestBody TodoSaveRequest todoSaveRequest) {
        TodoResponse todo = todoService.update(id, todoSaveRequest);

        return ResponseEntity.ok()
                .body(todo);
    }

    // 할 일 완료 상태 변경
    @PostMapping("/todos/{id}/completed")
    public ResponseEntity<TodoResponse> toggleCompletedTodo(@PathVariable Long id) {
        TodoResponse todo = todoService.toggleCompleted(id);

        return ResponseEntity.ok()
                .body(todo);
    }

    // 할 일 삭제
    @DeleteMapping("/todos/{id}")
    public ResponseEntity<Map<String, String>> deleteTodoById(@PathVariable Long id) {
        todoService.deleteById(id);

        return ResponseEntity.ok()
                .body(Map.of("message", "할 일이 삭제되었습니다."));
    }


}
