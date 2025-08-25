package com.enset.digital_banking_tp4_backend.web;

import com.enset.digital_banking_tp4_backend.dtos.AccountHistoryDTO;
import com.enset.digital_banking_tp4_backend.dtos.AccountOperationsDTO;
import com.enset.digital_banking_tp4_backend.dtos.BankAccountDTO;
import com.enset.digital_banking_tp4_backend.entities.AccountOperations;
import com.enset.digital_banking_tp4_backend.exceptions.BankAccountNotFoundException;
import com.enset.digital_banking_tp4_backend.service.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
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
}
