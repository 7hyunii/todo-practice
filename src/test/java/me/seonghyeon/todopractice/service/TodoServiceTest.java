package me.seonghyeon.todopractice.service;

import me.seonghyeon.todopractice.domain.Todo;
import me.seonghyeon.todopractice.dto.TodoSaveRequest;
import me.seonghyeon.todopractice.repository.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// 통합 테스트
@SpringBootTest
@Transactional
public class TodoServiceTest {

    private final TodoRepository todoRepository;
    private final TodoService todoService;

    @Autowired
    public TodoServiceTest(TodoRepository todoRepository, TodoService todoService) {
        this.todoRepository = todoRepository;
        this.todoService = todoService;
    }


    @DisplayName("save: 할 일 등록 성공")
    @Test
    void saveTest() {
        // given
        TodoSaveRequest todoSaveRequest = TodoSaveRequest.builder()
                .title("자바 공부")
                .description("컬렉션 복습하기")
                .build();
        // when
        Todo todo1 = todoService.save(todoSaveRequest);
        // then
        assertThat(todo1.getId()).isNotNull();
        assertThat(todo1.getTitle()).isEqualTo("자바 공부");
        assertThat(todo1.getDescription()).isEqualTo("컬렉션 복습하기");
        assertThat(todo1.isCompleted()).isFalse();
    }

    @DisplayName("findAll: 할 일 전체 조회 성공")
    @Test
    void findAllTest() {
        // given
        TodoSaveRequest todoSaveRequest1 = TodoSaveRequest.builder()
                .title("자바 공부")
                .description("컬렉션 복습하기")
                .build();

        TodoSaveRequest todoSaveRequest2 = TodoSaveRequest.builder()
                .title("운동")
                .description("")
                .build();

        Todo todo1 = todoService.save(todoSaveRequest1);
        Todo todo2 = todoService.save(todoSaveRequest2);
        todoService.toggleCompleted(todo2.getId());
        // when
        List<Todo> todos = todoService.findAll();
        // then
        assertThat(todos).hasSize(2);
        assertThat(todos.get(0).getId()).isEqualTo(todo1.getId());
        assertThat(todos.get(0).getTitle()).isEqualTo("자바 공부");
        assertThat(todos.get(0).getDescription()).isEqualTo("컬렉션 복습하기");
        assertThat(todos.get(0).isCompleted()).isFalse();
        assertThat(todos.get(1).getId()).isEqualTo(todo2.getId());
        assertThat(todos.get(1).getTitle()).isEqualTo("운동");
        assertThat(todos.get(1).getDescription()).isEqualTo("");
        assertThat(todos.get(1).isCompleted()).isTrue();
    }

    @DisplayName("findById: 할 일 개별 조회 성공")
    @Test
    void findByIdTest() {
        // given
        TodoSaveRequest todoSaveRequest = TodoSaveRequest.builder()
                .title("자바 공부")
                .description("컬렉션 복습하기")
                .build();

        Todo todo1 = todoService.save(todoSaveRequest);
        // when
        Todo result = todoService.findById(todo1.getId());
        // then
        assertThat(result.getId()).isEqualTo(todo1.getId());
        assertThat(result.getTitle()).isEqualTo("자바 공부");
        assertThat(result.getDescription()).isEqualTo("컬렉션 복습하기");
        assertThat(result.isCompleted()).isFalse();
    }

    @DisplayName("update: 할 일 수정 성공")
    @Test
    void updateTest() {
        // given
        TodoSaveRequest todoSaveRequest = TodoSaveRequest.builder()
                .title("자바 공부")
                .description("컬렉션 복습하기")
                .build();

        Todo todo1 = todoService.save(todoSaveRequest);
        boolean completedBefore = todo1.isCompleted();

        TodoSaveRequest updateRequest = TodoSaveRequest.builder()
                .title("하네스 엔지니어링 공부")
                .description("Codex 공식 문서 정리하기")
                .build();
        // when
        Todo updateTodo1 = todoService.update(todo1.getId(), updateRequest);
        // then
        assertThat(updateTodo1.getId()).isEqualTo(todo1.getId());
        assertThat(updateTodo1.getTitle()).isEqualTo("하네스 엔지니어링 공부");
        assertThat(updateTodo1.getDescription()).isEqualTo("Codex 공식 문서 정리하기");
        assertThat(updateTodo1.isCompleted()).isEqualTo(completedBefore);
    }

    @DisplayName("toggleCompleted: 할 일 완료 상태 변경 성공")
    @Test
    void toggleCompletedTest() {
        // given
        TodoSaveRequest todoSaveRequest = TodoSaveRequest.builder()
                .title("자바 공부")
                .description("컬렉션 복습하기")
                .build();

        Todo todo1 = todoService.save(todoSaveRequest);
        // when
        Todo result = todoService.toggleCompleted(todo1.getId());
        // then
        assertThat(result.getId()).isEqualTo(todo1.getId());
        assertThat(result.getTitle()).isEqualTo("자바 공부");
        assertThat(result.getDescription()).isEqualTo("컬렉션 복습하기");
        assertThat(result.isCompleted()).isTrue();
    }

    @DisplayName("deleteById: 할 일 삭제 성공")
    @Test
    void deleteByIdTest() {
        // given
        TodoSaveRequest todoSaveRequest = TodoSaveRequest.builder()
                .title("자바 공부")
                .description("컬렉션 복습하기")
                .build();

        Todo todo1 = todoService.save(todoSaveRequest);
        Long todo1Id = todo1.getId();
        // when
        todoService.deleteById(todo1.getId());
        // then
        assertThat(todoRepository.existsById(todo1Id)).isFalse();
    }

}
