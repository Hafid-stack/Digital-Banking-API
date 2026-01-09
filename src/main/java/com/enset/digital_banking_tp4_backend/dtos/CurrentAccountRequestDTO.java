package com.enset.digital_banking_tp4_backend.dtos;

import lombok.Data;

@Data
public class CurrentAccountRequestDTO {
    private double initialBalance;
    private double overDraft;
    private Long customerId;
}