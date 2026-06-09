package com.example.bank;

import com.example.bank.dto.AccountRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import com.example.bank.dto.AccountResponse;




@Service
public class BankAccountService {

    private final BankAccountRepository repository;
    private final TransactionRepository transactionRepository;

    public BankAccountService(BankAccountRepository repository, TransactionRepository transactionRepository) {
        this.repository = repository;
        this.transactionRepository = transactionRepository;
    }


    public BankAccount createAccount(String holder) {
        return repository.save(new BankAccount(holder));
    }

    private AccountResponse convertToResponse(BankAccount account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountHolder(),
                account.getBalance()
        );
    }
    public AccountResponse createAccount(AccountRequest request) {
        BankAccount account = new BankAccount(request.holder());
        BankAccount savedAccount = repository.save(account);
        return convertToResponse(savedAccount);
    }
    public List<AccountResponse> findAllAccounts() {
        return repository.findByDeletedFalse().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AccountResponse depositAccount(Long id, double amount) {
        BankAccount account = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        transactionRepository.save(new Transactions("DEPOSIT", id, amount));
        account.deposit(amount);
        return convertToResponse(repository.save(account));
    }
    @Transactional
    public AccountResponse withdrawAccount(Long id, double amount) {
        BankAccount account = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        transactionRepository.save(new Transactions("WITHDRAW", id, amount));
        account.withdraw(amount);

        return convertToResponse(repository.save(account));
    }

    public AccountResponse displayBalanceAccount(Long id) {
        BankAccount account = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return convertToResponse(account);
    }

    public AccountResponse deleteAccount(Long id) {
        BankAccount account = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setDeleted(true);
        BankAccount savedAccount = repository.save(account);
        return convertToResponse(savedAccount);
    }
}
