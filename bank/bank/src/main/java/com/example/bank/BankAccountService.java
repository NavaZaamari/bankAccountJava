package com.example.bank;

import com.example.bank.dto.AccountRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.bank.dto.AccountResponse;




@Service
public class BankAccountService {

    private final BankAccountRepository repository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;
    private final TransactionProducer producer;

    public BankAccountService(BankAccountRepository repository, TransactionRepository transactionRepository,
                              PasswordEncoder passwordEncoder,
                              TransactionProducer producer) {
        this.repository = repository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
        this.producer = producer;

    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin".equalsIgnoreCase(username)) {
            return User.withUsername("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .authorities("ADMIN")
                    .build();
        }
        BankAccount account = repository.findByAccountHolderAndDeletedFalse(username);


        return User.withUsername(account.getAccountHolder())
                .password(account.getPassword())
                .authorities("USER")
                .build();
    }



    private AccountResponse convertToResponse(BankAccount account) {
        return new AccountResponse(
                account.getCardNumber(),
                account.getId(),
                account.getAccountHolder(),
                account.getBalance()
        );
    }
    public AccountResponse createAccount(AccountRequest request) {
        BankAccount account = new BankAccount(request.holder(), passwordEncoder.encode(request.password()));
        account.setCardNumber(generateCardNumber());
        BankAccount savedAccount = repository.save(account);
        return convertToResponse(savedAccount);
    }

    private String generateCardNumber() {
        Random random = new Random();
        String cardNumber;

        do {
            long number = 4_000_000_000_000_000L + (long)(random.nextDouble() * 1_000_000_000_000_000L);
            cardNumber = String.valueOf(number);
        } while (repository.existsByCardNumber(cardNumber));
        return cardNumber;
    }

    public List<AccountResponse> findAllAccounts() {
        return repository.findByDeletedFalse().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AccountResponse depositAccount(UUID id, double amount) {
        BankAccount account = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        transactionRepository.save(new Transactions("DEPOSIT", id, amount));
        account.deposit(amount);
        producer.sendTransaction("Deposited " + amount + "to " + account.getAccountHolder());
        return convertToResponse(repository.save(account));
    }
    @Transactional
    public AccountResponse withdrawAccount(UUID id, double amount) {
        BankAccount account = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // nj

        //

        transactionRepository.save(new Transactions("WITHDRAW", id, amount));
        account.withdraw(amount);
        producer.sendTransaction("Withdrawn " + amount + "from " + account.getAccountHolder());
        return convertToResponse(repository.save(account));
    }

    public AccountResponse displayBalanceAccount(UUID id) {
        BankAccount account = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return convertToResponse(account);
    }

    public AccountResponse deleteAccount(UUID id) {
        BankAccount account = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setDeleted(true);
        BankAccount savedAccount = repository.save(account);
        return convertToResponse(savedAccount);
    }

    @Transactional
    public void transfer(UUID fromId, String toCardNumber, double amount) {
        BankAccount sender =  repository.findByIdAndDeletedFalse(fromId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        BankAccount receiver = repository.findByCardNumber(toCardNumber);

        sender.withdraw(amount);
        receiver.deposit(amount);
        repository.save(sender);
        repository.save(receiver);

        transactionRepository.save(new Transactions("TRANSFER", fromId, amount));
    }

    @Scheduled(cron = "0 0 1 1 * *")
    public void monthlyInterest() {
        repository.findByDeletedFalse().forEach(account -> {
            account.deposit(account.getBalance() * 0.01);
            repository.save(account);
        });

    }
}