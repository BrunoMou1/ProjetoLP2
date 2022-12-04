package com.cannabank.cannabank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cannabank.cannabank.models.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
