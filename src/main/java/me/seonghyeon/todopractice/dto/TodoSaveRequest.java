package me.seonghyeon.todopractice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.seonghyeon.todopractice.domain.Todo;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoSaveRequest {

    @NotBlank
    @Size(max = 30)
    private String title;

    @Size(max = 1000)
    private String description;

    public Todo toEntity() {
        return Todo.builder()
                .title(title)
                .description(description)
                .build();
    }

}
