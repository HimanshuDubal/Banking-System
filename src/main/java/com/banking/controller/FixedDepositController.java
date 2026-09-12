package com.banking.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.dto.FixedDepositRequest;
import com.banking.dto.FixedDepositResponse;
import com.banking.service.FixedDepositService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/deposits/fixed")
public class FixedDepositController {

	@Autowired
	private FixedDepositService depositService;
	
	@PostMapping
    public ResponseEntity<?> createFixedDeposit(@Valid @RequestBody FixedDepositRequest request,
                                                 Authentication authentication) {
        try {
            FixedDepositResponse response =
                    depositService.createFixedDeposit(authentication.getName(), request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
	
	@GetMapping
    public ResponseEntity<List<FixedDepositResponse>> getMyDeposits(Authentication authentication) {
        return ResponseEntity.ok(depositService.getDepositsForUser(authentication.getName()));
    }
	
	@GetMapping("/{depositNumber}")
    public ResponseEntity<?> getDepositDetails(@PathVariable String depositNumber,
                                                Authentication authentication) {
        FixedDepositResponse fd = depositService.getDepositDetails(depositNumber);
        if (!fd.getOwnerUsername().equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Access denied"));
        }
        return ResponseEntity.ok(fd);
    }
}
