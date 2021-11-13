package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    void transfer(Transfer transfer);
    List<Transfer> listOfTransfers(int accountId);
    Transfer getTransferDetails(int transferId);

}
