package me.seonghyeon.todopractice.service;

import lombok.RequiredArgsConstructor;
import me.seonghyeon.todopractice.domain.Todo;
import me.seonghyeon.todopractice.dto.TodoResponse;
import me.seonghyeon.todopractice.dto.TodoSaveRequest;
import me.seonghyeon.todopractice.exception.TodoNotFoundException;
import me.seonghyeon.todopractice.repository.TodoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    // 할 일 등록
    public TodoResponse save(TodoSaveRequest todoSaveRequest) {
        Todo savedTodo = todoRepository.save(todoSaveRequest.toEntity());
        return new TodoResponse(savedTodo);
    }

    // 할 일 전체 조회
    public List<TodoResponse> findAll() {
        return todoRepository.findAll(Sort.by("id"))
                .stream()
                .map(TodoResponse::new)
                .toList();
    }

    // 할 일 개별 조회
    public TodoResponse findById(Long id) {
        return new TodoResponse(findTodoOrThrow(id));
    }

    // 할 일 수정
    @Transactional
    public TodoResponse update(Long id, TodoSaveRequest todoSaveRequest) {
        Todo todo = findTodoOrThrow(id);
        todo.update(
                todoSaveRequest.getTitle(),
                todoSaveRequest.getDescription()
        );
        return new TodoResponse(todo);
    }

    // 할 일 완료 상태 변경
    @Transactional
    public TodoResponse toggleCompleted(Long id) {
        Todo todo = findTodoOrThrow(id);
        todo.toggleCompleted();
        return new TodoResponse(todo);
    }

    // 할 일 삭제
    @Transactional
    public void deleteById(Long id) {
        Todo todo = findTodoOrThrow(id);
        todoRepository.delete(todo);
    }

    private Todo findTodoOrThrow(Long id) {
        return todoRepository.findById(id)  // 해당 ID의 할 일이 없으면 예외 발생
                .orElseThrow(TodoNotFoundException::new);
    }

}
