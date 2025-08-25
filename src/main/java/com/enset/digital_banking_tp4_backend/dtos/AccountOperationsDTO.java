package com.enset.digital_banking_tp4_backend.dtos;

import com.enset.digital_banking_tp4_backend.entities.BankAccount;
import com.enset.digital_banking_tp4_backend.enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data


public class AccountOperationsDTO {

    private Long id;

    private LocalDate operationDate;
    private Double amount;

    private OperationType type;

    private String description;
}
