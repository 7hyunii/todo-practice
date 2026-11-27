package me.seonghyeon.todopractice.service;

import me.seonghyeon.todopractice.domain.Todo;
import me.seonghyeon.todopractice.dto.TodoResponse;
import me.seonghyeon.todopractice.dto.TodoSaveRequest;
import me.seonghyeon.todopractice.exception.TodoNotFoundException;
import me.seonghyeon.todopractice.repository.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

// 단위 테스트
@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Captor
    private ArgumentCaptor<Todo> todoCaptor;

    @DisplayName("할 일 등록 성공")
    @Test
    void saveTest() {
        // given
        TodoSaveRequest request = new TodoSaveRequest("자바 공부", "컬렉션 복습하기");
        Todo savedTodo = new Todo("자바 공부", "컬렉션 복습하기");
        ReflectionTestUtils.setField(savedTodo, "id", 1L);

        given(todoRepository.save(any(Todo.class)))
                .willReturn(savedTodo);
        // when
        TodoResponse response = todoService.save(request);
        // then
        // Response DTO 검증
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("자바 공부");
        assertThat(response.getDescription()).isEqualTo("컬렉션 복습하기");
        assertThat(response.isCompleted()).isFalse();
        // Repository에 전달된 Entity 검증
        verify(todoRepository).save(todoCaptor.capture());
        Todo capturedTodo = todoCaptor.getValue();

        assertThat(capturedTodo.getTitle()).isEqualTo("자바 공부");
        assertThat(capturedTodo.getDescription()).isEqualTo("컬렉션 복습하기");
        assertThat(capturedTodo.isCompleted()).isFalse();
    }

    @DisplayName("할 일 목록 전체 조회 성공")
    @Test
    void findAllTest() {
        // given
        Todo todo1 = new Todo("자바 공부", "컬렉션 복습하기");
        Todo todo2 = new Todo("운동", "");
        ReflectionTestUtils.setField(todo1, "id", 1L);
        ReflectionTestUtils.setField(todo2, "id", 2L);
        todo2.toggleCompleted();

        given(todoRepository.findAll(Sort.by("id")))
                .willReturn(List.of(todo1, todo2));
        // when
        List<TodoResponse> todos = todoService.findAll();
        // then
        assertThat(todos)
                .extracting(
                    TodoResponse::getId,
                    TodoResponse::getTitle,
                    TodoResponse::getDescription,
                    TodoResponse::isCompleted
                )
                .containsExactly(
                        tuple(1L, "자바 공부", "컬렉션 복습하기", false),
                        tuple(2L, "운동", "", true)
                );
    }

    @DisplayName("할 일 개별 조회 성공")
    @Test
    void findByIdTest() {
        // given
        Todo todo1 = new Todo("자바 공부", "컬렉션 복습하기");
        ReflectionTestUtils.setField(todo1, "id", 1L);

        given(todoRepository.findById(1L))
                .willReturn(Optional.of(todo1));
        // when
        TodoResponse response = todoService.findById(1L);
        // then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("자바 공부");
        assertThat(response.getDescription()).isEqualTo("컬렉션 복습하기");
        assertThat(response.isCompleted()).isFalse();
    }

    @DisplayName("할 일 개별 조회 실패(존재하지 않는 ID)")
    @Test
    void findByIdWithMissingIdTest() {
        // given
        given(todoRepository.findById(999L))
                .willReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> todoService.findById(999L))
                .isInstanceOf(TodoNotFoundException.class)
                .hasMessage("할 일을 찾을 수 없습니다.");
    }

    @DisplayName("할 일 수정 성공")
    @Test
    void updateTest() {
        // given
        Todo todo1 = new Todo("자바 공부", "컬렉션 복습하기");
        todo1.toggleCompleted();
        ReflectionTestUtils.setField(todo1, "id", 1L);

        TodoSaveRequest updateRequest = new TodoSaveRequest("하네스 엔지니어링 공부", "Codex 공식 문서 정리하기");

        given(todoRepository.findById(1L))
                .willReturn(Optional.of(todo1));
        // when
        TodoResponse response = todoService.update(1L, updateRequest);
        // then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("하네스 엔지니어링 공부");
        assertThat(response.getDescription()).isEqualTo("Codex 공식 문서 정리하기");
        assertThat(response.isCompleted()).isTrue();
            // todo1 자체가 바뀌었는지 확인
        assertThat(todo1.getTitle()).isEqualTo(updateRequest.getTitle());
        assertThat(todo1.getDescription()).isEqualTo(updateRequest.getDescription());
    }

    @DisplayName("할 일 수정 실패(존재하지 않는 ID)")
    @Test
    void updateWithMissingIdTest() {
        // given
        TodoSaveRequest updateRequest = new TodoSaveRequest("하네스 엔지니어링 공부", "Codex 공식 문서 정리하기");

        given(todoRepository.findById(999L))
                .willReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> todoService.update(999L, updateRequest))
                .isInstanceOf(TodoNotFoundException.class)
                .hasMessage("할 일을 찾을 수 없습니다.");
    }

    @DisplayName("할 일 완료 상태 변경 성공")
    @Test
    void toggleCompletedTest() {
        // given
        Todo todo1 = new Todo("자바 공부", "컬렉션 복습하기");
        ReflectionTestUtils.setField(todo1, "id", 1L);

        given(todoRepository.findById(1L))
                .willReturn(Optional.of(todo1));
        // when
        TodoResponse firstResponse = todoService.toggleCompleted(1L);
        TodoResponse secondResponse = todoService.toggleCompleted(1L);
        // then
        assertThat(firstResponse.isCompleted()).isTrue();
        assertThat(secondResponse.isCompleted()).isFalse();
    }

    @DisplayName("할 일 삭제 성공")
    @Test
    void deleteByIdTest() {
        // given
        Todo todo1 = new Todo("자바 공부", "컬렉션 복습하기");

        given(todoRepository.findById(1L))
                .willReturn(Optional.of(todo1));
        // when
        todoService.deleteById(1L);
        // then
        verify(todoRepository).delete(todo1);
    }

}
