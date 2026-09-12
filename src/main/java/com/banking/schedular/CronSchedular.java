package com.banking.schedular;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.banking.repository.FixedDepositRepository;
import com.banking.repository.LoanRepository;
import com.banking.service.AccountService;
import com.banking.service.EmailServiceImpl;
import com.banking.service.FixedDepositService;
import com.banking.service.LoanRepaymentCalculator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.banking.model.Account;
import com.banking.model.FixedDeposit;
import com.banking.model.FixedDepositStatus;
import com.banking.model.LoanApplication;
import com.banking.model.LoanStatus;
import com.banking.model.TransactionType;

import lombok.RequiredArgsConstructor;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class CronSchedular {

	@Autowired
	private LoanRepository loanRepository;
	
	@Autowired
	private EmailServiceImpl emailServiceImpl;
	
	@Autowired
	private FixedDepositRepository fixedDepositRepository;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private FixedDepositService fixedDepositService;
	
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
	
	@Scheduled(cron = "0 0 7 * * *")
    @Transactional
    public void processMaturedDeposits() {
        List<FixedDeposit> matured = fixedDepositRepository
                .findByStatusAndMaturityDateLessThanEqual(FixedDepositStatus.ACTIVE, LocalDate.now());
 
        for (FixedDeposit fd : matured) {
            Account linkedAccount = accountService.findByAccountNumber(fd.getLinkedAccountNumber());
 
            BigDecimal newBalance = linkedAccount.getBalance().add(fd.getMaturityAmount());
            accountService.updateBalance(linkedAccount, newBalance);
 
            fixedDepositService.recordTransaction(linkedAccount, fd.getMaturityAmount(), TransactionType.DEPOSIT,
                    "Fixed Deposit matured - " + fd.getDepositNumber(), newBalance);
 
            fd.setStatus(FixedDepositStatus.MATURED);
            fd.setMaturedAt(LocalDateTime.now());
            fixedDepositRepository.save(fd);
 
            // Programmatic eviction — see method-level note above.
            cacheManager.getCache("accounts").evict(linkedAccount.getAccountNumber());
            cacheManager.getCache("fixedDeposits").evict(fd.getDepositNumber());
            cacheManager.getCache("fixedDepositsByUser").evict(fd.getUser().getUsername());
 
            BigDecimal interestEarned = fd.getMaturityAmount().subtract(fd.getPrincipalAmount());
            String subject = "Fixed Deposit Matured - " + fd.getDepositNumber();
            String body = "<p>Dear " + fd.getUser().getFirstName() + ",</p>"
                    + "<p>Your fixed deposit has matured and the proceeds have been credited to account "
                    + fd.getLinkedAccountNumber() + ".</p>"
                    + "<p>Principal: <b>" + fd.getPrincipalAmount() + "</b></p>"
                    + "<p>Interest earned: <b>" + interestEarned + "</b></p>"
                    + "<p>Total credited: <b>" + fd.getMaturityAmount() + "</b></p>";
            emailServiceImpl.sendEmail(fd.getUser().getEmail(), subject, body);
        }
    }

}
