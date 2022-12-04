package com.cannabank.cannabank.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cannabank.cannabank.models.Account;
import com.cannabank.cannabank.repositories.AccountRepository;
import com.cannabank.cannabank.security.services.UserDetailsImpl;

@RestController
@RequestMapping("/cannabank/home/account")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Account account = accountRepository.findById(userDetails.getId()).get();
        return ResponseEntity.ok(account);
    }

    @PutMapping("/deposit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deposit(@RequestBody Account account) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Account accountToUpdate = accountRepository.findById(userDetails.getId()).get();
        accountToUpdate.setBalance(accountToUpdate.getBalance() + account.getBalance());
        accountRepository.save(accountToUpdate);
        return ResponseEntity.ok(accountToUpdate);
    }

    @PutMapping("/withdraw")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> withdraw(@RequestBody Account account) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Account accountToUpdate = accountRepository.findById(userDetails.getId()).get();
        if (accountToUpdate.getBalance() >= account.getBalance()) {
            accountToUpdate.setBalance(accountToUpdate.getBalance() - account.getBalance());
            accountRepository.save(accountToUpdate);
            return ResponseEntity.ok(accountToUpdate);
        } else {
            return ResponseEntity.ok("Insufficient funds");
        }
    }
}
