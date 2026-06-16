package com.example.bank;


import com.example.bank.dto.TransferRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionRepository transactionRepository;
    private final BankAccountRepository repository;
    private final BankAccountService bankAccountService;

    public TransactionController(TransactionRepository transactionRepository,  BankAccountRepository repository, BankAccountService bankAccountService) {
        this.transactionRepository = transactionRepository;
        this.repository = repository;
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/")
    public List<Transactions> getTransactions(Authentication authentication) {
        String username = authentication.getName();
        BankAccount user = repository.findByAccountHolderAndDeletedFalse(username);
        return transactionRepository.findByAccountId(user.getId());
    }

    @PutMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request, Authentication authentication) {
        String username = authentication.getName();
        BankAccount sender = repository.findByAccountHolderAndDeletedFalse(username);

        bankAccountService.transfer(sender.getId(), request.toCardNumber(), request.amount());

        return  ResponseEntity.ok("Transfer successful");
    }
}
