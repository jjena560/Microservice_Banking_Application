package com.nagarro.customerservice.feign;

import com.nagarro.customerservice.feign.hystrix.AccountServiceFallbackFactory;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "account-service", /*fallback = AccountServiceFallback.class,*/ fallbackFactory = AccountServiceFallbackFactory.class)
public interface AccountFeignClient {
    
    @DeleteMapping("/account/delete")
    ResponseEntity<String> deleteAccount(@RequestParam("customerId") Long customerId);

}
