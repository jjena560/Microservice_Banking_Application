package com.nagarro.customerservice.feign.hystrix;

import com.nagarro.customerservice.feign.AccountFeignClient;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import feign.hystrix.FallbackFactory;

@Component
public class AccountServiceFallbackFactory implements FallbackFactory<AccountFeignClient> {

    @Override
    public AccountFeignClient create(Throwable throwable) {
        return new AccountFeignClient() {
            @Override
            public ResponseEntity<String> deleteAccount(Long customerId) {
                //LOGGER.error("Error occurred", throwable);
                System.out.println("account for customer id " + customerId + " cannot be deleted");
                return new ResponseEntity<>("Bad request - Account for the customer doesn't exist",HttpStatus.BAD_REQUEST);
            }
        };
    }

}
