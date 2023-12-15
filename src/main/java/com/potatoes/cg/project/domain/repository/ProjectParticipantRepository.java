package com.potatoes.cg.project.domain.repository;


import com.potatoes.cg.project.domain.Project;
import com.potatoes.cg.project.domain.ProjectParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectParticipantRepository extends JpaRepository<ProjectParticipant, Long> {

    /* 프로젝트 내 인원 조회 */
    List<ProjectParticipant> findAllByProjectProjectCode(Long projectCode);

    List<ProjectParticipant> findParticipantsByProjectProjectCode(Long projectCode);


//    @Query("SELECT COUNT(pp) FROM ProjectParticipant pp WHERE pp.project.projectCode = :projectCode")
//    long countParticipantsByProjectCode(@Param("projectCode") Long projectCode);



}