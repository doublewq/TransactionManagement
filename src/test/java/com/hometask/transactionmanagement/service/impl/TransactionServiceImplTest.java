package com.hometask.transactionmanagement.service.impl;

import com.hometask.transactionmanagement.model.Transaction;
import com.hometask.transactionmanagement.repository.TransactionRepository;
import com.hometask.transactionmanagement.utils.TransactionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private ConcurrentMap<String, ReentrantLock> orderLocks;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderLocks = new ConcurrentHashMap<>();
    }

    @Test
    void createTransaction_NormalCase_ShouldSucceed() {
        // 准备数据
        String orderId = "4445782509346754560";
        Transaction transaction = TransactionUtil.createTransaction();
        when(transactionRepository.findById(orderId)).thenReturn(Optional.of(new Transaction()));
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        // 执行
        String result = transactionService.crateTransaction(transaction);
        // 验证
        assertNotNull(result);
    }

    @Test
    void createTransaction_LockFail_ShouldThrowException() {
        // 准备数据
        String orderId = "4445782509346754560";
        Transaction transaction = TransactionUtil.createTransaction();
        when(transactionRepository.findById(orderId)).thenReturn(Optional.of(new Transaction()));
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        // 执行
        String result = transactionService.crateTransaction(transaction);
        // 验证
        assertNotNull(result);
    }

    @Test
    void deleteTransaction() {
    }

    @Test
    void updateTransaction() {
    }

    @Test
    void getTransactionById() {
    }

    @Test
    void getAllTransactions() {
    }

    @Test
    void testGetAllTransactions() {
    }
}