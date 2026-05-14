package com.expense.expense_tracker.repository;

import com.expense.expense_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Custom method to find a user by their username
    Optional<User> findByUsername(String username);
    
    // Custom method to check if an email is already registered
    Boolean existsByEmail(String email);
}