package com.nagarro.accountservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Customer {
    Long id;
    String name;
    String password;
    String email;
    String address;
}
