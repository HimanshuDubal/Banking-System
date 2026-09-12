package com.banking.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

import com.banking.model.LoanApplication;
import com.banking.model.LoanStatus;

public final class LoanRepaymentCalculator {

	private LoanRepaymentCalculator(){
		
	}
	
	public static int monthsElapsedSinceApproval(LoanApplication loan) {
		if(loan.getStatus() != LoanStatus.APPROVED || loan.getReviewedAt() == null) {
			return 0;
		}
		LocalDate approvedOn = loan.getReviewedAt().toLocalDate();
		
		return (int) Period.between(approvedOn, LocalDate.now()).toTotalMonths();
	}
	
	public static int remainingMonths(LoanApplication loan) {
		if(loan.getStatus() != LoanStatus.APPROVED) {
			return 0;
		}
		int remaining = loan.getTermInMonths() - monthsElapsedSinceApproval(loan);
		
		return Math.max(remaining, 0);
	}
	
	public static BigDecimal monthlyInstallment(LoanApplication loan) {
		BigDecimal principal = loan.getRequestedAmount();
		int n = loan.getTermInMonths();
		double annualRatePercent = loan.getInterestRate().doubleValue();
		double monthlyRate = annualRatePercent / 12.0 / 100.0;
		
		double emi;
		if(monthlyRate == 0.0) {
			emi = principal.doubleValue() / n;
		}else {
			double factor = Math.pow(1 + monthlyRate, n);
			emi =  principal.doubleValue() * monthlyRate * factor / (factor - 1);
		}
		
		return BigDecimal.valueOf(emi).setScale(2, RoundingMode.HALF_UP);
	}
	
	public static BigDecimal remainingAmount(LoanApplication loan) {
		int remaining = remainingMonths(loan);
		if(remaining == 0) {
			return BigDecimal.ZERO;
		}
		
		return monthlyInstallment(loan).multiply(BigDecimal.valueOf(remaining));
	}
}
