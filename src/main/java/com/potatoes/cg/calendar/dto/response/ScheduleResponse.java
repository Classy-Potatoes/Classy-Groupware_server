package com.potatoes.cg.calendar.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potatoes.cg.calendar.domain.type.StatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
@Getter
public class ScheduleResponse {

    private final String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime start;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime end;

    private StatusType status;

    private boolean isPersonal;

    public ScheduleResponse(String calendarTitle, LocalDateTime calendarStartedDate, LocalDateTime calendarEndDate, StatusType status, boolean isPersonal) {

        this.title = calendarTitle;
        this.start = calendarStartedDate;
        this.end = calendarEndDate;
        this.status = status;
        this.isPersonal = isPersonal;
    }


    public static ScheduleResponse from(String calendarTitle, LocalDateTime calendarStartedDate, LocalDateTime calendarEndDate, StatusType status, boolean isPersonal) {

        return new ScheduleResponse(
                calendarTitle,
                calendarStartedDate,
                calendarEndDate,
                status,
                isPersonal
        );
    }
}
