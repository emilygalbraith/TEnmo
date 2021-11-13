package com.techelevator.tenmo.Services;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;

public class TenmoService {

    private AccountDao accountDao;
    private TransferDao transferDao;
    private UserDao userDao;

    public TenmoService(AccountDao accountDao, TransferDao transferDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    //TODO make sure to add logic for transfers that check if the balance is enough to make transfer etc.
}
