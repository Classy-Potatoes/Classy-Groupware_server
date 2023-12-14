package com.potatoes.cg.projectManagers.domain.repository;

import com.potatoes.cg.projectManagers.domain.ProjectManagersSchedule;
import com.potatoes.cg.projectManagers.domain.ProjectManagersTodo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectManagersTodoRepository extends JpaRepository<ProjectManagersTodo, Long> {

//    List<ProjectManagersTodo> findAllByTodoList(Long todoCode);


    void deleteAllByTodoListTodoListCodeIn(List<Long> todoListCode);
}
