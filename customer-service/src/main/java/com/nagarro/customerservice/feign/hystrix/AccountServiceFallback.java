package com.nagarro.customerservice.feign.hystrix;

import com.nagarro.customerservice.feign.AccountFeignClient;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountServiceFallback implements AccountFeignClient{
    
    @Override
    public ResponseEntity<String> deleteAccount(Long customerId){
        return new ResponseEntity<>("Bad request - Account for the customer doesn't exist",HttpStatus.BAD_REQUEST);
    }
}
