package com.example.bank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {
    boolean existsByCardNumber(String cardNumber);
    BankAccount findByCardNumber(String cardNumber);
    Optional<BankAccount> findByIdAndDeletedFalse(UUID id);
    List<BankAccount> findByDeletedFalse();

    BankAccount findByAccountHolderAndDeletedFalse(String accountHolder);
}
