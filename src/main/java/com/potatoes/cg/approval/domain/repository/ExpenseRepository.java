package com.potatoes.cg.approval.domain.repository;

import com.potatoes.cg.approval.domain.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
