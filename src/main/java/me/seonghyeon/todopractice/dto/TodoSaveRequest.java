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

    @NotBlank(message = "제목은 공백이 아닌 1~30자로 입력해야 합니다.")
    @Size(max = 30, message = "제목은 공백이 아닌 1~30자로 입력해야 합니다.")
    private String title;

    @Size(max = 1000, message = "설명은 최대 1000자까지 입력할 수 있습니다")
    private String description;

    public Todo toEntity() {
        return Todo.builder()
                .title(title)
                .description(description)
                .build();
    }

}
