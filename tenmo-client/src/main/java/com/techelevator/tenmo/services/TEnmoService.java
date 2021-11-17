package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
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
        String userName = "";
        try {
            ResponseEntity<String> response = restTemplate.exchange(API_BASE_URL + "users/" + accountId, HttpMethod.GET,
                    authEntity(), String.class);
            userName = response.getBody();
        } catch(RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return userName;
    }

    public int getAccountId(int userId) {
        int accountId = 0;
        try {
            ResponseEntity<Integer> response = restTemplate.exchange(API_BASE_URL + "accounts/" + userId,
                    HttpMethod.GET, authEntity(), Integer.class);
            accountId = response.getBody();
        } catch(RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return accountId;
    }

    public BigDecimal getBalance(int accountId) {
        BigDecimal balance = new BigDecimal(0);
        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(API_BASE_URL + "accounts/balance/" + accountId,
                    HttpMethod.GET, authEntity(), BigDecimal.class);
            balance = response.getBody();
        } catch(RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return balance;
    }

    public void transfer(Transfer transfer) {
        try {
            ResponseEntity<Void> response = restTemplate.exchange(API_BASE_URL + "transfer", HttpMethod.POST, makeTransferEntity(transfer),
                    Void.class);
        } catch(RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Transfer> getTransferList(int accountId) {
        Transfer[] transferArray = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "transfer/list/" + accountId,
                    HttpMethod.GET, authEntity(), Transfer[].class);
            transferArray = response.getBody();
        } catch(RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return Arrays.asList(transferArray);
    }

    public List<Transfer> getPendingTransferList(int accountId) {
        Transfer[] pendingTransfersArray = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "transfers/pending/" + accountId,
                    HttpMethod.GET, authEntity(), Transfer[].class);
            pendingTransfersArray = response.getBody();
        } catch(RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return Arrays.asList(pendingTransfersArray);
    }

    public Transfer getTransferDetails(int transferId) {
        Transfer transfer = new Transfer();
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "transfer/" + transferId, HttpMethod.GET,
                    authEntity(), Transfer.class);
            transfer = response.getBody();
        } catch(RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return transfer;
    }

    public List<User> getUserList(){
        User[] userArray = null;
        try {
            ResponseEntity<User[]> response = restTemplate.exchange(API_BASE_URL + "users", HttpMethod.GET, authEntity(),
                    User[].class);
            userArray = response.getBody();
        } catch(RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return Arrays.asList(userArray);
    }

    public void respondToRequest(Transfer transfer) {
        try {
            ResponseEntity<Void> response = restTemplate.exchange(API_BASE_URL + "transfer", HttpMethod.POST, makeTransferEntity(transfer),
                    Void.class);

        } catch(RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void makeTransferRequest(Transfer transfer) {
        try {
            ResponseEntity<Void> response = restTemplate.exchange(API_BASE_URL + "transfer", HttpMethod.POST, makeTransferEntity(transfer),
                    Void.class);
        } catch(RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getTransferTypeDesc(int transferTypeId) {
        String transferTypeDesc = "";
        try {
            ResponseEntity<String> response = restTemplate.exchange(API_BASE_URL + "transfer_type/" + transferTypeId,
                    HttpMethod.GET, authEntity(), String.class);
            transferTypeDesc = response.getBody();
        } catch(RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return transferTypeDesc;
    }

    public String getTransferStatusDesc(int transferStatusId){
        String transferStatusDesc = "";
        try {
            ResponseEntity<String> response = restTemplate.exchange(API_BASE_URL + "transfer_status/" + transferStatusId,
                    HttpMethod.GET, authEntity(), String.class);
            transferStatusDesc = response.getBody();
        } catch(RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return transferStatusDesc;
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
