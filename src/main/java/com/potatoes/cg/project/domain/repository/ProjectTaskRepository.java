package com.potatoes.cg.project.domain.repository;

import com.potatoes.cg.project.domain.ProjectTask;
import com.potatoes.cg.project.domain.type.ProjectStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Long> {

    /* 내 업무 조회 */
    @Query(value = "SELECT t FROM ProjectTask t " +
            "JOIN t.projectManagers pm " +
            "JOIN pm.memberCode m " +
            "WHERE m.infoCode = :memberCode",
            countQuery = "SELECT COUNT(t) FROM ProjectTask t " +
                    "JOIN t.projectManagers pm " +
                    "JOIN pm.memberCode m " +
                    "WHERE m.infoCode = :memberCode")
    Page<ProjectTask> findByMyTask(Pageable pageable, @Param("memberCode") Long infoCode);

    /* 업무 조회 */
    Page<ProjectTask> findByProjectCodeAndTaskDeleteStatus(Long projectCode, Pageable pageable, ProjectStatusType projectStatusType);
}
