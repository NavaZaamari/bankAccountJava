package com.example.bank;

import com.example.bank.dto.AccountRequest;
import com.example.bank.dto.AccountResponse;
import com.example.bank.dto.TransactionRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/accounts")
public class BankAccountController {

    private final BankAccountService service;

    public BankAccountController(BankAccountService service) {
        this.service = service;
    }

    @PostMapping
    public AccountResponse createAccount(@RequestBody AccountRequest request) {
        return service.createAccount(request);
    }

    @GetMapping("/getall")
    public List<AccountResponse> getAllAccounts() { 
        return service.findAllAccounts();
    }

    @PutMapping("/deposit/{id}")
    public AccountResponse deposit(@PathVariable Long id, @RequestBody TransactionRequest request) {
        return service.depositAccount(id, request.amount());
    }

    @PutMapping("/withdraw/{id}")
    public AccountResponse withdraw(@PathVariable Long id, @RequestBody TransactionRequest request) {
        return service.withdrawAccount(id, request.amount());
    }

    @GetMapping("/{id}")
    public AccountResponse display(@PathVariable Long id) {
        return service.displayBalanceAccount(id);
    }

    @DeleteMapping("/{id}")
    public AccountResponse delete(@PathVariable Long id) {
        return service.deleteAccount(id);
    }


}
