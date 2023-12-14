package com.potatoes.cg.projectSchedule.domain.repository;

import com.potatoes.cg.calendar.domain.type.StatusType;
import com.potatoes.cg.projectSchedule.domain.ProjectSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectScheduleRepository extends JpaRepository<ProjectSchedule, Long> {

    /* 1. 프로젝트 내 일정 조회 */
    List<ProjectSchedule> findAllByScheduleStatusNotAndMemberMemberCode(StatusType statusType, Long memberCode);

    /* 2. 프로젝트 상세 조회 - 삭제 제외 */
    Optional<ProjectSchedule> findByScheduleCodeAndScheduleStatusNot(Long scheduleCode, StatusType statusType);

    /* 3. 프로젝트 일정 조회 -삭제용 */
    Optional<ProjectSchedule> findByScheduleCodeAndProjectProjectCode(Long scheduleCode, Long projectCode);

    /* 4. 프로젝트 일정 조회 */
    Page<ProjectSchedule> findByProjectProjectCodeAndScheduleStatusNotAndMemberMemberCode(Long projectCode, Pageable pageable, StatusType statusType, Long customUser);
}
