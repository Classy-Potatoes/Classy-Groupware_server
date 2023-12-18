package com.potatoes.cg.projectTodolist.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class ProjectTodolistCreateRequest {

    private final String todoBody;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate endDates;

    private final Long attendant;
}
