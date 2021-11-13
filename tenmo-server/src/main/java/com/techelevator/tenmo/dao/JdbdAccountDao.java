package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbdAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbdAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int findByUserId(int userId) {
        String sql = "SELECT account_id FROM accounts WHERE user_id = ?;";
        Integer accountId = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        if (accountId != null) {
            return accountId;
        } else {
            return -1;
        }
    }

    @Override
    public BigDecimal getBalance(int accountId) {
        String sql = "SELECT balance FROM accounts WHERE account_id = ?";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
        return balance;
    }

    @Override
    public void updateBalance(BigDecimal amount, int accountId) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        jdbcTemplate.update(sql, amount, accountId);
    }
}
