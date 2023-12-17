package com.potatoes.cg.projectTodolist.domain.repository;

import com.potatoes.cg.calendar.domain.type.StatusType;
import com.potatoes.cg.projectTodolist.domain.ProjectTodolist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectTodolistRepository extends JpaRepository<ProjectTodolist, Long> {

    /* 프로젝트 할일리스트 상세조회 */

    List<ProjectTodolist> findAllByProjectManager(ProjectTodolist projectTodolist);

    /* 삭제 */
    void deleteAllByTodoCode(Long code);

    /* 할일리스트 조회 */
    List<ProjectTodolist> findAllByTodoCode(Long collect);

    Long findByTodoBody(String todoBody);

    /* 3정보 조회 */
    List<ProjectTodolist> findAllByTofoStatusAndProjectManagerMemberMemberCodeOrderByTodoListCodeDesc(StatusType statusType, Long memberCode);

    /* 내 할일 조회 */
    @Query(value = "SELECT t FROM ProjectTodolist t " +
            "JOIN t.projectManager pm " +
            "JOIN pm.member m " +
            "WHERE m.memberCode = :memberCode",
            countQuery = "SELECT COUNT(t) FROM ProjectTodolist t " +
                    "JOIN t.projectManager pm " +
                    "JOIN pm.member m " +
                    "WHERE m.memberCode = :memberCode")
    Page<ProjectTodolist> findByMyTodo(Pageable pageable, @Param("memberCode") Long memberCode);
}
