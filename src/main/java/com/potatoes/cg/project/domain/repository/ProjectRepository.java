package com.potatoes.cg.project.domain.repository;

import com.potatoes.cg.project.domain.Project;
import com.potatoes.cg.project.domain.type.ProjectStatusType;
import com.potatoes.cg.project.dto.response.ProjectsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

        /* 내 부서 프로젝트 조회 */
        Page<Project> findByDeptDeptCodeAndProjectStatus(Pageable pageable, Long deptCode, ProjectStatusType projectStatus);

        /* 내가 참여한 프로젝트 조회 */
        @Query(value = "SELECT p FROM Project p " +
                "JOIN ProjectParticipant pp ON p.projectCode = pp.id.projectCode " +
                "WHERE pp.id.memberCode = :memberCode",
                countQuery = "SELECT COUNT(p) FROM Project p " +
                        "JOIN ProjectParticipant pp ON p.projectCode = pp.id.projectCode " +
                        "WHERE pp.id.memberCode = :memberCode")
        Page<Project> findMyProjects(Pageable pageable, @Param("memberCode") Long memberCode);

        /* 프로젝트 디테일 조회 */
        Optional<Project> findByProjectCodeAndProjectStatus(Long projectCode, ProjectStatusType projectStatusType);

        /* 프로젝트 수정 */
        @EntityGraph(attributePaths = {"dept"})
        Optional<Project> findByProjectCodeAndProjectStatusNot(Long projectCode, ProjectStatusType projectStatusType);

        /* 프로젝트 참여자 수 */
        @Query("SELECT COUNT(pp) FROM ProjectParticipant pp WHERE pp.project.projectCode = :projectCode")
        long countParticipantsByProjectCode(@Param("projectCode") Long projectCode);
}
