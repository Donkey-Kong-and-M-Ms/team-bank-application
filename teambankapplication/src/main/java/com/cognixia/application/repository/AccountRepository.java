package com.cognixia.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.cognixia.application.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
	
}
