package com.banking.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.banking.dto.FixedDepositRequest;
import com.banking.dto.FixedDepositResponse;
import com.banking.model.Account;
import com.banking.model.FixedDeposit;
import com.banking.model.FixedDepositStatus;
import com.banking.model.Transaction;
import com.banking.model.TransactionStatus;
import com.banking.model.TransactionType;
import com.banking.repository.FixedDepositRepository;
import com.banking.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FixedDepositService {

	@Autowired
	private FixedDepositRepository fixedDepositRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private EmailServiceImpl emailServiceImpl;
	
	@Autowired
	private CacheManager cacheManager;
	
	@CacheEvict(value = "fixedDepositsByUser", key = "#username")
	public FixedDepositResponse createFixedDeposit(String username, FixedDepositRequest depositRequest) {
		Account linkedAccount = accountService.findByAccountNumber(depositRequest.getLinkedAccountNumber());
		
		if(!linkedAccount.getUser().getUsername().equals(username)) {
			throw new RuntimeException("Account does not belongs to the Current User!");
		}
		if (linkedAccount.getBalance().compareTo(depositRequest.getPrincipalAmount()) < 0) {
            throw new RuntimeException("Insufficient balance to open this fixed deposit");
        }
		
		BigDecimal rate = getInterestRateForTerm(depositRequest.getTermInMonths());
		LocalDate startDate = LocalDate.now();
		LocalDate maturityDate = startDate.plusMonths(depositRequest.getTermInMonths());
		BigDecimal maturityAmount = calculateMaturityAmount(depositRequest.getPrincipalAmount(), rate, depositRequest.getTermInMonths());
		
		BigDecimal newBalance = linkedAccount.getBalance().subtract(depositRequest.getPrincipalAmount());
		accountService.updateBalance(linkedAccount, newBalance);
		
		FixedDeposit fd = new FixedDeposit();
		fd.setDepositNumber(generateDepositNumber());
		fd.setUser(linkedAccount.getUser());
		fd.setLinkedAccountNumber(linkedAccount.getAccountNumber());
		fd.setPrincipalAmount(depositRequest.getPrincipalAmount());
		fd.setInterestRate(rate);
		fd.setTermInMonths(depositRequest.getTermInMonths());
		fd.setStartDate(startDate);
		fd.setMaturityDate(maturityDate);
		fd.setMaturityAmount(maturityAmount);
		fd.setStatus(FixedDepositStatus.ACTIVE);
		fd.setCreatedAt(LocalDateTime.now());
		FixedDeposit savedFD = fixedDepositRepository.save(fd);
		
		 recordTransaction(linkedAccount, depositRequest.getPrincipalAmount(), TransactionType.WITHDRAWAL,
	                "Fixed Deposit opened - " + savedFD.getDepositNumber(), newBalance);
		 
		 cacheManager.getCache("accounts").evict(linkedAccount.getAccountNumber());
		 
	        String subject = "Fixed Deposit Opened - " + savedFD.getDepositNumber();
	        String body = "<p>Dear " + linkedAccount.getUser().getFirstName() + ",</p>"
	                + "<p>Your fixed deposit has been opened successfully.</p>"
	                + "<p>Principal: <b>" + depositRequest.getPrincipalAmount() + "</b></p>"
	                + "<p>Interest rate: <b>" + rate + "% p.a.</b></p>"
	                + "<p>Term: <b>" + depositRequest.getTermInMonths() + " months</b></p>"
	                + "<p>Maturity date: <b>" + maturityDate + "</b></p>"
	                + "<p>Maturity amount: <b>" + maturityAmount + "</b></p>";
	        emailServiceImpl.sendEmail(linkedAccount.getUser().getEmail(), subject, body);
	 
	        return FixedDepositResponse.fromEntity(savedFD);
	}
	
	@Cacheable(value = "fixedDeposits", key = "#depositNumber")
    public FixedDepositResponse getDepositDetails(String depositNumber) {
        FixedDeposit fd = fixedDepositRepository.findByDepositNumber(depositNumber)
                .orElseThrow(() -> new RuntimeException("Fixed deposit not found: " + depositNumber));
        return FixedDepositResponse.fromEntity(fd);
    }
 
    @Cacheable(value = "fixedDepositsByUser", key = "#username")
    public List<FixedDepositResponse> getDepositsForUser(String username) {
        return fixedDepositRepository.findByUser_UsernameOrderByCreatedAtDesc(username)
                .stream().map(FixedDepositResponse::fromEntity).toList();
    }
	
    public void recordTransaction(Account account, BigDecimal amount, TransactionType type,
            String description, BigDecimal balanceAfter) {
    		Transaction transaction = new Transaction();
    		transaction.setTransactionId("TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase());
    		
    		if (type == TransactionType.WITHDRAWAL) {
			transaction.setFromAccount(account);
			} else {
			transaction.setToAccount(account);
			}
			
    		transaction.setAmount(amount);
			transaction.setTransactionType(type);
			transaction.setDescription(description);
			transaction.setStatus(TransactionStatus.COMPLETED);
			transaction.setProcessedAt(LocalDateTime.now());
			transaction.setBalanceAfter(balanceAfter);
			transactionRepository.save(transaction);
		}
	
	 private String generateDepositNumber() {
	        return "FD" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
	    }
	 
	    // Tiered rates by term — adjust freely, these are placeholder figures.
	    private BigDecimal getInterestRateForTerm(int termInMonths) {
	        if (termInMonths < 6) return new BigDecimal("5.5");
	        if (termInMonths <= 12) return new BigDecimal("6.5");
	        if (termInMonths <= 24) return new BigDecimal("7.5");
	        return new BigDecimal("8.5");
	    }
	 
	    // Simple interest, not compound — matches the level of detail already used
	    // for loan EMI display; consistent, and transparent enough to explain in a viva.
	    private BigDecimal calculateMaturityAmount(BigDecimal principal, BigDecimal annualRate, int termInMonths) {
	        BigDecimal interest = principal.multiply(annualRate).multiply(BigDecimal.valueOf(termInMonths))
	                .divide(BigDecimal.valueOf(1200), 2, RoundingMode.HALF_UP); // 12 months x 100 (percent)
	        return principal.add(interest);
	    }
}
