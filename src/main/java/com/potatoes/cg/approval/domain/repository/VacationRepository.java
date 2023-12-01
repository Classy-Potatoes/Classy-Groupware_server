package com.potatoes.cg.approval.domain.repository;

import com.potatoes.cg.approval.domain.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacationRepository extends JpaRepository<Vacation, Long> {
}
