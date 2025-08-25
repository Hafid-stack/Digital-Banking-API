package com.enset.digital_banking_tp4_backend.service;

import com.enset.digital_banking_tp4_backend.dtos.*;
import com.enset.digital_banking_tp4_backend.entities.*;
import com.enset.digital_banking_tp4_backend.enums.AccountStatus;
import com.enset.digital_banking_tp4_backend.enums.OperationType;
import com.enset.digital_banking_tp4_backend.exceptions.BalanceInsufficientException;
import com.enset.digital_banking_tp4_backend.exceptions.BankAccountNotFoundException;
import com.enset.digital_banking_tp4_backend.exceptions.CustomerNotFoundException;
import com.enset.digital_banking_tp4_backend.mappers.BankAccountMapperImpl;
import com.enset.digital_banking_tp4_backend.repository.AccountOperationsRepository;
import com.enset.digital_banking_tp4_backend.repository.BankAccountRepository;
import com.enset.digital_banking_tp4_backend.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {


    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    private AccountOperationsRepository accountOperationsRepository;
    private BankAccountMapperImpl dtoMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer");
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);

        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCreditedAt(LocalDate.now());
        currentAccount.setCurrency("DH");
        currentAccount.setStatus(AccountStatus.CREATED);
        CurrentAccount savedBankAccount= bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentBankAccount(savedBankAccount);

    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCreditedAt(LocalDate.now());
        savingAccount.setCurrency("DH");
        savingAccount.setStatus(AccountStatus.CREATED);
        SavingAccount savedBankAccount= bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingBankAccount(savedBankAccount);
    }


    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers= customerRepository.findAll();
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerDTO customerDTO=dtoMapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);
        }
        return customerDTOS;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(
                ()-> new BankAccountNotFoundException("Bank account not found")
        );
        if (bankAccount instanceof CurrentAccount) {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentBankAccount(currentAccount);

        }else{
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingBankAccount(savingAccount);

        }

    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceInsufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(
                ()-> new BankAccountNotFoundException("Bank account not found"));
        if (bankAccount.getBalance() < amount)
            throw new BalanceInsufficientException("Insufficient balance");
        AccountOperations accountOperations = new AccountOperations();
        accountOperations.setType(OperationType.DEBIT);
        accountOperations.setOperationDate(LocalDate.now());
        accountOperations.setDescription(description);
        accountOperations.setAmount(amount);
        accountOperations.setBankAccount(bankAccount);
        accountOperationsRepository.save(accountOperations);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(
                ()-> new BankAccountNotFoundException("Bank account not found"));

        AccountOperations accountOperations = new AccountOperations();
        accountOperations.setType(OperationType.CREDIT);
        accountOperations.setOperationDate(LocalDate.now());
        accountOperations.setDescription(description);
        accountOperations.setAmount(amount);
        accountOperations.setBankAccount(bankAccount);
        accountOperationsRepository.save(accountOperations);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceInsufficientException {

        debit(accountIdSource,amount,"Transfer to "+ accountIdDestination);
        credit(accountIdDestination,amount,"Transfer from "+accountIdSource);

    }
    @Override
    public List<BankAccountDTO> getBankAccountsList() {

        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream()
                .map(bankAccount -> {
            if (bankAccount instanceof CurrentAccount) {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentBankAccount(currentAccount);
            }else {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.fromSavingBankAccount(savingAccount);

            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }
@Override
public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(()->{
            return new CustomerNotFoundException("Customer not found");
        });
        return dtoMapper.fromCustomer(customer);

    }


    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Updating customer");
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);

        return dtoMapper.fromCustomer(savedCustomer);
    }
    @Override
    public void deleteCustomer(Long customerId) {

        log.info("Deleting customer");
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<AccountOperationsDTO> accountHistory(String accountId){
        List<AccountOperations> accountOperations = accountOperationsRepository.findByBankAccountId(accountId);
        return accountOperations.stream().map(accountOperations1 -> dtoMapper.fromAccountOperations(accountOperations1)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null) throw new BankAccountNotFoundException("Account not found");
       Page<AccountOperations> accountOperations = accountOperationsRepository.findByBankAccountId(accountId, PageRequest.of(page, size));
       AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
       List<AccountOperationsDTO>  accountOperationsDTOS= accountOperations.getContent().stream().map(accountOperations1 -> dtoMapper.fromAccountOperations(accountOperations1)).collect(Collectors.toList());
       accountHistoryDTO.setAccountOperationsDTO(accountOperationsDTOS);
       accountHistoryDTO.setAccountId(accountId);
       accountHistoryDTO.setBalance(bankAccount.getBalance());
       accountHistoryDTO.setCurrentPage(page);
       accountHistoryDTO.setPageSize(size);
       accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());

       return accountHistoryDTO;
    }
}
