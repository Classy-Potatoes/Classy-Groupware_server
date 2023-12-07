package com.potatoes.cg.calendar.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potatoes.cg.calendar.domain.Calendar;
import com.potatoes.cg.calendar.domain.type.StatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ScheduleDetailResponse {

    private final Long calendarCode;
    private final String calendarTitle;
    private final String calendarContent;
    private final StatusType status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private final LocalDateTime calendarStartedDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private final LocalDateTime calendarEndDate;

    public static ScheduleDetailResponse from(Calendar calendar) {
        return new ScheduleDetailResponse(
                calendar.getCalendarCode(),
                calendar.getCalendarTitle(),
                calendar.getCalendarContent(),
                calendar.getStatus(),
                calendar.getCalendarStartedDate(),
                calendar.getCalendarEndDate()
        );
    }
}
