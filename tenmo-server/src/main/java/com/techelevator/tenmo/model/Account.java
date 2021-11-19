package com.techelevator.tenmo.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class Account {

    @NotNull(message = "accoundId cannot be null")
    private int accountId;
    @NotNull(message = "userId cannot be null")
    private int userId;
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance cannot be below zero")
    private int balance;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
