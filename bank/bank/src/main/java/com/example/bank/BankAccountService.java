package com.example.bank;

import com.example.bank.dto.AccountRequest;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import com.example.bank.dto.AccountResponse;




@Service
public class BankAccountService {

    private final BankAccountRepository repository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    public BankAccountService(BankAccountRepository repository, TransactionRepository transactionRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin".equalsIgnoreCase(username)) {
            return User.withUsername("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .authorities("ADMIN")
                    .build();
        }
        BankAccount account = repository.findByAccountHolderAndDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account holder not found: " + username));


        return User.withUsername(account.getAccountHolder())
                .password(account.getPassword())
                .authorities("USER")
                .build();
    }



    private AccountResponse convertToResponse(BankAccount account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountHolder(),
                account.getBalance()
        );
    }
    public AccountResponse createAccount(AccountRequest request) {
        BankAccount account = new BankAccount(request.holder(), passwordEncoder.encode(request.password()));
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