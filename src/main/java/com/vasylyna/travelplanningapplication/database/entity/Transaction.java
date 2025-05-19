package com.vasylyna.travelplanningapplication.database.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private double amount;
    private String category;
    private String description;
    private LocalDateTime date;
    private String transactionType;

    public Transaction(int id, double amount, String category, String description, LocalDateTime date, String transactionType) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
        this.transactionType = transactionType;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public int getId() {
        return id;
    }
}
