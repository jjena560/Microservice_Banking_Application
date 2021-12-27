package com.nagarro.accountservice.feign;

import com.nagarro.accountservice.model.Customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service"/*fallback = CustomerServiceFallback.class,*/)
public interface CustomerFeignClient {
    
    @GetMapping("/customer/{id}")
    Customer getCustomerDetails(@PathVariable Long id);

}
