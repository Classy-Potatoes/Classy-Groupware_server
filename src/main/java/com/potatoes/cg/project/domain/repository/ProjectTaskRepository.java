package com.potatoes.cg.project.domain.repository;

import com.potatoes.cg.project.domain.ProjectTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Long> {
}
