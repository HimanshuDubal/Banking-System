package com.banking.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.dto.TransactionDto;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.TransactionStatus;
import com.banking.model.TransactionType;
import com.banking.repository.TransactionRepository;

@Service
@Transactional	
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private NotificationService notificationService;
	
	public Transaction processTransfer(TransactionDto transactionDto) {
		Account fromAccount = accountService.findByAccountNumber(transactionDto.getFromAccountNumber());
        Account toAccount = accountService.findByAccountNumber(transactionDto.getToAccountNumber());
        
        BigDecimal availableBalance = fromAccount.getBalance().add(fromAccount.getOverdraftLimit());
        if (availableBalance.compareTo(transactionDto.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        
        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setDescription(transactionDto.getDescription());
        transaction.setReference(transactionDto.getReference());

        try {
            BigDecimal newFromBalance = fromAccount.getBalance().subtract(transactionDto.getAmount());
            BigDecimal newToBalance = toAccount.getBalance().add(transactionDto.getAmount());

            accountService.updateBalance(fromAccount, newFromBalance);
            accountService.updateBalance(toAccount, newToBalance);

            transaction.setBalanceAfter(newFromBalance);
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction.setProcessedAt(LocalDateTime.now());

            Transaction savedTransaction = transactionRepository.save(transaction);

            notificationService.sendTransactionNotification(fromAccount.getUser(), savedTransaction, "debited");
            notificationService.sendTransactionNotification(toAccount.getUser(), savedTransaction, "credited");

            return savedTransaction;

        }catch (Exception e) {
        	 transaction.setStatus(TransactionStatus.FAILED);
             transactionRepository.save(transaction);
             throw new RuntimeException("Transaction failed: " + e.getMessage());
		}
	}
	
	public Transaction processDeposit(String accountNumber, BigDecimal amount, String description) {
        Account account = accountService.findByAccountNumber(accountNumber);

        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setToAccount(account);
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setDescription(description);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setProcessedAt(LocalDateTime.now());

        BigDecimal newBalance = account.getBalance().add(amount);
        accountService.updateBalance(account, newBalance);
        transaction.setBalanceAfter(newBalance);

        Transaction savedTransaction = transactionRepository.save(transaction);
        notificationService.sendTransactionNotification(account.getUser(), savedTransaction, "credited");

        return savedTransaction;
    }

	 public Transaction processWithdrawal(String accountNumber, BigDecimal amount, String description) {
	        Account account = accountService.findByAccountNumber(accountNumber);

	        BigDecimal availableBalance = account.getBalance().add(account.getOverdraftLimit());
	        if (availableBalance.compareTo(amount) < 0) {
	            throw new RuntimeException("Insufficient funds");
	        }

	        Transaction transaction = new Transaction();
	        transaction.setTransactionId(generateTransactionId());
	        transaction.setFromAccount(account);
	        transaction.setAmount(amount);
	        transaction.setTransactionType(TransactionType.WITHDRAWAL);
	        transaction.setDescription(description);
	        transaction.setStatus(TransactionStatus.COMPLETED);
	        transaction.setProcessedAt(LocalDateTime.now());

	        BigDecimal newBalance = account.getBalance().subtract(amount);
	        accountService.updateBalance(account, newBalance);
	        transaction.setBalanceAfter(newBalance);

	        Transaction savedTransaction = transactionRepository.save(transaction);
	        notificationService.sendTransactionNotification(account.getUser(), savedTransaction, "debited");

	        return savedTransaction;
	    }
	
	 public Page<Transaction> getAccountTransactions(String accountNumber, Pageable pageable) {
	        Account account = accountService.findByAccountNumber(accountNumber);
	        return transactionRepository.findByAccount(account, pageable);
	 }
	 
	private String generateTransactionId() {
		return "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0,10).toUpperCase();
	}

}
