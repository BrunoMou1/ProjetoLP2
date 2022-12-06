package com.cannabank.cannabank.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cannabank.cannabank.models.Account;
import com.cannabank.cannabank.models.Transaction;
import com.cannabank.cannabank.models.User;
import com.cannabank.cannabank.repositories.AccountRepository;
import com.cannabank.cannabank.repositories.TransactionRepository;
import com.cannabank.cannabank.repositories.UserRepository;
import com.cannabank.cannabank.security.services.UserDetailsImpl;

@RestController
@RequestMapping("/cannabank/home/transaction")
public class TransactionController {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId()).get();
        Account accountTo = accountRepository.findByAccountNumber(transaction.getAccountNumber());
        transaction.setDate(java.time.LocalDate.now());
        if (user.getAccount().getBalance() >= transaction.getAmount()) {
            user.getAccount().setBalance(user.getAccount().getBalance() -
                    transaction.getAmount());
            accountRepository.save(user.getAccount());
            accountTo.setBalance(accountTo.getBalance() + transaction.getAmount());
            accountRepository.save(accountTo);
            transactionRepository.save(transaction);
            user.getTransactions().add(transaction);
            userRepository.save(user);
            return ResponseEntity.ok(transaction);
        } else {
            return ResponseEntity.ok("Insufficient funds");
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getTransactions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId()).get();
        return ResponseEntity.ok(user.getTransactions());
    }
}
