package com.hometask.transactionmanagement.utils;

import com.hometask.transactionmanagement.model.Transaction;
import com.hometask.transactionmanagement.vo.TransactionVo;
import org.springframework.util.ObjectUtils;

/**
 * bean转换工具类
 */
public class BeanConverter {
    public static TransactionVo toTransactionVo(Transaction transaction) {
        if (ObjectUtils.isEmpty(transaction)) {
            return TransactionVo.builder().build();
        }
        TransactionVo transactionVo = TransactionVo.builder().orderId(transaction.getOrderId())
                .fromAccount("*****" + transaction.getFromAccount().substring(0, transaction.getFromAccount().length() - 5))
                .toAccount(transaction.getToAccount())
                .amount(transaction.getAmount())
                .build();
        return transactionVo;
    }
}
