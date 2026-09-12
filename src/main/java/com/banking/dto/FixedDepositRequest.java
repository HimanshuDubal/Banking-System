package com.banking.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FixedDepositRequest {

	@NotBlank(message = "linkedAccountNumber is required")
    private String linkedAccountNumber;
 
    @NotNull(message = "principalAmount is required")
    @DecimalMin(value = "1000.00", message = "Minimum fixed deposit amount is 1000")
    private BigDecimal principalAmount;
 
    @NotNull(message = "termInMonths is required")
    @Min(value = 3, message = "Minimum term is 3 months")
    private Integer termInMonths;

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

	public Integer getTermInMonths() {
		return termInMonths;
	}

	public void setTermInMonths(Integer termInMonths) {
		this.termInMonths = termInMonths;
	}
 
	public FixedDepositRequest() {
		
	}
}
