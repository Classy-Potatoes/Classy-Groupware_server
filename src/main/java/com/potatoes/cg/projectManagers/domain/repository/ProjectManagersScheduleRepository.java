package com.potatoes.cg.projectManagers.domain.repository;

import com.potatoes.cg.projectManagers.domain.ProjectManagersSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectManagersScheduleRepository extends JpaRepository<ProjectManagersSchedule, Long> {

    List<ProjectManagersSchedule> findAllByScheduleCode(Long scheduleCode);

    void deleteAllByScheduleCode(Long code);
}
