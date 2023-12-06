package com.potatoes.cg.projectSchedule.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class ProjectScheduleUpdateRequest {

    @NotBlank
    private final String scheduleTitle;

    private final String scheduleContent;

    private final List<Long> attendants;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate scheduleStartedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private final LocalTime scheduleStartedTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate scheduleEndDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private final LocalTime scheduleEndTime;
}
