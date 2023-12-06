package com.potatoes.cg.projectTodo.domain.repository;

import com.potatoes.cg.calendar.domain.type.StatusType;
import com.potatoes.cg.projectTodo.domain.ProjectTodo;
import com.potatoes.cg.projectTodolist.domain.ProjectTodolist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectTodoRepository extends JpaRepository<ProjectTodo, Long> {

    /* todo상세 조회 - 삭제 제외 */
    Optional<ProjectTodo> findByTodoCodeAndTodoStatusNot(Long todoCode, StatusType statusType);

    /* 삭제용 */
    Optional<ProjectTodo> findByTodoCode(Long todoCode);


//    List<ProjectTodolist> findAllByTodoCode(List<Long> projectTodolist);
//    List<ProjectTodolist> findAllByTodoCodeIn(List<Long> projectTodolist);
}
