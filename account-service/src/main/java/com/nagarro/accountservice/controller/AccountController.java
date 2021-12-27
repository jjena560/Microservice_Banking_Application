package com.nagarro.accountservice.controller;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.nagarro.accountservice.dto.AccountDetailsDTO;
import com.nagarro.accountservice.feign.CustomerFeignClient;
import com.nagarro.accountservice.model.Account;
import com.nagarro.accountservice.model.Customer;
import com.nagarro.accountservice.repository.AccountRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class AccountController {
    
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomerFeignClient customerFeignClient;

    @PostMapping("/account/{id}/add")
    public ResponseEntity<String> addMoney(@PathVariable Long id, @RequestParam Long customerId, @RequestParam Double amount){
        return accountRepository.findById(id).map(account -> {
            if(account.getCustomerId() == customerId){
                account.setBalance(amount + account.getBalance());
                accountRepository.save(account);
                return ResponseEntity.ok("Amount " + amount + " added to account no " + id + " for customer id " + customerId);
            }
            else{
                return new ResponseEntity<>("Bad request - Customer with id " + customerId + " doesn't exist",HttpStatus.BAD_REQUEST);
            }
        }).orElseThrow( () -> new EntityNotFoundException("No account with id " + id));
        /*
        Optional<Account> account = accountRepository.findById(id);
        if(account != null){
            Account customerAccount = (Account) account;
        }
        if(account != null && (Account)(account.getCustomerId()) == customerId ){
            return ResponseEntity.ok("Amount " + amount + " added to account no " + id);
        }   
        else{
            return new ResponseEntity<>("Bad request - Customer with id " + customerId + " doesn't exist",HttpStatus.BAD_REQUEST);
        }*/
    }

    @PostMapping("/account/{id}/withdraw")
    public ResponseEntity<String> withdrawMoney(@PathVariable Long id, @RequestParam Long customerId, @RequestParam Double amount){
        return accountRepository.findById(id).map(account -> {
            if(account.getCustomerId() == customerId){
                if(amount > account.getBalance()){
                    return new ResponseEntity<>("Bad request - Insufficient account balance - Current Balance: " + account.getBalance(),HttpStatus.BAD_REQUEST);
                }
                account.setBalance(account.getBalance() - amount);
                accountRepository.save(account);
                return ResponseEntity.ok("Amount " + amount + " withdrawn from account no " + id + " for customer id " + customerId);
            }
            else{
                return new ResponseEntity<>("Bad request - Customer with id " + customerId + " doesn't exist",HttpStatus.BAD_REQUEST);
            }
        }).orElseThrow( () -> new EntityNotFoundException("No account with id " + id));
    }

    @GetMapping("/account/{id}/details")
    public AccountDetailsDTO getAccountDetails(@PathVariable Long id){

        return accountRepository.findById(id).map(account -> {
            Customer customer = customerFeignClient.getCustomerDetails(account.getCustomerId());
            AccountDetailsDTO accountDetailsDTO = new AccountDetailsDTO();
            accountDetailsDTO.setAccountBalance(account.getBalance());
            accountDetailsDTO.setAccountNo(account.getAccountNo());
            accountDetailsDTO.setAccountType(account.getType());
            accountDetailsDTO.setCustomerAddress(customer.getAddress());
            accountDetailsDTO.setCustomerEmail(customer.getEmail());
            accountDetailsDTO.setCustomerId(customer.getId());
            accountDetailsDTO.setCustomerName(customer.getName());
            return accountDetailsDTO;            
        }).orElseThrow( () -> new EntityNotFoundException("Account with id: " + id + " not found"));
        /*
        Optional<Account> account = accountRepository.findById(id);
        if(account == null){
            throw new EntityNotFoundException("Account with id: " + id + " not found");
        }
        else{
            Customer customer = customerFeignClient.getCustomerDetails(account.getCustomerId());
            AccountDetailsDTO accountDetailsDTO = new AccountDetailsDTO();
            accountDetailsDTO.setAccountBalance();
            accountDetailsDTO.setAccountNo();
            accountDetailsDTO.setAccountType();
            accountDetailsDTO.setCustomerAddress();
            accountDetailsDTO.setCustomerEmail();
            accountDetailsDTO.setCustomerId();
            accountDetailsDTO.setCustomerName();
            return accountDetailsDTO;
        }*/
    }

    @DeleteMapping("/account/delete")
    public ResponseEntity<String> deleteAccount(@RequestParam(required = false) Long accountNo, @RequestParam(required = false) Long customerId){
        if(accountNo == null && customerId == null){
            return new ResponseEntity<>("Bad request - Account number/Customer ID  not provided",HttpStatus.BAD_REQUEST);
        }
        else if(accountNo == null && customerId != null){
            return accountRepository.findByCustomerId(customerId).map(account -> {
                accountRepository.delete(account);
                return ResponseEntity.ok("Account deleted for customer id " + customerId);
            }).orElseThrow( () -> new EntityNotFoundException("No account exits for customer id " + customerId));
        }
        else if(accountNo != null && customerId == null){
            return accountRepository.findById(accountNo).map(account -> {
                accountRepository.delete(account);
                return ResponseEntity.ok("Account " + accountNo + " deleted");
            }).orElseThrow( () -> new EntityNotFoundException("No account exits for customer id " + customerId));
        }
        else{
            return accountRepository.findById(accountNo).map(account -> {
                accountRepository.delete(account);
                return ResponseEntity.ok("Account " + accountNo + " deleted");
            }).orElseThrow( () -> new EntityNotFoundException("No account exits for customer id " + customerId));
        }
    }
}
