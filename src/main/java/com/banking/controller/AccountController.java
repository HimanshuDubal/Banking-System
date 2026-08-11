package com.banking.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.dto.AccountCreationDto;
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
			User user = userService.findByUsername(authentication.getName()).orElseThrow();
			Account account = accountService.createAccount(user, accountDto);
			return ResponseEntity.ok(account);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
		}
	}
	
	@GetMapping
	public ResponseEntity<List<Account>> getUserAccounts(Authentication authentication){
		User user = userService.findByUsername(authentication.getName()).orElseThrow();
		List<Account> accounts = accountService.getUserAccounts(user);
		return ResponseEntity.ok(accounts);
	} 
	
	@GetMapping("/{accountNumber}")
	public ResponseEntity<?> getAccount(@PathVariable String accountNumber){
		try {
			Account account = accountService.findByAccountNumber(accountNumber);
			return ResponseEntity.ok(account);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

}
