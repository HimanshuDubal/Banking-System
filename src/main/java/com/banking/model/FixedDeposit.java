package com.banking.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
import jakarta.persistence.Table;

@Entity
@Table(name = "fixed_deposits")
public class FixedDeposit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(unique = true, nullable = false)
	private String depositNumber;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(nullable = false)
	private String linkedAccountNumber;
	
	@Column(nullable = false)
	private BigDecimal principalAmount;
	
	@Column(nullable = false)
    private BigDecimal interestRate;
 
    @Column(nullable = false)
    private Integer termInMonths;
 
    @Column(nullable = false)
    private LocalDate startDate;
 
    @Column(nullable = false)
    private LocalDate maturityDate;
 
    @Column(nullable = false)
    private BigDecimal maturityAmount;
 
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FixedDepositStatus status;
 
    @Column(nullable = false)
    private LocalDateTime createdAt;
 
    private LocalDateTime maturedAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDepositNumber() {
		return depositNumber;
	}

	public void setDepositNumber(String depositNumber) {
		this.depositNumber = depositNumber;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getLinkedAccountNumber() {
		return linkedAccountNumber;
	}

	public void setLinkedAccountNumber(String linkedAccountNumber) {
		this.linkedAccountNumber = linkedAccountNumber;
	}

	public BigDecimal getPrincipalAmount() {
		return principalAmount;
	}

	public void setPrincipalAmount(BigDecimal principalAmount) {
		this.principalAmount = principalAmount;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public Integer getTermInMonths() {
		return termInMonths;
	}

	public void setTermInMonths(Integer termInMonths) {
		this.termInMonths = termInMonths;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(LocalDate maturityDate) {
		this.maturityDate = maturityDate;
	}

	public BigDecimal getMaturityAmount() {
		return maturityAmount;
	}

	public void setMaturityAmount(BigDecimal maturityAmount) {
		this.maturityAmount = maturityAmount;
	}

	public FixedDepositStatus getStatus() {
		return status;
	}

	public void setStatus(FixedDepositStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getMaturedAt() {
		return maturedAt;
	}

	public void setMaturedAt(LocalDateTime maturedAt) {
		this.maturedAt = maturedAt;
	}
    
}
