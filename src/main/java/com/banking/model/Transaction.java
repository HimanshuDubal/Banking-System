package com.banking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;

@Entity
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(unique = true,nullable = false)
	private String transactionId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_account_id")
	private Account fromAccount;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_account_id")
	private Account toAccount;
	
	@DecimalMin(value = "0.01")
	private BigDecimal amount;
	
	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;
	
	@Enumerated(EnumType.STRING)
	private TransactionStatus status = TransactionStatus.PENDING;
	
	private String description;
	private String reference;
	private BigDecimal balanceAfter;
	
	private LocalDateTime createdAt = LocalDateTime.now();
	private LocalDateTime processedAt;
	
	 public Transaction() {
	 }

	 public Transaction(String transactionId, Account fromAccount, Account toAccount,
	            BigDecimal amount, TransactionType transactionType, String description) {
        this.transactionId = transactionId;
        this.fromAccount = fromAccount;
	    this.toAccount = toAccount;
        this.amount = amount;
        this.transactionType = transactionType;
        this.description = description;
	 }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Account getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(Account fromAccount) {
		this.fromAccount = fromAccount;
	}

	public Account getToAccount() {
		return toAccount;
	}

	public void setToAccount(Account toAccount) {
		this.toAccount = toAccount;
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
