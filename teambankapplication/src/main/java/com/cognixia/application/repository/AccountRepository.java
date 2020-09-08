package com.cognixia.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.application.model.Account;

@Repository("accountRepo")
public interface AccountRepository extends JpaRepository<Account, Integer> {

}
