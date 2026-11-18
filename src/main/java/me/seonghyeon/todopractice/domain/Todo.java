package me.seonghyeon.todopractice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)      // JPA가 사용할 기본 생성자 생성
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false, length = 30)
    private String title;

    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @Column(name = "completed")
    private boolean completed;      // 기본값 false

    // 할 일 생성
    @Builder
    public Todo(String title, String description) {
        this.title = title;
        this.description = description == null ? "" : description;
    }

    // 할 일 수정
    public void update(String title, String description) {
        this.title = title;
        this.description = description == null ? "" : description;
    }

    // 할 일 완료 상태 변경
    public void toggleCompleted() {
        this.completed = !this.completed;
    }

}
