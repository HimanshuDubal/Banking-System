package com.banking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.dto.LoanApplicationDto;
import com.banking.dto.LoanResponse;
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
	private EmailServiceImpl emailServiceImpl;
	
	@Autowired 
	private NotificationService notificationService;
	
	@CacheEvict(value = "loansByUser", key = "'user:' + #user.username")
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
		
		emailServiceImpl.sendEmail(user.getEmail(), 
				"Welcome to The Banking System", 
				"<h2>Welcome " + user.getUsername() + "!</h2>"
						+"<p>Your Loan Request is Submitted</p>"
						+"<p>Your Loan Details are "+ loanDto +"!</p>"
						+"<p>Please Wait while your Loan Application is Approved</p>"
						+"<p>Thank you</p>");
		
		return savedApplication;
	}
	
	public List<LoanApplication> getUserApplications(User user) {
		return loanRepository.findByUserOrderByCreatedAtDesc(user);
	}
	@Cacheable(value = "loans", key = "#applicationNumber")
	public LoanResponse getLoanDetails(String applicationNumber){
		LoanApplication loan = loanRepository.findByApplicationNumber(applicationNumber);
		return LoanResponse.fromEntity(loan);
	}
	
	@Cacheable(value = "loansByUser", key = "'user:' + #username")
	public List<LoanResponse> getLoansForUser(String username){
		List<LoanApplication> loans = loanRepository.findByUser_Username(username);
		return loans.stream().map(LoanResponse :: fromEntity).toList();
	}
	
	public List<LoanApplication> getAllApplications() {
		return loanRepository.findAllByOrderByCreatedAtDesc();
	}
	
	@Caching(evict = {
			@CacheEvict(value = "loans", allEntries = true),
			@CacheEvict(value = "loansByUser", allEntries = true)
	})
	public LoanApplication reviewApplication(long applicationId,LoanStatus status,String comments) {
		LoanApplication application = loanRepository.findById(applicationId)
				.orElseThrow(() -> new RuntimeException("Loan Application Not Found"));
		
		LoanResponse response = LoanResponse.fromEntity(application);
		
		application.setStatus(status);
		application.setManagerComments(comments);
		application.setReviewedAt(LocalDateTime.now());
		
		LoanApplication savedApplication = loanRepository.save(application);
		notificationService.sendLoanStatusNotification(application.getUser(), savedApplication);
		
		emailServiceImpl.sendEmail(application.getUser().getEmail(), 
				"Welcome to The Banking System",
				"<h2>Welcome " + application.getUser().getUsername() + "!</h2>"
						+"<p>Your Loan Request is Submitted</p>"
						+"<p>Your Loan Details are "+ response +"!</p>"
						+"<p>Your Loan Application Status is "+ savedApplication.getStatus() +"!</p>"
						+"<p>Thank you</p>");
		
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
