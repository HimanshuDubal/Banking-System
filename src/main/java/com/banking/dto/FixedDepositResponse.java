package com.banking.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.banking.model.FixedDeposit;

public class FixedDepositResponse {

	private String depositNumber;
    private String ownerUsername;
    private String linkedAccountNumber;
    private BigDecimal principalAmount;
    private BigDecimal interestRate;
    private Integer termInMonths;
    private LocalDate startDate;
    private LocalDate maturityDate;
    private BigDecimal maturityAmount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime maturedAt;
    
    public static FixedDepositResponse fromEntity(FixedDeposit fd) {
        FixedDepositResponse dto = new FixedDepositResponse();
        dto.depositNumber = fd.getDepositNumber();
        dto.ownerUsername = fd.getUser().getUsername();
        dto.linkedAccountNumber = fd.getLinkedAccountNumber();
        dto.principalAmount = fd.getPrincipalAmount();
        dto.interestRate = fd.getInterestRate();
        dto.termInMonths = fd.getTermInMonths();
        dto.startDate = fd.getStartDate();
        dto.maturityDate = fd.getMaturityDate();
        dto.maturityAmount = fd.getMaturityAmount();
        dto.status = fd.getStatus().name();
        dto.createdAt = fd.getCreatedAt();
        dto.maturedAt = fd.getMaturedAt();
        return dto;
    }

	public String getDepositNumber() {
		return depositNumber;
	}

	public void setDepositNumber(String depositNumber) {
		this.depositNumber = depositNumber;
	}

	public String getOwnerUsername() {
		return ownerUsername;
	}

	public void setOwnerUsername(String ownerUsername) {
		this.ownerUsername = ownerUsername;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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
