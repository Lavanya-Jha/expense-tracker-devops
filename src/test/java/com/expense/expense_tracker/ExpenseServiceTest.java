package com.expense.expense_tracker;

import com.expense.expense_tracker.dto.ExpenseResponseDTO;
import com.expense.expense_tracker.model.Expense;
import com.expense.expense_tracker.model.User;
import com.expense.expense_tracker.repository.UserRepository;
import com.expense.expense_tracker.service.ExpenseService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional // Ensures the database rolls back cleanly after every test!
public class ExpenseServiceTest {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    // This runs before every single test to ensure we have a valid user in the DB
    @BeforeEach
    public void setup() {
        testUser = new User("testadmin", "admin@test.com", "password123");
        testUser = userRepository.save(testUser);
    }

    @Test
    public void testAddExpense() {
        Expense expense = new Expense(null, "Groceries", 500.0, LocalDate.now());
        expense.setUser(testUser); // Attach the user to satisfy the DB constraint

        // Expect the DTO back, not the raw Entity
        ExpenseResponseDTO saved = expenseService.addExpense(expense);

        assertNotNull(saved.getId());
        assertEquals("Groceries", saved.getCategory());
    }

    @Test
    public void testGetAllExpenses() {
        // Pass the testUser's ID along with the pagination request
        Page<ExpenseResponseDTO> expenses = expenseService.getAllExpenses(testUser.getId(), PageRequest.of(0, 10));
        assertNotNull(expenses);
    }

    @Test
    public void testDeleteExpense() {
        Expense expense = new Expense(null, "Temp", 100.0, LocalDate.now());
        expense.setUser(testUser); 
        
        ExpenseResponseDTO saved = expenseService.addExpense(expense);
        Long id = saved.getId();

        // Pass the testUser's ID to authorize the deletion
        expenseService.deleteExpense(id, testUser.getId());

        // Verify it was deleted
        Page<ExpenseResponseDTO> all = expenseService.getAllExpenses(testUser.getId(), PageRequest.of(0, 10));
        boolean exists = all.getContent().stream().anyMatch(e -> e.getId().equals(id));
        
        assertFalse(exists);
    }
}