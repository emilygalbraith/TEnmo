package com.techelevator.tenmo.Service;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TellerService {

    private AccountDao accountDao;
    private TransferDao transferDao;
    private UserDao userDao;
    private TransferStatusDao transferStatusDao;
    private TransferTypeDao transferTypeDao;
    private static final int APPROVED_STATUS = 2;
    private static final int REJECTED_STATUS = 3;

    public TellerService(AccountDao accountDao, TransferDao transferDao, UserDao userDao, TransferTypeDao transferTypeDao,
                         TransferStatusDao transferStatusDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
        this.transferTypeDao = transferTypeDao;
        this.transferStatusDao = transferStatusDao;
    }

    public String getUsername(int accountId) { return userDao.getUsername(accountId); }

    public int getAccountId(int userId) {
        return accountDao.findByUserId(userId);
    }

    public BigDecimal getBalance(int accountId) {
        return accountDao.getBalance(accountId);
    }

    public void updateBalances(Transfer transfer) {
        int accountFrom = transfer.getAccountFrom();
        int accountTo = transfer.getAccountTo();
        BigDecimal amount = transfer.getAmount();
        BigDecimal newFromBalance = getBalance(accountFrom).subtract(amount);
        BigDecimal newToBalance = getBalance(accountTo).add(amount);
        accountDao.updateBalance(newFromBalance, accountFrom);
        accountDao.updateBalance(newToBalance, accountTo);
    }

    public void transfer(Transfer transfer) {
        boolean isSufficient = isBalanceSufficient(transfer);
        if(isSufficient){
            updateBalances(transfer);
        } else {
            transfer.setTransferStatusId(REJECTED_STATUS);
        }
        transferDao.transfer(transfer);
    }

    public List<Transfer> getTransferList(int accountId) {
        return transferDao.listOfTransfers(accountId);
    }

    public List<Transfer> getPendingTransferList(int accountId) { return transferDao.listOfPendingTransfers(accountId); }

    public Transfer getTransferDetails(int transferId) {
        return transferDao.getTransferDetails(transferId);
    }

    public List<User> getUserList() {
        return userDao.findAll();
    }

    public void respondToRequest(Transfer transfer) {
        int transferStatusId = transfer.getTransferStatusId();
        if (transferStatusId == REJECTED_STATUS) {
            transferDao.updatePendingTransfer(transfer);
        } else if (transferStatusId == APPROVED_STATUS) {
            boolean isSufficient = isBalanceSufficient(transfer);
            if(isSufficient) {
                updateBalances(transfer);
            } else {
                transfer.setTransferStatusId(REJECTED_STATUS);
            }
            transferDao.updatePendingTransfer(transfer);
        }
    }

    public void makeTransferRequest(Transfer transfer) {
        transferDao.transfer(transfer);
    }

    public boolean isBalanceSufficient(Transfer transfer) {
        int accountFrom = transfer.getAccountFrom();
        BigDecimal amount = transfer.getAmount();
        BigDecimal accountFromBalance = accountDao.getBalance(accountFrom);
        BigDecimal zero = BigDecimal.valueOf(0);
        boolean isSufficient = accountFromBalance.subtract(amount).compareTo(zero) >= 0;
        return isSufficient;
    }

    public String getTransferStatusDesc(int transferStatusId) {
        return transferStatusDao.getTransferStatusDesc(transferStatusId);
    }

    public String getTransferTypeDesc(int transferTypeId) {
        return transferTypeDao.getTransferTypeDesc(transferTypeId);
    }
}
