package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    int findByUserId(int userId);
    BigDecimal getBalance(int accountId);
    void updateBalance(BigDecimal amount, int accountId);

}
