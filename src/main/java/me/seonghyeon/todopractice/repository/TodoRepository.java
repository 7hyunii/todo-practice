package me.seonghyeon.todopractice.repository;

import me.seonghyeon.todopractice.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
