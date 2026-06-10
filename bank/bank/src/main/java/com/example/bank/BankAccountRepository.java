package com.example.bank;
import com.example.bank.BankAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {
    Optional<BankAccount> findByIdAndDeletedFalse(Long id);
    List<BankAccount> findByDeletedFalse();

    Optional<BankAccount> findByAccountHolderAndDeletedFalse(String accountHolder);
}
