package com.nagarro.accountservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDetailsDTO {
    String customerName;
    String customerEmail;
    Long customerId;
    String customerAddress;
    String accountType;
    Double accountBalance;
    Long accountNo;
}
