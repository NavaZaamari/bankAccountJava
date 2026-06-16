package com.example.bank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {
    Optional<BankAccount> findByIdAndDeletedFalse(Long id);
    List<BankAccount> findByDeletedFalse();

    BankAccount findByAccountHolderAndDeletedFalse(String accountHolder);
}
