package com.enset.digital_banking_tp4_backend.dtos;

import lombok.Data;

import java.util.List;
@Data
public class AccountHistoryDTO {

    private String accountId;
    private double balance;
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private List<AccountOperationsDTO> accountOperationsDTO;
}
