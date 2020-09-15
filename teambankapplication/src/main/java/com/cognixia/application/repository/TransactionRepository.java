package com.cognixia.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.application.model.Transaction;

@Repository("transactionRepo")
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

	List<Transaction> findAllByUserUserId(int user_user_id);
}
