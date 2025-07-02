package com.app85soft.qiqishop.repositories.transaction;

import com.app85soft.qiqishop.dto.constant.PaymentGateway;
import com.app85soft.qiqishop.dto.constant.TransactionStatus;
import com.app85soft.qiqishop.dto.response.transaction.TransactionRes;

import java.util.List;

public interface TransactionRepositoryCustom {
    long countTransaction(TransactionStatus status, PaymentGateway gateway, String searchKeyword);

    List<TransactionRes> getTransactions(TransactionStatus status, PaymentGateway gateway, String searchKeyword, int page);

    TransactionRes getTransactionDetail(int transactionId);
}
