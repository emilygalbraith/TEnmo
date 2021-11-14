package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidaterService {

    public boolean isValidUserId(int userId, TEnmoService tenmoService) {
        List<User> userList = tenmoService.getUserList();
        boolean isValid = false;
        for (User user : userList) {
            if (user.getId() == userId) {
                isValid = true;
            }
        }
        return isValid;
    }

    public boolean isValidTransferId(int userId, int transferId, TEnmoService tenmoService) {
        List<Transfer> usersTransferList = tenmoService.getTransferList(tenmoService.getAccountId(userId));
        boolean isContained = false;
        for (Transfer transfer: usersTransferList) {
            if (transfer.getTransferId() == transferId) {
                isContained = true;
            }
        }
        return isContained;
    }


}
