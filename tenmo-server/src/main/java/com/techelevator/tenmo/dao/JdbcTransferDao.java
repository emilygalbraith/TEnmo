package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate;  }

    @Override
    public void transfer(Transfer transfer) {
        int fromAccount = transfer.getAccountFrom();
        int toAccount = transfer.getAccountTo();
        int transferStatusId = transfer.getTransferStatusId();
        int transferTypeId = transfer.getTransferIdType();
        BigDecimal amount = transfer.getAmount();
        String sql = "INSERT INTO transfers VALUES (DEFAULT, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, transferTypeId, transferStatusId, fromAccount, toAccount, amount);
    }

    @Override
    public void updatePendingTransfer(Transfer transfer) {
        int transferId = transfer.getTransferId();
        int transferStatusId = transfer.getTransferStatusId();
        String sql = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?";
        jdbcTemplate.update(sql, transferStatusId, transferId);
    }

    @Override
    public List<Transfer> listOfTransfers(int accountId) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT * FROM transfers WHERE account_from = ?";
        String sql2 = "SELECT * FROM transfers WHERE account_to = ? AND transfer_type_id = 1";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        while(result.next()){
            transferList.add(mapToTransfer(result));
        }
        SqlRowSet result2 = jdbcTemplate.queryForRowSet(sql2, accountId);
        while(result2.next()) {
            transferList.add(mapToTransfer(result2));
        }
        return transferList;
    }

    @Override
    public List<Transfer> listOfPendingTransfers(int accountId) {
        List<Transfer> pendingTransferList = new ArrayList<>();
        String sql = "SELECT * FROM transfers WHERE account_from = ? AND transfer_status_id = 1";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        while(result.next()){
            pendingTransferList.add(mapToTransfer(result));
        }
        return pendingTransferList;
    }

    @Override
    public Transfer getTransferDetails(int transferId) {
        String sql = "SELECT * FROM transfers WHERE transfer_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);
        Transfer transfer = new Transfer();
        while(result.next()) {
            transfer = mapToTransfer(result);
        }
        return transfer;
    }

    private Transfer mapToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferIdType(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }
}
