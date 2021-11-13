package com.techelevator.tenmo.Service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TellerService {

    private AccountDao accountDao;
    private TransferDao transferDao;
    private UserDao userDao;

    public TellerService(AccountDao accountDao, TransferDao transferDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
    }
    //TODO make sure to add logic for transfers that check if the balance is enough to make transfer etc.

    public BigDecimal getBalance(int accountId) {
        return accountDao.getBalance(accountId);
    }

    public void transfer(Transfer transfer) {
        int accountFrom = transfer.getAccountFrom();
        int accountTo = transfer.getAccountTo();
        BigDecimal amount = transfer.getAmount();
        BigDecimal accountFromBalance = accountDao.getBalance(accountFrom);
        BigDecimal accountToBalance = accountDao.getBalance(accountTo);
        BigDecimal zero = BigDecimal.valueOf(0);
        if(accountFromBalance.subtract(amount).compareTo(zero) >= 0) {
            transferDao.transfer(transfer);
        } else {
            transfer.setTransferStatusId(3);
            transferDao.transfer(transfer);
        }
    }

    public List<Transfer> getTransferList(int accountId) {
        return transferDao.listOfTransfers(accountId);
    }

    public Transfer getTransferDetails(int transferId) {
        return transferDao.getTransferDetails(transferId);
    }
}
