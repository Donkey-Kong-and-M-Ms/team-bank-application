package com.cognixia.application.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.application.model.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {

}
