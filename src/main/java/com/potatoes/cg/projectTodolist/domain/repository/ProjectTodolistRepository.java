package com.potatoes.cg.projectTodolist.domain.repository;

import com.potatoes.cg.projectTodolist.domain.ProjectTodolist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectTodolistRepository extends JpaRepository<ProjectTodolist, Long> {

    /* 프로젝트 할일리스트 상세조회 */

    List<ProjectTodolist> findAllByProjectManager(ProjectTodolist projectTodolist);

    /* 삭제 */

}
