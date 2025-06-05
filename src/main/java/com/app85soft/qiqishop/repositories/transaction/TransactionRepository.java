package com.app85soft.qiqishop.repositories.transaction;

import com.app85soft.qiqishop.entities.transaction.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Integer>, TransactionRepositoryCustom {
}
