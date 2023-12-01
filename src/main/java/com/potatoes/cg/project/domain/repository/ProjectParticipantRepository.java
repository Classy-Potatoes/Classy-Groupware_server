package com.potatoes.cg.project.domain.repository;


import com.potatoes.cg.project.domain.Project;
import com.potatoes.cg.project.domain.ProjectParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectParticipantRepository extends JpaRepository<ProjectParticipant, Long> {

//    @Query("SELECT COUNT(pp) FROM ProjectParticipant pp WHERE pp.project.projectCode = :projectCode")
//    long countParticipantsByProjectCode(@Param("projectCode") Long projectCode);



}