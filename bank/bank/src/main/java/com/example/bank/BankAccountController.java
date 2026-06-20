package com.example.bank;
import com.example.bank.dto.AccountRequest;
import com.example.bank.dto.AccountResponse;
import com.example.bank.dto.TransactionRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/accounts")
public class BankAccountController {

    private final BankAccountService service;
    private final BankAccountRepository repository;

    public BankAccountController(BankAccountService service, BankAccountRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping
    public AccountResponse createAccount(@RequestBody AccountRequest request) {
        return service.createAccount(request);
    }

    @GetMapping("/getall")
    public List<AccountResponse> getAllAccounts() { 
        return service.findAllAccounts();
    }

    @PutMapping("/deposit")
    public AccountResponse deposit(Authentication authentication, @RequestBody TransactionRequest request) {
        String username = authentication.getName();
        BankAccount user = repository.findByAccountHolderAndDeletedFalse(username);
        return service.depositAccount(user.getId(), request.amount());
    }

    @PutMapping("/withdraw")
    public AccountResponse withdraw(Authentication authentication, @RequestBody TransactionRequest request) {
        String username = authentication.getName();
        BankAccount user = repository.findByAccountHolderAndDeletedFalse(username);
        return service.withdrawAccount(user.getId(), request.amount());
    }

    @GetMapping("/display")
    public AccountResponse display(Authentication authentication) {
        String username = authentication.getName();
        BankAccount user = repository.findByAccountHolderAndDeletedFalse(username);
        return service.displayBalanceAccount(user.getId());
    }

    @DeleteMapping("/")
    public AccountResponse delete(@RequestBody UUID id) {
        return service.deleteAccount(id);
    }


}
