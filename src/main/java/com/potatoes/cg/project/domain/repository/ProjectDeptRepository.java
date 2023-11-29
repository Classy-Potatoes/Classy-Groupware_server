package com.potatoes.cg.project.domain.repository;

import com.potatoes.cg.member.domain.Dept;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectDeptRepository extends JpaRepository<Dept,Long> {
}
