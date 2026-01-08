package com.enset.digital_banking_tp4_backend.web;

import com.enset.digital_banking_tp4_backend.dtos.CustomerDTO;
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
public class CustomerRestController {
    private BankAccountService bankAccountService;
    @GetMapping("/customers")
public List<CustomerDTO> customers(){
    return bankAccountService.listCustomers();
}
@GetMapping("/customer/{id}")
public CustomerDTO getCustomerById(@PathVariable(name="id") Long id) throws CustomerNotFoundException {
return bankAccountService.getCustomer(id);
}
@PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        return bankAccountService.saveCustomer(customerDTO);
}

        @PutMapping("/customers/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId, @RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);
    }

        @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id) throws CustomerNotFoundException {
        bankAccountService.deleteCustomer(id);
    }
}
