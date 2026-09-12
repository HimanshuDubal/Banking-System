package com.banking.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banking.dto.TransactionDto;
import com.banking.dto.TransactionResponse;
import com.banking.model.Transaction;
import com.banking.service.OtpService;
import com.banking.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private OtpService otpService;

	@PostMapping("/transfer")
	public ResponseEntity<?> transfer(@RequestBody TransactionDto dto, Authentication authentication) {
	    if (otpService.requiresOtp(dto.getAmount())) {
	        String otpReference = otpService.initiateTransfer(dto, authentication.getName());
	        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of(
	                "otpRequired", true,
	                "otpReference", otpReference,
	                "message", "An OTP has been sent to your registered email. Confirm it to complete this transfer."
	        ));
	    }
	    Transaction transaction = transactionService.processTransfer(dto);
	    return ResponseEntity.ok(TransactionResponse.fromEntity(transaction));
	}
	
	@PostMapping("/transfer/verify-otp")
	public ResponseEntity<?> verifyTransferOtp(@RequestBody Map<String, String> body, Authentication authentication) {
		Transaction transaction = otpService.verifyAndCompleteTransfer(
				body.get("otpReference"), body.get("otpCode"), authentication.getName());
		return ResponseEntity.ok(TransactionResponse.fromEntity(transaction));
	}
	
	@PostMapping("/deposit")
	public  ResponseEntity<?> deposit(@RequestBody Map<String, Object> depositRequest){
		try {
			String accountNumber = (String) depositRequest.get("accountNumber");
			BigDecimal amount = new BigDecimal(depositRequest.get("amount").toString());
			String description = (String) depositRequest.get("description");
			
			Transaction transaction = transactionService.processDeposit(accountNumber, amount, description);
			return ResponseEntity.ok(TransactionResponse.fromEntity(transaction));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}
	
	@PostMapping("/withdraw")
	public ResponseEntity<?> withdraw(@RequestBody Map<String, Object> withdrawRequest){
		try {
			String accountNumber = (String) withdrawRequest.get("accountNumber");
			BigDecimal amount = new BigDecimal(withdrawRequest.get("amount").toString());
			String description = (String) withdrawRequest.get("description");
			
			Transaction transaction = transactionService.processWithdrawal(accountNumber, amount, description);
			return ResponseEntity.ok(TransactionResponse.fromEntity(transaction));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}
	
	@GetMapping("/account/{accountNumber}")
    public ResponseEntity<Page<TransactionResponse>> getAccountTransactions(
            @PathVariable String accountNumber, Pageable pageable,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Transaction> transactions = transactionService.getAccountTransactions(accountNumber, pageable);
        Page<TransactionResponse> response = transactions.map(TransactionResponse :: fromEntity);
        return ResponseEntity.ok(response);
    }
}
