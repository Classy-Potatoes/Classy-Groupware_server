package com.potatoes.cg.calendar.service;

import com.potatoes.cg.calendar.common.exception.NotFoundException;
import com.potatoes.cg.calendar.domain.Calendar;
import com.potatoes.cg.calendar.domain.repository.CalendarRepository;
import com.potatoes.cg.calendar.dto.request.CalendarCreateRequest;
import com.potatoes.cg.calendar.dto.request.CalendarUpdateRequest;
import com.potatoes.cg.calendar.dto.response.ScheduleResponse;
import com.potatoes.cg.calendar.dto.response.ScheduleDetailResponse;
import com.potatoes.cg.projectSchedule.domain.ProjectSchedule;
import com.potatoes.cg.projectSchedule.domain.repository.ProjectScheduleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.potatoes.cg.calendar.common.exception.type.ExceptionCode.*;
import static com.potatoes.cg.calendar.domain.type.StatusType.DELETED;

@Service
@Transactional
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;

    private final ProjectScheduleRepository projectScheduleRepository;

    /* 1. 전체 일정 조회 */
    @Transactional(readOnly = true)
    public List<ScheduleResponse> getAllSchedule(Integer memberCode) {

        List<ScheduleResponse> allSchedules = new ArrayList<>();

        List<ScheduleResponse> personalSchedules = getPersonalCalendar(memberCode);
        allSchedules.addAll(personalSchedules);

        List<ScheduleResponse> projectSchedules = getProjectCalendar(memberCode);
        allSchedules.addAll(projectSchedules);

        return allSchedules;
    }

    /* 2. 개인 일정 조회 */
    @Transactional(readOnly = true)
    public List<ScheduleResponse> getPersonalCalendar(Integer memberCode) {

        List<Calendar> calendarList = calendarRepository.findAllByStatusNot(DELETED);

        List<ScheduleResponse> scheduleResponseList = calendarList.stream()
                .map(calendar -> ScheduleResponse.from(
                        calendar.getCalendarTitle(),
                        calendar.getCalendarStartedDate(),
                        calendar.getCalendarEndDate(),
                        calendar.getStatus(),
                        true
                ))
                .collect(Collectors.toList());

        return scheduleResponseList;
    }

    /* 3. 프로젝트 내 일정 조회 */
    @Transactional(readOnly = true)
    public List<ScheduleResponse> getProjectCalendar(Integer memberCode) {

        List<ProjectSchedule> calendarList = projectScheduleRepository.findAllByScheduleStatusNot(DELETED);

        List<ScheduleResponse> scheduleResponseList = calendarList.stream()
                .map(calendar -> ScheduleResponse.from(
                        calendar.getScheduleTitle(),
                        calendar.getScheduleStartDate(),
                        calendar.getScheduleEndDate(),
                        calendar.getScheduleStatus(),
                        false
                ))
                .collect(Collectors.toList());

        return scheduleResponseList;
    }

    /* 4. 개인 일정 조회 상세 */
    @Transactional(readOnly = true)
    public ScheduleDetailResponse getScheduleDetail(Long calendarCode) {

        Calendar calendar = calendarRepository.findByCalendarCodeAndStatusNot(calendarCode, DELETED)
                    .orElseThrow(() -> new NotFoundException(NOT_FOUND_CALENDAR_CODE));

        return ScheduleDetailResponse.from(calendar);
    }

    /* 5. 개인 일정 추가 */
    public Long save(CalendarCreateRequest calendarRequest, Integer memberCode) {

        String calendarTitle = calendarRequest.getCalendarTitle();
        LocalDateTime calendarStartedDate = calendarRequest.getCalendarStartedDate();
        LocalDateTime calendarEndDate = calendarRequest.getCalendarEndDate();

        if (calendarTitle == null || calendarTitle.isEmpty()) {
            throw new NotFoundException(NOT_FOUND_VALID_TITLE);
        }

        if (calendarStartedDate == null || calendarEndDate == null) {
            throw new NotFoundException(NOT_FOUND_VALID_DATE);
        }

        final Calendar newCalendar = Calendar.of(
                calendarTitle,
                calendarRequest.getCalendarContent(),
                calendarStartedDate,
                calendarEndDate,
                memberCode
        );

        final Calendar calendar = calendarRepository.save(newCalendar);

        return calendar.getCalendarCode();
    }

    /* 6. 개인 일정 수정 */
    public void update(Long calendarCode, CalendarUpdateRequest calendarRequest) {

        Calendar calendar = calendarRepository.findByCalendarCodeAndStatusNot(calendarCode, DELETED)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CALENDAR_CODE));

        calendar.update(
                calendarRequest.getCalendarTitle(),
                calendarRequest.getCalendarContent(),
                calendarRequest.getCalendarStartedDate(),
                calendarRequest.getCalendarEndDate()
        );
    }

    /* 7. 개인 일정 삭제 */
    public void delete(Long calendarCode) {

        calendarRepository.deleteById(calendarCode);
    }

}
