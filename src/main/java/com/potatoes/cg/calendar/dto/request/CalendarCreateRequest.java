package com.potatoes.cg.calendar.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class CalendarCreateRequest {

    @NotBlank
    private final String calendarTitle;

    private final String calendarContent;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime calendarStartedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime calendarEndDate;
}
