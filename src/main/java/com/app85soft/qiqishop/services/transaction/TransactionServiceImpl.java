package com.app85soft.qiqishop.services.transaction;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.constant.PaymentGateway;
import com.app85soft.qiqishop.dto.constant.TransactionStatus;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.transaction.TransactionRes;
import com.app85soft.qiqishop.entities.role.constant.PermissionKey;
import com.app85soft.qiqishop.entities.role.constant.PermissionType;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.repositories.transaction.TransactionRepository;
import com.app85soft.qiqishop.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl extends BaseService implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Override
    public BaseResponse<List<TransactionRes>> getTransactions(TransactionStatus status, PaymentGateway gateway, String searchKeyword, int page) {
        User user = getUser(PermissionKey.READ, PermissionType.TRANSACTION);
        long countTransaction = transactionRepository.countTransaction(status, gateway, searchKeyword);
        List<TransactionRes> listTransactions = transactionRepository.getTransactions(status, gateway, searchKeyword, page);
        return new BaseResponse<>(listTransactions, countTransaction, page);
    }

    @Override
    public BaseResponse<TransactionRes> getTransaction(int transactionId) {
        User user = getUser(PermissionKey.READ, PermissionType.TRANSACTION);
        TransactionRes transactionRes = transactionRepository.getTransactionDetail(transactionId);
        if (transactionRes == null) {
            throw new BusinessException(Translator.toLocale("id_not_exist"), HttpStatus.NOT_FOUND);
        }
        return new BaseResponse<>(transactionRes);
    }
}
