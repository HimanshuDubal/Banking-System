package com.banking.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.dto.AccountCreationDto;
import com.banking.model.Account;
import com.banking.model.AccountType;
import com.banking.model.User;
import com.banking.repository.AccountRepository;

@Service
@Transactional
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;
	
	public Account createAccount(User user,AccountCreationDto accountDto) {
		
		Account account = new Account();
		account.setAccountNumber(generateAccountNumber());
		account.setAccountType(accountDto.getAccountType());
		account.setUser(user);
		account.setInterestRate(getInterestRateForAccountType(accountDto.getAccountType()));
		
		if(accountDto.getAccountType() == AccountType.CURRENT) {
			account.setOverdraftLimit(new BigDecimal("10000"));
		}
		
		return accountRepository.save(account);
	}
	
	public List<Account> getUserAccounts(User user) {
		return accountRepository.findActiveAccountByUser(user);
	}
	
	public Account findByAccountNumber(String accountnumber) {
		return accountRepository.findByAccountNumber(accountnumber)
				.orElseThrow(() -> new RuntimeException("Account Not Found for this Account Number " + accountnumber));
	}
	
	public Account updateBalance(Account account, BigDecimal newBalance) {
		account.setBalance(newBalance);
		account.setLastTransactionAt(LocalDateTime.now());
		return accountRepository.save(account);
	}

	private BigDecimal getInterestRateForAccountType(AccountType accountType) {
		
		return switch (accountType) {
			case SAVINGS -> new BigDecimal("3.5");
			case CURRENT -> new BigDecimal("0.0");
			case FIXED_DEPOSIT -> new BigDecimal("6.5");
			case CREDIT -> new BigDecimal("16.5");
		};
	}

	private String generateAccountNumber() {
		
		Random random = new Random();
		StringBuilder accountNumber = new StringBuilder("ACC");
		for(int i=0;i<10;i++) {
			accountNumber.append(random.nextInt(10));
		}
		return accountNumber.toString();
	}

}
