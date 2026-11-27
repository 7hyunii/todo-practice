package me.seonghyeon.todopractice.exception;

public class TodoNotFoundException extends RuntimeException {

    public TodoNotFoundException() {
        super("할 일을 찾을 수 없습니다.");
    }
}
