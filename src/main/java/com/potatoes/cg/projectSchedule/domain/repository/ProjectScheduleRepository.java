package com.potatoes.cg.projectSchedule.domain.repository;

import com.potatoes.cg.calendar.domain.type.StatusType;
import com.potatoes.cg.projectSchedule.domain.ProjectSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectScheduleRepository extends JpaRepository<ProjectSchedule, Long> {

    /* 1. 프로젝트 내 일정 조회 */
    List<ProjectSchedule> findAllByScheduleStatusNot(StatusType statusType);
}
