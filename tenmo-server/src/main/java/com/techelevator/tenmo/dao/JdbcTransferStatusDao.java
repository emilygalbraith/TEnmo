package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferStatusDao implements TransferStatusDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferStatusDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String getTransferStatusDesc(int transferStatusId) {
        String sql = "SELECT transfer_status_desc FROM transfer_types WHERE transfer_status_id = ?";
        String transferStatusDesc = jdbcTemplate.queryForObject(sql, String.class, transferStatusId);
        return transferStatusDesc;
    }
}
