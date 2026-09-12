package com.banking.dto;

import java.math.BigDecimal;

import com.banking.model.LoanType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LoanApplicationDto {

	@NotNull(message = "Loan Type is Required")
	private LoanType loanType;
	
	@NotNull(message = "Requested Amount is Required")
	@DecimalMin(value = "1000.0", message = "Minimum loan amount is ₹1000")
	private BigDecimal requestedAmount;
	
	@NotNull(message = "Term in Months is Required")
	@Min(value = 1, message = "Minimum term is 1 Month")
	private Integer termInMonths;
	
	@NotBlank(message = "Purpose is Required")
	private String purpose;
	
	@NotNull(message = "Monthly Income is Required")
	@DecimalMin(value = "0.0", message = "Monthly income must be positive")
	private BigDecimal monthlyIncome;
	
	@DecimalMin(value = "0.0", message = "Existing debt must be positive")
	private BigDecimal existingDebt = BigDecimal.ZERO;
	
	private BigDecimal monthlyInstallment;
	private Integer remainingMonths;
	private BigDecimal remainingAmount;

	public LoanApplicationDto() {
		
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

	public BigDecimal getMonthlyInstallment() {
		return monthlyInstallment;
	}

	public void setMonthlyInstallment(BigDecimal monthlyInstallment) {
		this.monthlyInstallment = monthlyInstallment;
	}

	public Integer getRemainingMonths() {
		return remainingMonths;
	}

	public void setRemainingMonths(Integer remainingMonths) {
		this.remainingMonths = remainingMonths;
	}

	public BigDecimal getRemainingAmount() {
		return remainingAmount;
	}

	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}
	
	
}
