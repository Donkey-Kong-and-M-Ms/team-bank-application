package com.cognixia.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.application.model.Account;

@Repository("accountRepo")
public interface AccountRepository extends JpaRepository<Account, Integer> {
	Optional<Account> findByAccountType(String accountType);

	Optional<Account> findByAccountTypeAndUserUserId(String accountType, int userid);
}
