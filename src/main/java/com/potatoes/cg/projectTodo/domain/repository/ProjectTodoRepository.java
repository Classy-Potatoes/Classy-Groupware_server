package com.potatoes.cg.projectTodo.domain.repository;

import com.potatoes.cg.calendar.domain.type.StatusType;
import com.potatoes.cg.projectTodo.domain.ProjectTodo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectTodoRepository extends JpaRepository<ProjectTodo, Long> {

    /* todo상세 조회 - 삭제 제외 */
    Optional<ProjectTodo> findByTodoCodeAndTodoStatusNot(Long todoCode, StatusType statusType);

    /* 삭제용 */
    Optional<ProjectTodo> findByTodoCode(Long todoCode);

    /* 할일 조회 */
    Page<ProjectTodo> findByProjectProjectCodeAndTodoStatusNotAndMemberMemberCode(Long projectCode, Pageable pageable, StatusType statusType, Long memberCode);

    Page<ProjectTodo> findByProjectProjectCodeAndTodoStatusNot(Long projectCode, Pageable pageable, StatusType statusType);


//    List<ProjectTodolist> findAllByTodoCode(List<Long> projectTodolist);
//    List<ProjectTodolist> findAllByTodoCodeIn(List<Long> projectTodolist);
}
