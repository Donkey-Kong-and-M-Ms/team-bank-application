package com.cognixia.application.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.application.model.Transaction;

@Repository("transactionRepo")
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

}
