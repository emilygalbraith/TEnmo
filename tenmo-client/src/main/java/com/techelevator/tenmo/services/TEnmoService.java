package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TEnmoService {

    private static String API_BASE_URL = "http://localhost:8080/";
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;

    public void setCurrentUser(AuthenticatedUser currentUser) { this.currentUser = currentUser; }
    public AuthenticatedUser getCurrentUser() { return currentUser; }

    public String getUsername(int accountId) {
        ResponseEntity<String> response = restTemplate.exchange(API_BASE_URL + "users/" + accountId, HttpMethod.GET,
                authEntity(), String.class);
        return response.getBody();
    }

    public int getAccountId(int userId) {
        ResponseEntity<Integer> response = restTemplate.exchange(API_BASE_URL + "accounts/" + userId,
                HttpMethod.GET, authEntity(), Integer.class);
        return response.getBody();
    }

    public BigDecimal getBalance(int accountId) {
        ResponseEntity<BigDecimal> response = restTemplate.exchange(API_BASE_URL + "accounts/balance/" + accountId,
                HttpMethod.GET, authEntity(), BigDecimal.class);
        return response.getBody();
    }

    public void transfer(Transfer transfer) {
        ResponseEntity<Void> response = restTemplate.exchange(API_BASE_URL + "transfer", HttpMethod.POST, makeTransferEntity(transfer),
                Void.class);
    }

    public List<Transfer> getTransferList(int accountId) {
        ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "transfer/list/" + accountId,
                HttpMethod.GET, authEntity(), Transfer[].class);
        Transfer[] transferArray = response.getBody();
        return Arrays.asList(transferArray);
    }

    public List<Transfer> getPendingTransferList(int accountId) {
        ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "transfers/pending/" + accountId,
                HttpMethod.GET, authEntity(), Transfer[].class);
        Transfer[] pendingTransfersArray = response.getBody();
        return Arrays.asList(pendingTransfersArray);
    }

    public Transfer getTransferDetails(int transferId) {
        ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "transfer/" + transferId, HttpMethod.GET,
                authEntity(), Transfer.class);
        return response.getBody();
    }

    public List<User> getUserList(){
        ResponseEntity<User[]> response = restTemplate.exchange(API_BASE_URL + "users", HttpMethod.GET, authEntity(),
                User[].class);
        User[] userArray = response.getBody();
        return Arrays.asList(userArray);
    }

    public void respondToRequest(Transfer transfer) {
        ResponseEntity<Void> response = restTemplate.exchange(API_BASE_URL + "transfer", HttpMethod.POST, makeTransferEntity(transfer),
                Void.class);
    }

    public void makeTransferRequest(Transfer transfer) {
        ResponseEntity<Void> response = restTemplate.exchange(API_BASE_URL + "transfer", HttpMethod.POST, makeTransferEntity(transfer),
                Void.class);
    }

    public String getTransferTypeDesc(int transferTypeId) {
        ResponseEntity<String> response = restTemplate.exchange(API_BASE_URL + "transfer_type/" + transferTypeId,
                HttpMethod.GET, authEntity(), String.class);
        return response.getBody();
    }

    public String getTransferStatusDesc(int transferStatusId){
        ResponseEntity<String> response = restTemplate.exchange(API_BASE_URL + "transfer_status/" + transferStatusId,
                HttpMethod.GET, authEntity(), String.class);
        return response.getBody();
    }


    private HttpEntity<Void> authEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }

}
