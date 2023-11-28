package com.potatoes.cg.calendar.domain.repository;

import com.potatoes.cg.calendar.domain.Calendar;
import com.potatoes.cg.calendar.domain.type.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    /* 1. 캘린더 상세 조회 - calendarCode로 조회, 삭제 제외 */
    Optional<Calendar> findByCalendarCodeAndStatusNot(Long calendarCode, StatusType statusType);

    /* 2. 캘린더 조회 (개인 일정) - deleted 제외 */
    List<Calendar> findAllByStatusNot(StatusType statusType);

}
