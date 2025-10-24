package com.expense.expense_tracker;

import com.expense.expense_tracker.model.Expense;
import com.expense.expense_tracker.service.ExpenseService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class ExpenseServiceTest {

    @Autowired
    private ExpenseService expenseService;

    @Test
    public void testAddExpense() {
        Expense expense = new Expense(null, "Groceries", 500.0, LocalDate.now());
        Expense saved = expenseService.addExpense(expense);

        assertNotNull(saved.getId());
        assertEquals("Groceries", saved.getCategory());
    }

    @Test
    public void testGetAllExpenses() {
        List<Expense> expenses = expenseService.getAllExpenses();
        assertNotNull(expenses);
    }

    @Test
    public void testDeleteExpense() {
        Expense expense = new Expense(null, "Temp", 100.0, LocalDate.now());
        Expense saved = expenseService.addExpense(expense);
        Long id = saved.getId();

        expenseService.deleteExpense(id);

        List<Expense> all = expenseService.getAllExpenses();
        boolean exists = all.stream().anyMatch(e -> e.getId().equals(id));
        assertFalse(exists);
    }
}

