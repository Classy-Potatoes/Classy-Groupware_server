package com.potatoes.cg.projectTodo.presentation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class TodoListResponse {

    private Long todoListCode;
    private String todoBody;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public TodoListResponse(Long todoListCode, String todoBody, LocalDate endDate) {
        this.todoListCode = todoListCode;
        this.todoBody = todoBody;
        this.endDate = endDate;
    }

    public static TodoListResponse from(Long todoListCode, String todoBody, LocalDate todoEndDate) {

        return new TodoListResponse(
                todoListCode,
                todoBody,
                todoEndDate
        );
    }
}
