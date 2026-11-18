package me.seonghyeon.todopractice.service;

import lombok.RequiredArgsConstructor;
import me.seonghyeon.todopractice.domain.Todo;
import me.seonghyeon.todopractice.dto.TodoSaveRequest;
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
    public Todo save(TodoSaveRequest todoSaveRequest) {
        return todoRepository.save(todoSaveRequest.toEntity());
    }

    // 할 일 전체 조회
    public List<Todo> findAll() {
        return todoRepository.findAll(Sort.by("id"));
    }

    // 할 일 개별 조회
    public Todo findById(Long id) {
        return todoRepository.findById(id)  // 해당 ID의 할 일이 없으면 예외 발생
                .orElseThrow(() -> new IllegalArgumentException("할 일이 없습니다."));
    }

    // 할 일 수정
    @Transactional
    public Todo update(Long id, TodoSaveRequest todoSaveRequest) {
        Todo todo = findById(id);
        todo.update(
                todoSaveRequest.getTitle(),
                todoSaveRequest.getDescription()
        );
        return todo;
    }

    // 할 일 완료 상태 변경
    @Transactional
    public Todo toggleCompleted(Long id) {
        Todo todo = findById(id);
        todo.toggleCompleted();
        return todo;
    }

    // 할 일 삭제
    @Transactional
    public void deleteById(Long id) {
        Todo todo = findById(id);
        todoRepository.delete(todo);
    }


}
