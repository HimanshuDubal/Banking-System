package com.banking.schedular;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.banking.repository.LoanRepository;
import com.banking.service.EmailServiceImpl;
import com.banking.service.LoanRepaymentCalculator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import com.banking.model.LoanApplication;
import com.banking.model.LoanStatus;

import lombok.RequiredArgsConstructor;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class CronSchedular {

	@Autowired
	private LoanRepository loanRepository;
	
	@Autowired
	private EmailServiceImpl emailServiceImpl;
	
	@Scheduled(cron = "0 0 8 * * *")
	@Transactional
	public void sendMonthlyReminders() {
		List<LoanApplication> approvedLoans = loanRepository.findByStatus(LoanStatus.APPROVED);
		
		for (LoanApplication loan : approvedLoans) {
			int remaining = LoanRepaymentCalculator.remainingMonths(loan);
			if(remaining <= 0) {
				continue;
			}
			LocalDateTime baseLine = loan.getLastReminderSentAt() != null
					? loan.getLastReminderSentAt()
							:loan.getReviewedAt();
			if(baseLine == null) {
				continue;
			}
			boolean dueForReminder = ChronoUnit.DAYS.between(baseLine, LocalDateTime.now()) >= 30;
			if(!dueForReminder) {
				continue;
			}
			BigDecimal installment = LoanRepaymentCalculator.monthlyInstallment(loan);
			BigDecimal remainingAmount = LoanRepaymentCalculator.remainingAmount(loan);
			
			String subject = "Loan Payment Reminder - " + loan.getApplicationNumber();
			String body = "<p>Dear " + loan.getUser().getFirstName() + ",</p>"
					+ "<p>This is a reminder for your " + loan.getLoanType() + " loan ("
					+ loan.getApplicationNumber() + ").</p>"
					+ "<p>Monthly installment due: <b>" + installment + "</b></p>"
					+ "<p>Months remaining: <b>" + remaining + "</b></p>"
					+ "<p>Total remaining amount: <b>" + remainingAmount + "</b></p>"
					+ "<p>Thank you for banking with us.</p>";
			
			emailServiceImpl.sendEmail(loan.getUser().getEmail(), subject, body);
			
			loan.setLastReminderSentAt(LocalDateTime.now());
			loanRepository.save(loan);
		}
	}

}
