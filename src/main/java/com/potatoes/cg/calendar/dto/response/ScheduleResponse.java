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

    private final Long calendarCode;

    private final String title;

    private final String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime start;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime end;

    private StatusType status;

    private boolean isPersonal;

    public ScheduleResponse(Long calendarCode,String calendarTitle, String calendarContent, LocalDateTime calendarStartedDate, LocalDateTime calendarEndDate, StatusType status, boolean isPersonal) {

        this.calendarCode = calendarCode;
        this.title = calendarTitle;
        this.content = calendarContent;
        this.start = calendarStartedDate;
        this.end = calendarEndDate;
        this.status = status;
        this.isPersonal = isPersonal;
    }


    public static ScheduleResponse from(Long calendarCode, String calendarTitle, String calendarContent, LocalDateTime calendarStartedDate, LocalDateTime calendarEndDate, StatusType status, boolean isPersonal) {

        return new ScheduleResponse(
                calendarCode,
                calendarTitle,
                calendarContent,
                calendarStartedDate,
                calendarEndDate,
                status,
                isPersonal
        );
    }
}
