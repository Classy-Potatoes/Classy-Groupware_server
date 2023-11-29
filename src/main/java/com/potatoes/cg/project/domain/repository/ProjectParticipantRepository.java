package com.potatoes.cg.project.domain.repository;


import com.potatoes.cg.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectParticipantRepository extends JpaRepository<Project, Long> {

    @Query("SELECT COUNT(pp) FROM ProjectParticipant pp WHERE pp.project.projectCode = :projectCode")
    long countParticipantsByProjectCode(@Param("projectCode") Long projectCode);

}