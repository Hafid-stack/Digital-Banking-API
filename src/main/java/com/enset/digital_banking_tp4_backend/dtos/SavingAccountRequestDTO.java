package com.enset.digital_banking_tp4_backend.dtos;

import lombok.Data;

@Data
public class SavingAccountRequestDTO {
    private double initialBalance;
    private double interestRate;
    private Long customerId;
}