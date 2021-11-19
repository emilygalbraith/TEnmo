package com.techelevator.tenmo.model;

import javax.validation.constraints.NotNull;

public class TransferType {

    @NotNull(message = "transferTypeId cannot be null")
    private int transferTypeId;
    private String transferTypeDesc;

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public String getTransferTypeDesc() {
        return transferTypeDesc;
    }

    public void setTransferTypeDesc(String transferTypeDesc) {
        this.transferTypeDesc = transferTypeDesc;
    }
}
