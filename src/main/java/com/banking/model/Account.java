package com.banking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;

@Entity
@Table(name = "accounts")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true, nullable = false)
	private String accountNumber;
	
	@Enumerated(EnumType.STRING)
	private AccountType accountType;
	
	@DecimalMin(value = "0.0",inclusive = true)
	private BigDecimal balance = BigDecimal.ZERO;
	
	@DecimalMin(value = "0.0")
	private BigDecimal interestRate;

	@DecimalMin(value = "0.0")
	private BigDecimal overdraftLimit = BigDecimal.ZERO;
	
	private boolean isActive = true;
	private LocalDateTime createdAt = LocalDateTime.now();
	private LocalDateTime lastTransactionAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id",nullable = false)
	private User user;
	
	@OneToMany(mappedBy = "fromAccount",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private Set<Transaction> outgoingTransactions;
	
	@OneToMany(mappedBy = "toAccount",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private Set<Transaction> incomingTransactions;
	
	 public Account() {
	 }

	 public Account(String accountNumber, AccountType accountType, User user, BigDecimal interestRate) {
		 this.accountNumber = accountNumber;
	     this.accountType = accountType;
	     this.user = user;
	     this.interestRate = interestRate;
	  }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getLastTransactionAt() {
		return lastTransactionAt;
	}

	public void setLastTransactionAt(LocalDateTime lastTransactionAt) {
		this.lastTransactionAt = lastTransactionAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Transaction> getOutgoingTransactions() {
		return outgoingTransactions;
	}

	public void setOutgoingTransactions(Set<Transaction> outgoingTransactions) {
		this.outgoingTransactions = outgoingTransactions;
	}

	public Set<Transaction> getIncomingTransactions() {
		return incomingTransactions;
	}

	public void setIncomingTransactions(Set<Transaction> incomingTransactions) {
		this.incomingTransactions = incomingTransactions;
	}
	 
	 
}
