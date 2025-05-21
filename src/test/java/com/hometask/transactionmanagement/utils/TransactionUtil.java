package com.hometask.transactionmanagement.utils;

import com.hometask.transactionmanagement.model.Transaction;

import java.time.LocalDateTime;

public class TransactionUtil {
    public static Transaction createTransaction() {
        Transaction transaction = new Transaction();
        transaction.setOrderId("4445782509346754560");
        transaction.setFromAccount("xCzSzwWCMCJCgJxnYFKYnmkcXwCitWhl");
        transaction.setToAccount("duqdOHuyOOpbtICwbqVQAqoQHqqlPQMf");
        transaction.setAmount(1000.0);
        transaction.setDateCreate(LocalDateTime.now());
        transaction.setDesc("desc");
        return transaction;
    }
}
