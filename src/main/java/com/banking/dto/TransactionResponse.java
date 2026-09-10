package com.banking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.banking.model.Transaction;
import com.banking.model.TransactionStatus;
import com.banking.model.TransactionType;

public class TransactionResponse {

	private String transactionId;
	private String fromAccountNumber;
	private String toAccountNumber;
	private BigDecimal amount;
	private TransactionType transactionType;
	private TransactionStatus status;
	private String description;
	private String reference;
	private BigDecimal balanceAfter;
	private LocalDateTime createdAt;
	private LocalDateTime processedAt;
	
	public static TransactionResponse fromEntity(Transaction txn) {
        TransactionResponse dto = new TransactionResponse();
        dto.transactionId = txn.getTransactionId();
        // fromAccount/toAccount are lazy-loaded proxies — only pull the account
        // number out of them, never let the full Account (and its User) escape.
        dto.fromAccountNumber = txn.getFromAccount() != null ? txn.getFromAccount().getAccountNumber() : null;
        dto.toAccountNumber = txn.getToAccount() != null ? txn.getToAccount().getAccountNumber() : null;
        dto.amount = txn.getAmount();
        dto.transactionType = txn.getTransactionType();
        dto.status = txn.getStatus();
        dto.description = txn.getDescription();
        dto.reference = txn.getReference();
        dto.balanceAfter = txn.getBalanceAfter();
        dto.createdAt = txn.getCreatedAt();
        dto.processedAt = txn.getProcessedAt();
        return dto;
    }

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public String getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public BigDecimal getBalanceAfter() {
		return balanceAfter;
	}

	public void setBalanceAfter(BigDecimal balanceAfter) {
		this.balanceAfter = balanceAfter;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getProcessedAt() {
		return processedAt;
	}

	public void setProcessedAt(LocalDateTime processedAt) {
		this.processedAt = processedAt;
	}
	
	

}
