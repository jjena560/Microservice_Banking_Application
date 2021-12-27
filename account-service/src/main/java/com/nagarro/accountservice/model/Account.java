package com.nagarro.accountservice.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Account {
    
    @Id
    Long accountNo;
    String type;
    Double balance;
    Long customerId;
}
