package com.banking.service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.banking.dto.PendingTransferOtp;
import com.banking.dto.TransactionDto;
import com.banking.model.Transaction;
import com.banking.model.User;

@Service
public class OtpService {

	private static final Duration OTP_VALIDITY = Duration.ofMinutes(5);
	private static final String KEY_PREFIX = "otp:transfer";
	
	@Autowired
	private RedisTemplate<String, PendingTransferOtp> otpRedisTemplate;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailServiceImpl emailServiceImpl;
	
	@Autowired
	private TransactionService transactionService;
	
	private final BigDecimal otpThreshold = new BigDecimal("50000.00");
	
	private final SecureRandom secureRandom = new SecureRandom();
	
	  public boolean requiresOtp(BigDecimal amount) {
	        return amount.compareTo(otpThreshold) > 0;
	    }
	
	public String initiateTransfer(TransactionDto transactionDto, String username) {
		User user = userService.findByUsername(username);
		if(user == null) {
			throw new RuntimeException("User not found: " + username);
		}
		
		String otpReference = UUID.randomUUID().toString();
		String otpCode = String.format("%06d", secureRandom.nextInt(1_000_000));
		
		PendingTransferOtp pending = new PendingTransferOtp();
		pending.setOtpReference(otpReference);
		pending.setUsername(username);
		pending.setFromAccountNumber(transactionDto.getFromAccountNumber());
		pending.setToAccountNumber(transactionDto.getToAccountNumber());
		pending.setAmount(transactionDto.getAmount());
		pending.setDescription(transactionDto.getDescription());
		pending.setReference(transactionDto.getReference());
		pending.setOtpCode(otpCode);
		pending.setCreatedAt(LocalDateTime.now());
		
		otpRedisTemplate.opsForValue().set(KEY_PREFIX + otpReference, pending, OTP_VALIDITY);
		
		String subject = "Your OTP for a large transfer";
        String body = "<p>Dear " + user.getFirstName() + ",</p>"
                + "<p>You requested a transfer of <b>" + transactionDto.getAmount() + "</b> "
                + "from account " + transactionDto.getFromAccountNumber() + " to "
                + transactionDto.getToAccountNumber() + ".</p>"
                + "<p>Your one-time password is: <b style=\"font-size:20px\">" + otpCode + "</b></p>"
                + "<p>This code expires in 5 minutes. If you did not request this transfer, "
                + "do not share this code with anyone and contact support immediately.</p>";
        emailServiceImpl.sendEmail(user.getEmail(), subject, body);
        
        return otpReference;
	}
	
	public Transaction verifyAndCompleteTransfer(String otpReference, String otpCode, String username) {
		String key = KEY_PREFIX + otpReference;
		PendingTransferOtp pending = otpRedisTemplate.opsForValue().get(key);
		
		if(pending == null) {
			throw new RuntimeException("OTP expired or invalid reference. Please initiate the transfer again");
		}
		if(!pending.getUsername().equals(username)) {
			throw new RuntimeException("OTP expired or invalid reference. Please initiate the transfer again");
		}
		if(!pending.getOtpCode().equals(otpCode)) {
			throw new RuntimeException("Invalid OTP");
		}
		
		otpRedisTemplate.delete(key);
		
		TransactionDto transactionDto = new TransactionDto();
		
		transactionDto.setFromAccountNumber(pending.getFromAccountNumber());
		transactionDto.setToAccountNumber(pending.getToAccountNumber());
		transactionDto.setAmount(pending.getAmount());
		transactionDto.setDescription(pending.getDescription());
		transactionDto.setReference(pending.getReference());
		
		return transactionService.processTransfer(transactionDto);
	}

}
