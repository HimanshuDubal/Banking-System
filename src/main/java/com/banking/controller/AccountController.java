package com.banking.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.dto.AccountCreationDto;
import com.banking.dto.AccountResponse;
import com.banking.model.Account;
import com.banking.model.User;
import com.banking.service.AccountService;
import com.banking.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping
	public ResponseEntity<?> createAccount(@Valid @RequestBody AccountCreationDto accountDto, Authentication authentication){
		try {
			User user = userService.findByUsername(authentication.getName());
			Account account = accountService.createAccount(user, accountDto);
			return ResponseEntity.ok(account);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
		}
	}
	
	@GetMapping
	public ResponseEntity<List<AccountResponse>> getUserAccounts(Authentication authentication){
		User user = userService.findByUsername(authentication.getName());
		List<Account> accounts = accountService.getUserAccounts(user);
		List<AccountResponse> response = accounts.stream()
				.map(AccountResponse :: fromEntity)
				.toList();
		return ResponseEntity.ok(response);
	} 
	
	@GetMapping("/{accountNumber}")
	public ResponseEntity<?> getAccount(@PathVariable String accountNumber,Authentication auth){
		AccountResponse response = accountService.getAccountDetails(accountNumber);
		if(!response.getOwnerUsername().equals(auth.getName())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Access Denied"));
		}
		return ResponseEntity.ok(response);
	}

}
