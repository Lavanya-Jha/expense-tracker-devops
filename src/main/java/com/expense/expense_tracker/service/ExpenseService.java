package com.expense.expense_tracker.service;

import com.expense.expense_tracker.dto.ExpenseResponseDTO;
import com.expense.expense_tracker.model.Expense;
import com.expense.expense_tracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    // Helper method to convert an Entity to a DTO
    private ExpenseResponseDTO mapToDTO(Expense expense) {
        return new ExpenseResponseDTO(
                expense.getId(),
                expense.getCategory(),
                expense.getAmount(),
                expense.getDate()
        );
    }

    public ExpenseResponseDTO addExpense(Expense expense) {
        Expense saved = expenseRepository.save(expense);
        return mapToDTO(saved);
    }

    // Now uses the new repository method and maps the entire page to DTOs
    public Page<ExpenseResponseDTO> getAllExpenses(Long userId, Pageable pageable) {
        return expenseRepository.findByUserIdAndIsDeletedFalse(userId, pageable)
                .map(this::mapToDTO);
    }

    public ExpenseResponseDTO updateExpense(Long id, Long userId, Expense updatedExpense) {
        Expense existing = expenseRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Expense not found with ID: " + id));

        // SECURITY CHECK: Does this expense belong to the user trying to update it?
        if (!existing.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied: You do not own this expense");
        }

        existing.setCategory(updatedExpense.getCategory());
        existing.setAmount(updatedExpense.getAmount());
        existing.setDate(updatedExpense.getDate());
        
        return mapToDTO(expenseRepository.save(existing));
    }

    public void deleteExpense(Long id, Long userId) {
        Expense existing = expenseRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Expense not found with ID: " + id));

        // SECURITY CHECK: Does this expense belong to the user trying to delete it?
        if (!existing.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied: You do not own this expense");
        }

        existing.setDeleted(true);
        expenseRepository.save(existing);
    }
}