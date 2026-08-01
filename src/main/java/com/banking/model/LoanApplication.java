package com.banking.model;

import java.math.BigDecimal;
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
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "loan_applications")
public class LoanApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(unique = true, nullable = false)
	private String applicationNumber;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Enumerated(EnumType.STRING)
	private LoanType loanType;
	
	@DecimalMin(value = "1000.0")
	private BigDecimal requestedAmount;
	
	@DecimalMin(value = "0.0")
	private BigDecimal interestRate;
	
	@Min(value = 1)
	private Integer termInMonths;
	
	private String purpose;
	
	@DecimalMin(value = "0.0")
	private BigDecimal monthlyIncome;
	
	@DecimalMin(value = "0.0")
	private BigDecimal existingDebt;
	
	@Enumerated(EnumType.STRING)
	private LoanStatus status = LoanStatus.SUBMITTED;
	
	private String managerComments;
	private LocalDateTime createdAt = LocalDateTime.now();
	private LocalDateTime reviewedAt;
	
	public LoanApplication(){
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LoanType getLoanType() {
		return loanType;
	}

	public void setLoanType(LoanType loanType) {
		this.loanType = loanType;
	}

	public BigDecimal getRequestedAmount() {
		return requestedAmount;
	}

	public void setRequestedAmount(BigDecimal requestedAmount) {
		this.requestedAmount = requestedAmount;
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

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public BigDecimal getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(BigDecimal monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public BigDecimal getExistingDebt() {
		return existingDebt;
	}

	public void setExistingDebt(BigDecimal existingDebt) {
		this.existingDebt = existingDebt;
	}

	public LoanStatus getStatus() {
		return status;
	}

	public void setStatus(LoanStatus status) {
		this.status = status;
	}

	public String getManagerComments() {
		return managerComments;
	}

	public void setManagerComments(String managerComments) {
		this.managerComments = managerComments;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getReviewedAt() {
		return reviewedAt;
	}

	public void setReviewedAt(LocalDateTime reviewedAt) {
		this.reviewedAt = reviewedAt;
	}

	
}
