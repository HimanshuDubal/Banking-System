package com.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.model.FixedDeposit;
import com.banking.model.FixedDepositStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface FixedDepositRepository extends JpaRepository<FixedDeposit, Long>{

	Optional<FixedDeposit> findByDepositNumber(String depositNumber);
	
	List<FixedDeposit> findByUser_UsernameOrderByCreatedAtDesc(String username);
	
	List<FixedDeposit> findByStatusAndMaturityDateLessThanEqual(FixedDepositStatus status, LocalDate date);
}
