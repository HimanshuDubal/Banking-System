package com.banking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.banking.model.LoanApplication;
import com.banking.model.LoanStatus;
import com.banking.model.LoanType;

public class LoanResponse {

	private String applicationNumber;
	private String applicantUsername;
	private LoanType loanType;
	private BigDecimal requestedAmount;
	private BigDecimal interestRate;
	private Integer termInMonths;
	private String purpose;
	private BigDecimal monthlyIncome;
	private BigDecimal existingDebt;
	private LoanStatus status;
	private String managerComments;
	private LocalDateTime createdAt;
	private LocalDateTime reviewedAt;
	
	 public static LoanResponse fromEntity(LoanApplication loan) {
	        LoanResponse dto = new LoanResponse();
	        dto.applicationNumber = loan.getApplicationNumber();
	        dto.applicantUsername = loan.getUser() != null ? loan.getUser().getUsername() : null;
	        dto.loanType = loan.getLoanType();
	        dto.requestedAmount = loan.getRequestedAmount();
	        dto.interestRate = loan.getInterestRate();
	        dto.termInMonths = loan.getTermInMonths();
	        dto.purpose = loan.getPurpose();
	        dto.monthlyIncome = loan.getMonthlyIncome();
	        dto.existingDebt = loan.getExistingDebt();
	        dto.status = loan.getStatus();
	        dto.managerComments = loan.getManagerComments();
	        dto.createdAt = loan.getCreatedAt();
	        dto.reviewedAt = loan.getReviewedAt();
	        return dto;
	    }

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public String getApplicantUsername() {
		return applicantUsername;
	}

	public void setApplicantUsername(String applicantUsername) {
		this.applicantUsername = applicantUsername;
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
