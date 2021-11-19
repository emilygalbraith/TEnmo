package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.Service.TellerService;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TenmoController {

    private TellerService tellerService;

    public TenmoController(TellerService tellerService){
        this.tellerService = tellerService;
    }

    @RequestMapping(path = "users/{accountId}", method = RequestMethod.GET)
    public String getUsername(@PathVariable int accountId) { return tellerService.getUsername(accountId); }

    @RequestMapping(path = "accounts/{userId}", method = RequestMethod.GET)
    public int getAccountId(@PathVariable int userId) {
        return tellerService.getAccountId(userId);
    }

    @RequestMapping(path = "accounts/balance/{accountId}", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable int accountId) {
        return tellerService.getBalance(accountId);
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @RequestMapping(path = "transfer", method = RequestMethod.POST)
    public void transfer(@RequestBody Transfer transfer) {
        tellerService.transfer(transfer);
    }

    @RequestMapping(path = "transfer/list/{accountId}", method = RequestMethod.GET)
    public List<Transfer> getTransferList(@PathVariable int accountId) {
        List<Transfer> transferList = tellerService.getTransferList(accountId);
        return transferList;
    }

    @RequestMapping(path = "transfers/pending/{accountId}", method = RequestMethod.GET)
    public List<Transfer> getPendingTransferList(@PathVariable int accountId) {
        return tellerService.getPendingTransferList(accountId);
    }

    @RequestMapping(path = "transfer/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferDetails(@PathVariable int transferId) {
        return tellerService.getTransferDetails(transferId);
    }

    @RequestMapping(path = "users", method = RequestMethod.GET)
    public List<User> getUserList() {
        return tellerService.getUserList();
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @RequestMapping(path = "transfer/respond", method = RequestMethod.POST)
    public void respondToRequest(@RequestBody Transfer transfer) {
        tellerService.respondToRequest(transfer);
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @RequestMapping(path = "transfer/request", method = RequestMethod.POST)
    public void makeTransferRequest(@RequestBody Transfer transfer) {
        tellerService.makeTransferRequest(transfer);
    }

    @RequestMapping(path = "transfer_type/{transferTypeId}", method = RequestMethod.GET)
    public String getTransferTypeDesc(@PathVariable int transferTypeId) {
        return tellerService.getTransferTypeDesc(transferTypeId);
    }

    @RequestMapping(path = "transfer_status/{transferStatusId}", method = RequestMethod.GET)
    public String getTransferStatusDesc(@PathVariable int transferStatusId) {
        return tellerService.getTransferStatusDesc(transferStatusId);
    }
}
