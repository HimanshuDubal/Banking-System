package com.banking.dto;

import com.banking.model.AccountType;

import jakarta.validation.constraints.NotNull;

public class AccountCreationDto {

	@NotNull(message = "Account type is Required")
	private AccountType accountType;
	
	public AccountCreationDto() {
		// TODO Auto-generated constructor stub
	}

	public AccountCreationDto(AccountType accountType) {
		this.accountType = accountType;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}
	
	
}
