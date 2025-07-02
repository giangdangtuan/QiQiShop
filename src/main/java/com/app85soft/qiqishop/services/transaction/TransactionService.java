package com.app85soft.qiqishop.services.transaction;

import com.app85soft.qiqishop.dto.constant.PaymentGateway;
import com.app85soft.qiqishop.dto.constant.TransactionStatus;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.transaction.TransactionRes;

import java.util.List;

public interface TransactionService {
    BaseResponse<List<TransactionRes>> getTransactions(TransactionStatus status, PaymentGateway gateway, String searchKeyword, int page);

    BaseResponse<TransactionRes> getTransaction(int transactionId);


}
