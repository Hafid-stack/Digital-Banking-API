package com.enset.digital_banking_tp4_backend.mappers;

import com.enset.digital_banking_tp4_backend.dtos.AccountOperationsDTO;
import com.enset.digital_banking_tp4_backend.dtos.CurrentBankAccountDTO;
import com.enset.digital_banking_tp4_backend.dtos.CustomerDTO;
import com.enset.digital_banking_tp4_backend.dtos.SavingBankAccountDTO;
import com.enset.digital_banking_tp4_backend.entities.AccountOperations;
import com.enset.digital_banking_tp4_backend.entities.CurrentAccount;
import com.enset.digital_banking_tp4_backend.entities.Customer;
import com.enset.digital_banking_tp4_backend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.swing.plaf.PanelUI;

@Service
public class BankAccountMapperImpl {

    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }
    public Customer fromCustomerDTO(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }
    public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingAccount) {
        SavingBankAccountDTO savingBankAccountDTO = new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingAccount, savingBankAccountDTO);
        savingBankAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());
        return savingBankAccountDTO;
    }
    public SavingAccount fromSavingAccountDTO(SavingBankAccountDTO savingBankAccountDTO) {
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingBankAccountDTO, savingAccount);
        savingAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
        return savingAccount;
    }
    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentAccount) {
        CurrentBankAccountDTO currentBankAccountDTO = new CurrentBankAccountDTO();

        BeanUtils.copyProperties(currentAccount, currentBankAccountDTO);
        currentBankAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        currentBankAccountDTO.setType(currentAccount.getClass().getSimpleName());
        return currentBankAccountDTO;

    }
    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO currentBankAccountDTO) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDTO, currentAccount);
        currentAccount.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
        return currentAccount;
    }
    public AccountOperationsDTO fromAccountOperations(AccountOperations accountOperations) {
        AccountOperationsDTO accountOperationsDTO = new AccountOperationsDTO();
        BeanUtils.copyProperties(accountOperations, accountOperationsDTO);
        return accountOperationsDTO;
    }
}
