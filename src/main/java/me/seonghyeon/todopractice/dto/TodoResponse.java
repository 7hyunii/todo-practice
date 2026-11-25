package me.seonghyeon.todopractice.dto;

import lombok.Getter;
import me.seonghyeon.todopractice.domain.Todo;

@Getter
public class TodoResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final boolean completed;

    public TodoResponse(Todo todo) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.description = todo.getDescription();
        this.completed = todo.isCompleted();
    }

}
