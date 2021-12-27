package com.nagarro.customerservice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Customer {
    
    @Id
    @GeneratedValue
    Long id;
    String name;
    String password;
    String email;
    String address;
}
