package com.cognixia.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.cognixia.application.model.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

}
