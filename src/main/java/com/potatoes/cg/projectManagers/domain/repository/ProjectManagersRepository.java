package com.potatoes.cg.projectManagers.domain.repository;

import com.potatoes.cg.projectManagers.domain.ProjectManagers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectManagersRepository extends JpaRepository<ProjectManagers, Long> {

    List<ProjectManagers> findAllByScheduleCode(Long scheduleCode);


}
