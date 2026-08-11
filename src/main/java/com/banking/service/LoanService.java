package com.banking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.dto.LoanApplicationDto;
import com.banking.model.LoanApplication;
import com.banking.model.LoanStatus;
import com.banking.model.LoanType;
import com.banking.model.User;
import com.banking.repository.LoanRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Service
@Transactional
public class LoanService {

	@Autowired
	private LoanRepository loanRepository;
	
	@Autowired 
	private NotificationService notificationService;
	
	public LoanApplication submitApplication(User user,LoanApplicationDto loanDto) {
		LoanApplication application = new LoanApplication();
		application.setApplicationNumber(generateApplicationNumber());
		application.setUser(user);
		application.setLoanType(loanDto.getLoanType());
		application.setRequestedAmount(loanDto.getRequestedAmount());
		application.setTermInMonths(loanDto.getTermInMonths());
		application.setPurpose(loanDto.getPurpose());
		application.setMonthlyIncome(loanDto.getMonthlyIncome());
		application.setExistingDebt(loanDto.getExistingDebt());
		application.setInterestRate(calculateInterestRate(loanDto.getLoanType(), loanDto.getRequestedAmount()));
		
		LoanApplication savedApplication = loanRepository.save(application);
		notificationService.sendLoanApplicationNotification(user, savedApplication);
		
		return savedApplication;
	}
	
	public List<LoanApplication> getUserApplications(User user) {
		return loanRepository.findByUserOrderByCreatedAtDesc(user);
	}
	
	public List<LoanApplication> getAllApplications() {
		return loanRepository.findAllByOrderByCreatedAtDesc();
	}
	
	public LoanApplication reviewApplication(long applicationId,LoanStatus status,String comments) {
		LoanApplication application = loanRepository.findById(applicationId)
				.orElseThrow(() -> new RuntimeException("Loan Application Not Found"));
		
		application.setStatus(status);
		application.setManagerComments(comments);
		application.setReviewedAt(LocalDateTime.now());
		
		LoanApplication savedApplication = loanRepository.save(application);
		notificationService.sendLoanStatusNotification(application.getUser(), savedApplication);
		
		return savedApplication;
	}
	
	private String generateApplicationNumber() {
		return "LOAN" + UUID.randomUUID().toString().replace("-","").substring(0,8).toUpperCase();
	}
	
	private BigDecimal calculateInterestRate(LoanType loanType, BigDecimal amount) {
		BigDecimal baseRate = switch (loanType) {
		case PERSONAL -> new BigDecimal("12.5");
		case HOME -> new BigDecimal("8.5");
		case CAR -> new BigDecimal("10.0");
		case EDUCATION -> new BigDecimal("9.5");
		case BUSINESS -> new BigDecimal("11.0");
		};
		
		if(amount.compareTo(new BigDecimal("1000000")) > 0) {
			baseRate = baseRate.subtract(new BigDecimal("0.5"));
		}
		return baseRate;
	}

}
