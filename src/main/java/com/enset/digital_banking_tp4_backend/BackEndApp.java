package com.enset.digital_banking_tp4_backend;

import com.enset.digital_banking_tp4_backend.dtos.BankAccountDTO;
import com.enset.digital_banking_tp4_backend.dtos.CurrentBankAccountDTO;
import com.enset.digital_banking_tp4_backend.dtos.CustomerDTO;
import com.enset.digital_banking_tp4_backend.dtos.SavingBankAccountDTO;
import com.enset.digital_banking_tp4_backend.entities.*;
import com.enset.digital_banking_tp4_backend.enums.AccountStatus;
import com.enset.digital_banking_tp4_backend.enums.OperationType;
import com.enset.digital_banking_tp4_backend.exceptions.BalanceInsufficientException;
import com.enset.digital_banking_tp4_backend.exceptions.BankAccountNotFoundException;
import com.enset.digital_banking_tp4_backend.exceptions.CustomerNotFoundException;
import com.enset.digital_banking_tp4_backend.repository.AccountOperationsRepository;
import com.enset.digital_banking_tp4_backend.repository.BankAccountRepository;
import com.enset.digital_banking_tp4_backend.repository.CustomerRepository;
import com.enset.digital_banking_tp4_backend.service.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
//import java.sql.Date;
//
//import java.util.Date;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class BackEndApp {

    public static void main(String[] args) {
        SpringApplication.run(BackEndApp.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService, AccountOperationsRepository accountOperationsRepository) {
        return args -> {
            Stream.of("Hafid","Mohamed","Johanna").forEach(name -> {
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                bankAccountService.saveCustomer(customer);
            });

            bankAccountService.listCustomers().forEach(
                    customer -> {
                        try {
                            bankAccountService.saveCurrentBankAccount(Math.random()*9000,9000, customer.getId());
                            bankAccountService.saveSavingBankAccount(Math.random()*12000,3.5, customer.getId());

                        } catch (CustomerNotFoundException e) {
                            e.getStackTrace();
                        }
                    }
            );
            List<BankAccountDTO> bankAccounts=bankAccountService.getBankAccountsList();

            for (BankAccountDTO bankAccount : bankAccounts) {
                for (int i = 0; i <10 ; i++) {
                    String accountId;
                    if (bankAccount instanceof SavingBankAccountDTO) {
                        accountId=((SavingBankAccountDTO)bankAccount).getId();
                    }else {
                        accountId=((CurrentBankAccountDTO) bankAccount).getId();
                    }
                    bankAccountService.credit(accountId,10000+Math.random()*9000,"CREDIT");
                    bankAccountService.debit(accountId,1000+Math.random()*9000,"DEBIT");

                }
            }

        };
    }
    //After running the app and adding the databases then we no more need this part, since it would keep adding the same data to the DB.
    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationsRepository accountOperationsRepository) {
        return args -> {
            //for some reason builder does not work
            //Update: there is an issue with lombok i changed it to the one chatgpt give me and it worked
                    customerRepository.save(Customer.builder()
                            .name("Hafid")
                            .email("hafid@gmail.com").build());
            customerRepository.save(Customer.builder()
                    .name("Yassine")
                    .email("yassine@gmail.com").build());
            customerRepository.save(Customer.builder()
                    .name("Jihad")
                    .email("jihad@gmail.com").build());
//            Stream.of("Hafid","Yassine","Jihad").forEach(name->{
//                Customer customer = new Customer();
//                customer.setName(name);
//                customer.setEmail(name+"@gmail.com");
//                customerRepository.save(customer);
//            });
            customerRepository.findAll().forEach(customer -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setCustomer(customer);
                currentAccount.setBalance(Math.random() * 8000);
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCreditedAt(LocalDate.now());
                currentAccount.setOverDraft(20000);
                currentAccount.setCurrency("DH");
            bankAccountRepository.save(currentAccount);

                    SavingAccount savingAccount = new SavingAccount();
                    savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setCustomer(customer);
                savingAccount.setBalance(Math.random() * 8000);
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCreditedAt(LocalDate.now());
                savingAccount.setInterestRate(6.3);
                savingAccount.setCurrency("DH");
                    bankAccountRepository.save(savingAccount);
            });
            bankAccountRepository.findAll().forEach(acc -> {
                for (int i =0; i < 10; i++) {
                    AccountOperations accountOperations = new AccountOperations();
                    accountOperations.setOperationDate(LocalDate.now());
                    accountOperations.setAmount(Math.random() * 99000);
                    accountOperations.setType(Math.random()>0.5? OperationType.CREDIT: OperationType.DEBIT);
                    accountOperations.setBankAccount(acc);
                    accountOperationsRepository.save(accountOperations);
                }


            });


        };
    }

}
