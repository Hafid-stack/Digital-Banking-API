package com.enset.digital_banking_tp4_backend.web;

import com.enset.digital_banking_tp4_backend.dtos.*;
import com.enset.digital_banking_tp4_backend.entities.AccountOperations;
import com.enset.digital_banking_tp4_backend.exceptions.BankAccountNotFoundException;
import com.enset.digital_banking_tp4_backend.exceptions.CustomerNotFoundException;
import com.enset.digital_banking_tp4_backend.service.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class BankAccountRestAPI {
    private final BankAccountService bankAccountService;

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
return bankAccountService.getBankAccount(accountId);
    }
    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts()  {
return bankAccountService.getBankAccountsList();
    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationsDTO> getHistory(@PathVariable String accountId) {
        return bankAccountService.accountHistory(accountId);
    }
    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(@PathVariable String accountId
    , @RequestParam(name = "page",defaultValue = "0") int page
    , @RequestParam(name = "size",defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId,page,size);
    }
    //add Accounts
//    @PostMapping("/customers/{customerId}/current-accounts")
//    public CurrentBankAccountDTO saveCurrentBankAccount(
//            @PathVariable Long customerId,
//            @RequestBody CurrentBankAccountDTO currentAccountDTO) throws CustomerNotFoundException {
//
//        return bankAccountService.saveCurrentBankAccount(
//                currentAccountDTO.getBalance(),
//                currentAccountDTO.getOverDraft(),
//                customerId
//        );
//    }
//
//    // 2. Create Saving Account
//    @PostMapping("/customers/{customerId}/saving-accounts")
//    public SavingBankAccountDTO saveSavingBankAccount(
//            @PathVariable Long customerId,
//            @RequestBody SavingBankAccountDTO savingAccountDTO) throws CustomerNotFoundException {
//
//        return bankAccountService.saveSavingBankAccount(
//                savingAccountDTO.getBalance(),
//                savingAccountDTO.getInterestRate(),
//                customerId
//        );
//    }
//    @PostMapping("/accounts/current")
//    public CurrentBankAccountDTO saveCurrentBankAccount(
//            @RequestParam double initialBalance,
//            @RequestParam double overDraft,
//            @RequestParam Long customerId) throws CustomerNotFoundException {
//
//        // Assuming your service has this method
//        return bankAccountService.saveCurrentBankAccount(initialBalance, overDraft, customerId);
//    }
//
//    // POST /accounts/saving
//    @PostMapping("/accounts/saving")
//    public SavingBankAccountDTO saveSavingBankAccount(
//            @RequestParam double initialBalance,
//            @RequestParam double interestRate,
//            @RequestParam Long customerId) throws CustomerNotFoundException {
//
//        return bankAccountService.saveSavingBankAccount(initialBalance, interestRate, customerId);
//    }
    @PostMapping("/accounts/current")
    public CurrentBankAccountDTO saveCurrentBankAccount(@RequestBody CurrentAccountRequestDTO request) throws CustomerNotFoundException {
        return bankAccountService.saveCurrentBankAccount(
                request.getInitialBalance(),
                request.getOverDraft(),
                request.getCustomerId()
        );
    }

    @PostMapping("/accounts/saving")
    public SavingBankAccountDTO saveSavingBankAccount(@RequestBody SavingAccountRequestDTO request) throws CustomerNotFoundException {
        return bankAccountService.saveSavingBankAccount(
                request.getInitialBalance(),
                request.getInterestRate(),
                request.getCustomerId()
        );
    }
}
