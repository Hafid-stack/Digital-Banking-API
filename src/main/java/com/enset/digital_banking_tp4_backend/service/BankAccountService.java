package com.enset.digital_banking_tp4_backend.service;

import com.enset.digital_banking_tp4_backend.dtos.*;
import com.enset.digital_banking_tp4_backend.entities.BankAccount;
import com.enset.digital_banking_tp4_backend.entities.CurrentAccount;
import com.enset.digital_banking_tp4_backend.entities.Customer;
import com.enset.digital_banking_tp4_backend.entities.SavingAccount;

import com.enset.digital_banking_tp4_backend.exceptions.BalanceInsufficientException;
import com.enset.digital_banking_tp4_backend.exceptions.BankAccountNotFoundException;
import com.enset.digital_banking_tp4_backend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit (String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceInsufficientException;
    void credit (String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer (String accountIdSource,String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceInsufficientException;


    List<BankAccountDTO> getBankAccountsList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<AccountOperationsDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
}
