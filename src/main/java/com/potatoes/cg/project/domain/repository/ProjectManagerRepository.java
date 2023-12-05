package com.potatoes.cg.project.domain.repository;

import com.potatoes.cg.project.domain.ProjectManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectManagerRepository extends JpaRepository<ProjectManager, Long> {
}
