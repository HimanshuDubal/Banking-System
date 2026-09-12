package com.banking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.model.LoanApplication;
import com.banking.model.LoanStatus;
import com.banking.model.User;

@Repository
public interface LoanRepository extends JpaRepository<LoanApplication, Long>{

	List<LoanApplication> findByUserOrderByCreatedAtDesc(User user);
	
	List<LoanApplication> findByStatusOrderByCreatedAtDesc(LoanStatus status);
	
	List<LoanApplication> findAllByOrderByCreatedAtDesc();
	
	LoanApplication findByApplicationNumber(String applicationNumber);
	
	List<LoanApplication> findByUser_Username(String username);
	
	List<LoanApplication> findByStatus(LoanStatus status);
}
