package com.banking.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.dto.LoanApplicationDto;
import com.banking.dto.LoanResponse;
import com.banking.model.LoanApplication;
import com.banking.model.LoanStatus;
import com.banking.model.User;
import com.banking.service.LoanService;
import com.banking.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

	@Autowired
	private LoanService loanService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/apply")
	public ResponseEntity<?> applyForLoan(@Valid @RequestBody LoanApplicationDto loanDto, Authentication authentication){
		try {
			User user = userService.findByUsername(authentication.getName());
			LoanApplication application = loanService.submitApplication(user, loanDto);
			return ResponseEntity.ok(application);
		}catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}
	
	@GetMapping("/my-applications")
	public ResponseEntity<?> getUserApplications(Authentication authentication){
		User user = userService.findByUsername(authentication.getName());
		List<LoanResponse> applications = loanService.getLoansForUser(user.getUsername());
		return ResponseEntity.ok(applications);
	}
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('MANAGER') or hasRole('ADMIIN')")
	public ResponseEntity<?> getAllApplications(){
		List<LoanApplication> applications = loanService.getAllApplications();
		return ResponseEntity.ok(applications);
	}
	
	@PutMapping("/{applicationId}/review")
	@PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
	public ResponseEntity<?> reviewApplication(@PathVariable long applicationId, @RequestBody Map<String, String> reviewRequest){
		try {
			LoanStatus status = LoanStatus.valueOf(reviewRequest.get("status"));
			String commments = reviewRequest.get("comments");
			
			LoanApplication application = loanService.reviewApplication(applicationId, status, commments);
			return ResponseEntity.ok(LoanResponse.fromEntity(application));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

}
