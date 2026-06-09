package com.example.bank;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transactions, Long> {

    List<Transactions> findByAccountId(Long accountId);
}
