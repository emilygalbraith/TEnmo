package com.techelevator.tenmo.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Transfer {

    @NotNull(message = "transferId cannot be null")
    private int transferId;
    @NotNull(message = "transferIdType cannot be null")
    private int transferIdType;
    @NotNull(message = "transferStatusId cannot be null")
    private int transferStatusId;
    @NotNull(message = "accountFrom cannot be null")
    private int accountFrom;
    @NotNull(message = "accountTo cannot be null")
    private int accountTo;
    @DecimalMin(value = "0.0", inclusive = true, message = "Amount cannot be below zero")
    private BigDecimal amount;

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferIdType() {
        return transferIdType;
    }

    public void setTransferIdType(int transferIdType) {
        this.transferIdType = transferIdType;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
