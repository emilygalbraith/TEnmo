package com.techelevator.view;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.TEnmoService;
import com.techelevator.tenmo.services.ValidaterService;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;
	private TEnmoService tenmoService;
	private ValidaterService validaterService;

	public ConsoleService(InputStream input, OutputStream output, TEnmoService tenmoService, ValidaterService validaterService) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
		this.tenmoService = tenmoService;
		this.validaterService = validaterService;
	}

	public TEnmoService getTenmoService() { return tenmoService; }

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}

	public String getUserInput(String prompt) {
		out.print(prompt+": ");
		out.flush();
		return in.nextLine();
	}

	public Integer getUserInputInteger(String prompt) {
		Integer result = null;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}

	public void displayBalance() {
		int userId = tenmoService.getCurrentUser().getUser().getId();
		int accountId = tenmoService.getAccountId(userId);
		BigDecimal accountBalance = tenmoService.getBalance(accountId);
		System.out.println("Your current account balance is: $" + accountBalance);
	}

	public void displayUserList() {
		List<User> userList = tenmoService.getUserList();
		System.out.println("-------------------------------------------");
		System.out.println("Users\nID				Name");
		System.out.println("-------------------------------------------");

		for (int i = 0; i < userList.size(); i++) {
			User user = userList.get(i);
			String userInfo = String.format("%d				%s", user.getId(), user.getUsername());
			System.out.println(userInfo);
		}
	}

	public void transferMoney() {
		int currentUserId = tenmoService.getCurrentUser().getUser().getId();
		displayUserList();
		System.out.println("Enter ID of user you are sending to (0 to cancel): ");
		String userInput = in.nextLine();
		Transfer transfer = new Transfer();
		if(!userInput.equals("0")) {
			int toUserId = Integer.parseInt(userInput);
			boolean isValid = validaterService.isValidUserId(toUserId);
			if (isValid) {
				transfer.setAccountTo(tenmoService.getAccountId(toUserId));
				transfer.setAccountFrom(tenmoService.getAccountId(currentUserId));
				System.out.println("Enter Amount:");
				String amountString = in.nextLine();
				int amount = Integer.parseInt(amountString);
				transfer.setAmount(BigDecimal.valueOf(amount));
				transfer.setTransferIdType(2);
				transfer.setTransferStatusId(2);
				tenmoService.transfer(transfer);
			} else {
				transferMoney();
			}
		}
	}

	//TODO make sure that pending request display correctly
	public void displayTransferList() {
		int currentUserId = tenmoService.getCurrentUser().getUser().getId();
		System.out.println("-------------------------------------------\n" +
				"Transfers\n" +
				"ID From/To Amount\n" +
				"-------------------------------------------\n");
		List<Transfer> transferList = tenmoService.getTransferList(currentUserId);
		for (Transfer transfer: transferList) {
			if (transfer.getTransferIdType() == 1) {
				System.out.println(String.format("%d From: %s $ %s", transfer.getTransferId(),
						tenmoService.getUsername(transfer.getAccountFrom()), transfer.getAmount().toString()));
			}
			System.out.println(String.format("%d To: %s $ %s", transfer.getTransferId(),
					tenmoService.getUsername(transfer.getAccountTo()), transfer.getAmount().toString()));
		}
	}

	public void getTransferList() {
		User user = tenmoService.getCurrentUser().getUser();
		int currentUserId = user.getId();
		displayTransferList();
		System.out.println("---------\n" + "Please enter transfer ID to view details (0 to cancel): ");
		String userInput = in.nextLine();
		if(!userInput.equals("0")) {
			int transferId = Integer.parseInt(userInput);
			boolean isContained = validaterService.isValidTransferId(currentUserId, transferId);
			Transfer transfer = tenmoService.getTransferDetails(transferId);
			if (transfer != null && isContained) {
				System.out.println("--------------------------------------------\n" +
						"Transfer Details\n" +
						"--------------------------------------------\n");
				System.out.println(String.format(" Id: %d\n" +
								" From: %s\n" +
								" To: %s\n" +
								" Type: %s\n" +
								" Status: %s\n" +
								" Amount: $%s\n----------------------", transfer.getTransferId(), user.getUsername(),
						tenmoService.getUsername(transfer.getAccountTo()), tenmoService.getTransferTypeDesc(transfer.getTransferIdType()),
						tenmoService.getTransferStatusDesc(transfer.getTransferStatusId()), transfer.getAmount().toString()));
			}
			else {
				System.out.println("You have entered an invalid transfer Id.");
				getTransferList();
			}
		}
	}

	public void makeTransferRequest() {
		int currentUserId = tenmoService.getCurrentUser().getUser().getId();
		displayUserList();
		System.out.println("Enter ID of user you are requesting from (0 to cancel): ");
		String userInput = in.nextLine();
		Transfer transfer = new Transfer();
		if(!userInput.equals("0")) {
			int fromUserId = Integer.parseInt(userInput);
			boolean isValid = validaterService.isValidUserId(fromUserId);
			if (isValid) {
				transfer.setAccountTo(tenmoService.getAccountId(currentUserId));
				transfer.setAccountFrom(tenmoService.getAccountId(fromUserId));
				System.out.println("Enter Amount:");
				String amountString = in.nextLine();
				int amount = Integer.parseInt(amountString);
				transfer.setAmount(BigDecimal.valueOf(amount));
				transfer.setTransferIdType(1);
				transfer.setTransferStatusId(1);
				tenmoService.makeTransferRequest(transfer);
			} else {
				makeTransferRequest();
			}
		}
	}

	public void displayPendingRequests() {
		int currentUserId = tenmoService.getCurrentUser().getUser().getId();
		System.out.println("-------------------------------------------\n" +
				"Pending Transfers\n" +
				"ID		To		Amount\n" +
				"-------------------------------------------\n");
		List<Transfer> pendingTransferList = tenmoService.getPendingTransferList(tenmoService.getAccountId(currentUserId));
		for (Transfer transfer: pendingTransferList) {
			System.out.println(String.format("%d		%s		$ %s", transfer.getTransferId(),
					tenmoService.getUsername(transfer.getAccountTo()), transfer.getAmount().toString()));
		}
	}

	public void respondToRequest() {
		int currentUserId = tenmoService.getCurrentUser().getUser().getId();
		displayPendingRequests();
		System.out.println("Please enter transfer ID to approve/reject (0 to cancel): ");
		String userInput = in.nextLine();
		if (!userInput.equals("0")) {
			int transferId = Integer.parseInt(userInput);
			boolean isValid = validaterService.isValidTransferId(currentUserId, transferId);
			if (isValid) {
				Transfer transfer = tenmoService.getTransferDetails(transferId);
				System.out.println("1: Approve \n2: Reject \n0: Don't approve or reject \n---------\nPlease choose an option:");
				userInput = in.nextLine();
				int userChoice = Integer.parseInt(userInput);
				if (userChoice != 0) {
					if (userChoice == 1) {
						transfer.setTransferStatusId(2);
					} else if (userChoice == 2) {
						transfer.setTransferStatusId(3);
					}
					tenmoService.transfer(transfer);
				}
			} else {
				respondToRequest();
			}
		}
	}
}
