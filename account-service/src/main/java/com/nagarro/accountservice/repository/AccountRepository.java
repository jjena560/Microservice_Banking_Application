package com.nagarro.accountservice.repository;

import java.util.List;
import java.util.Optional;

import com.nagarro.accountservice.model.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByCustomerId(Long customerId);
}
