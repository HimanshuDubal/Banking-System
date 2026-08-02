package com.banking.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.TransactionStatus;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{

	@Query("SELECT t FROM Transaction t WHERE (t.fromAccount = :account OR t.toAccount = :account) ORDER BY t.createdAt DESC")
	Page<Transaction> findByAccount(@Param("account") Account account,Pageable pageable);
	
	@Query("SELECT t FROM Transaction t WHERE (t.fromAccount = :account OR t.toAccount = :account) AND t.createdAt BETWEEN :startDate AND :endDate ORDER BY t.createdAt DESC")
	List<Transaction> findByAccountAndDateRange(@Param("account") Account account,
			@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);
	
	List<Transaction> findByStatusOrderByCreatedAtDesc(TransactionStatus status);
}
