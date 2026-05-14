package com.expense.expense_tracker.repository;

import com.expense.expense_tracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Page<Expense> findByUserIdAndIsDeletedFalse(long userId, Pageable pageable);
}
