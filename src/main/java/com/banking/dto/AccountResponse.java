package com.banking.dto;

import java.math.BigDecimal;

import com.banking.model.Account;
import com.banking.model.AccountType;

public class AccountResponse {

	private String accountNumber;
	private AccountType accountType;
	private BigDecimal balance;
	private BigDecimal interestRate;
	private BigDecimal overdraftLimit;
	private String ownerUsername;
	
	 public static AccountResponse fromEntity(Account account) {
	        AccountResponse dto = new AccountResponse();
	        dto.accountNumber = account.getAccountNumber();
	        dto.accountType = account.getAccountType();
	        dto.balance = account.getBalance();
	        dto.interestRate = account.getInterestRate();
	        dto.overdraftLimit = account.getOverdraftLimit();
	        dto.ownerUsername = account.getUser().getUsername();
	        return dto;
	    }

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public BigDecimal getOverdraftLimit() {
		return overdraftLimit;
	}

	public void setOverdraftLimit(BigDecimal overdraftLimit) {
		this.overdraftLimit = overdraftLimit;
	}

	public String getOwnerUsername() {
		return ownerUsername;
	}

	public void setOwnerUsername(String ownerUsername) {
		this.ownerUsername = ownerUsername;
	}

	 
}
