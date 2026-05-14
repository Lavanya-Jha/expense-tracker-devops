package com.expense.expense_tracker.dto;

import java.time.LocalDate;

public class ExpenseResponseDTO {
    private Long id;
    private String category;
    private Double amount;
    private LocalDate date;

    public ExpenseResponseDTO(Long id, String category, Double amount, LocalDate date) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    // Getters
    public Long getId() { return id; }
    public String getCategory() { return category; }
    public Double getAmount() { return amount; }
    public LocalDate getDate() { return date; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setCategory(String category) { this.category = category; }
    public void setAmount(Double amount) { this.amount = amount; }
    public void setDate(LocalDate date) { this.date = date; }
}