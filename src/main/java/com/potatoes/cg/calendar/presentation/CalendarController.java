package com.potatoes.cg.calendar.presentation;

import com.potatoes.cg.calendar.dto.request.CalendarCreateRequest;
import com.potatoes.cg.calendar.dto.request.CalendarUpdateRequest;
import com.potatoes.cg.calendar.dto.response.ScheduleResponse;
import com.potatoes.cg.calendar.dto.response.ScheduleDetailResponse;
import com.potatoes.cg.calendar.service.CalendarService;
import com.potatoes.cg.jwt.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;

@RestController
@RequestMapping("/cg-api/v1")
@RequiredArgsConstructor
@Slf4j
public class CalendarController {

    private final CalendarService calendarService;

    /* 1. 전체 일정 조회 */
    @GetMapping("/calendar")
    public ResponseEntity<List<ScheduleResponse>> getAllSchedules(@AuthenticationPrincipal final CustomUser customUser) {

        List<ScheduleResponse> allScheduleList = calendarService.getAllSchedule(customUser);

        return ResponseEntity.status(HttpStatus.OK).body(allScheduleList);
    }


    /* 2. 개인 일정 조회 */
    @GetMapping("/calendar-personal")
    public ResponseEntity<List<ScheduleResponse>> getPersonalCalendar(@AuthenticationPrincipal final CustomUser customUser) {

        List<ScheduleResponse> personalList = calendarService.getPersonalCalendar(customUser);

        return ResponseEntity.status(HttpStatus.OK).body(personalList);
    }

    /* 3. 프로젝트 내 일정 조회 */
    @GetMapping("/calendar-project")
    public ResponseEntity<List<ScheduleResponse>> getProjectCalendar(@AuthenticationPrincipal final CustomUser customUser) {

        List<ScheduleResponse> projectList = calendarService.getProjectCalendar(customUser);

        return ResponseEntity.status(HttpStatus.OK).body(projectList);
    }

    /* 4. 개인 일정 조회 상세 */
    @GetMapping("/calendar/{calendarCode}")
    public ResponseEntity<ScheduleDetailResponse> getScheduleDetail(@PathVariable final Long calendarCode) {

        final ScheduleDetailResponse scheduleDetailResponse = calendarService.getScheduleDetail(calendarCode);

        return ResponseEntity.ok(scheduleDetailResponse);
    }

    /* 5. 개인 일정 추가 */
    @PostMapping("/calendar")
    public ResponseEntity<Void> save(@RequestBody @Valid final CalendarCreateRequest calendarRequest,
                                     @AuthenticationPrincipal final CustomUser customUser) {
        log.info("qwe : {}",calendarRequest);
        /* 응답 헤더 설정 */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        final Long calendarCode = calendarService.save(calendarRequest, customUser);

        return ResponseEntity.created(URI.create("/calendar-management/" + calendarCode)).build();
    }

    /* 6. 개인 일정 수정 */
    @PutMapping("/calendar/{calendarCode}")
    public ResponseEntity<Void> update(@PathVariable final Long calendarCode,
                                       @RequestBody @Valid final CalendarUpdateRequest calendarRequest) {

        calendarService.update(calendarCode, calendarRequest);

        return ResponseEntity.created(URI.create("/calnedar-management/" + calendarCode)).build();
    }

    /* 7. 개인 일정 삭제 */
    @DeleteMapping("/calendar/{calendarCode}")
    public ResponseEntity<Void> delete(@PathVariable final Long calendarCode) {

        calendarService.delete(calendarCode);

        return ResponseEntity.noContent().build();
    }
}
