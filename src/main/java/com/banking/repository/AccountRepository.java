package com.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.banking.model.Account;
import com.banking.model.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{

	Optional<Account> findByAccountNumber(String accountNumber);
	
	List<Account> findByUserAndIsActiveTrue(User user);
	
	@Query("SELECT COUNT(a) FROM Account a where a.user = :user")
	long countByUser(@Param("user") User user);
	
	@Query("SELECT a FROM Account a.user = :user AND a.isActive = true")
	List<Account> findActiveAccountByUser(@Param("user") User user);
}
