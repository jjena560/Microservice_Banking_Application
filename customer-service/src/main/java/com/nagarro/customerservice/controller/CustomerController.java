package com.nagarro.customerservice.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import com.nagarro.customerservice.feign.AccountFeignClient;
import com.nagarro.customerservice.model.Customer;
import com.nagarro.customerservice.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class CustomerController {
    
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AccountFeignClient accountFeignClient;

    @PostMapping("/customer")
    public Customer addCustomer(@RequestBody Customer customer){
        return customerRepository.save(customer);
    }

    @GetMapping("/customer")
    public ResponseEntity<List<Customer>> getAllCustomers(){
        List<Customer> customers =  customerRepository.findAll();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customer/{id}")
    public Customer getCutomerDetails(@PathVariable Long id){
        return customerRepository.findById(id).orElseThrow( () -> new EntityNotFoundException("customer " + id + " not found"));
    }

    @PutMapping("/customer/{id}")
    public Customer updateCustomerDetails(@RequestBody Customer updateCustomerRequest, @PathVariable Long id){
        return customerRepository.findById(id).map(customer -> {
            customer.setAddress(updateCustomerRequest.getAddress());
            customer.setEmail(updateCustomerRequest.getEmail());
            customer.setName(updateCustomerRequest.getName());
            customer.setPassword(updateCustomerRequest.getPassword());
            customerRepository.save(customer);
            return customer;
        }).orElseThrow( () -> new EntityNotFoundException("customer " + id +  " not found"));
    }

    @DeleteMapping("/customer/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) throws EntityNotFoundException{
        return customerRepository.findById(id).map(customer -> {
            try{
                customerRepository.delete(customer);
                accountFeignClient.deleteAccount(id);
            }catch(Exception e){
                throw new EntityNotFoundException("Customer with id " + id + " deleted but Account for the customer doesn't exist");
            }

            /*if(accountDeleted){
                System.out.println("Account for customer id " + id + " cannot be deleted");
            }
            else{
                System.out.println("Account for customer id " + id + " deleted");
            }*/ 
            
            return ResponseEntity.ok("customer with id " + id + " deleted along with its account");
            //return customer;
        }).orElseThrow( () -> new EntityNotFoundException("customer " + id +  " not found"));
    }
}
