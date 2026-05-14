package com.expense.expense_tracker.controller;

import com.expense.expense_tracker.model.Expense;
import com.expense.expense_tracker.service.ExpenseService;
import com.expense.expense_tracker.model.User;
import com.expense.expense_tracker.repository.UserRepository;
import com.expense.expense_tracker.dto.ExpenseResponseDTO;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private UserRepository userRepository;

    // Helper method to get the current authenticated user
    private User getAuthenticatedUser(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> addExpense(@Valid @RequestBody Expense expense, Principal principal) {
        User currentUser = getAuthenticatedUser(principal);
        expense.setUser(currentUser);
        
        ExpenseResponseDTO savedExpense = expenseService.addExpense(expense);
        return new ResponseEntity<>(savedExpense, HttpStatus.CREATED);
    }

    @GetMapping
    public Page<ExpenseResponseDTO> getAllExpenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal) {
            
        User currentUser = getAuthenticatedUser(principal);
        return expenseService.getAllExpenses(currentUser.getId(), PageRequest.of(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(
            @PathVariable Long id, 
            @Valid @RequestBody Expense expense, 
            Principal principal) {
            
        User currentUser = getAuthenticatedUser(principal);
        ExpenseResponseDTO updated = expenseService.updateExpense(id, currentUser.getId(), expense);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id, Principal principal) {
        User currentUser = getAuthenticatedUser(principal);
        expenseService.deleteExpense(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}
