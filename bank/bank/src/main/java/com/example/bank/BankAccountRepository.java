package com.example.bank;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {
    boolean existsByCardNumber(String cardNumber);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    BankAccount findByCardNumber(String cardNumber);
    Optional<BankAccount> findByIdAndDeletedFalse(UUID id);
    List<BankAccount> findByDeletedFalse();

    BankAccount findByAccountHolderAndDeletedFalse(String accountHolder);
}
