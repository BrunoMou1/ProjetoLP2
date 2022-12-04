package com.cannabank.cannabank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cannabank.cannabank.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    
    Account findByAccountNumber(String accountNumber);

}
