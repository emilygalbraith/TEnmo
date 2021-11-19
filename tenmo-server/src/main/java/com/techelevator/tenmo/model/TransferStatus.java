package com.techelevator.tenmo.model;

import javax.validation.constraints.NotNull;

public class TransferStatus {

    @NotNull(message = "transferStatusId cannot be null")
    private int transferStatusId;
    private String transferStatusDesc;

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public String getTransferStatusDesc() {
        return transferStatusDesc;
    }

    public void setTransferStatusDesc(String transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }
}
