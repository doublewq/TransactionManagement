package com.hometask.transactionmanagement.service.impl;

import com.hometask.transactionmanagement.exception.TransactionException;
import com.hometask.transactionmanagement.model.Transaction;
import com.hometask.transactionmanagement.repository.TransactionRepository;
import com.hometask.transactionmanagement.service.TransactionService;
import com.hometask.transactionmanagement.utils.SnowflakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static com.hometask.transactionmanagement.exception.ErrorEnum.*;

/**
 * 服务层实现类
 */
@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    // 维护交易的锁
    private ConcurrentMap<String, ReentrantLock> orderLocks = new ConcurrentHashMap<>();

    @Override
    public String crateTransaction(Transaction transaction) {
        Transaction transactionCreated = new Transaction();
        // id一般使用数据库自增主键或数据库自动生成策略，这里模拟生成
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(1, 1);
        transaction.setOrderId(String.valueOf(snowflakeIdGenerator.nextId()));
        transaction.setDateCreate(LocalDateTime.now());
        ReentrantLock reentrantLock = orderLocks.computeIfAbsent(transaction.getOrderId(), e -> new ReentrantLock());
        try {
            boolean locked = reentrantLock.tryLock();
            if (!locked) {
                throw new TransactionException(CONCURRENCY_EXCEPTION);
            }
            Transaction transactionExit = transactionRepository.findById(transaction.getOrderId()).orElse(new Transaction());
            if (StringUtils.isNotBlank(transactionExit.getOrderId())) {
                log.error("transaction exist! order_id: " + transactionExit.getOrderId());
                throw new TransactionException(CONCURRENCY_EXCEPTION);
            }
            try {
                transactionCreated = transactionRepository.save(transaction);
            } catch (RuntimeException e) {
                log.error("create transaction error, order_id:{}", transaction.getOrderId(), e);
                throw new TransactionException(SYSTEM_EXCEPTION);
            }
            orderLocks.remove(transaction.getOrderId(), reentrantLock);
        } finally {
            try {
                reentrantLock.unlock();
            }catch (IllegalMonitorStateException e){
                log.error("not hold lock");
            }
        }
        return transactionCreated.getOrderId();
    }

    @Override
    public void deleteTransaction(String orderId) {
        ReentrantLock reentrantLock = orderLocks.computeIfAbsent(orderId, e -> new ReentrantLock());
        try {
            boolean locked = reentrantLock.tryLock();
            if (!locked) {
                throw new TransactionException(CONCURRENCY_EXCEPTION);
            }
            // 存在性校验
            Optional<Transaction> transactionOpt = transactionRepository.findById(orderId);
            if (!transactionOpt.isPresent()) {
                orderLocks.remove(orderId, reentrantLock);
                // orderID已参数校验过，不存在注入风险，可记录日志
                log.warn("transaction id :{} not exist.", orderId);
                throw new TransactionException(TRANSACTION_NOT_EXISTS_EXCEPTION);
            }

            try {
                transactionRepository.delete(orderId);
            } catch (RuntimeException e) {
                log.error("delete transaction error, order_id:{}", orderId, e);
                throw new TransactionException(SYSTEM_EXCEPTION);
            }
            orderLocks.remove(orderId, reentrantLock);
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override
    public Transaction updateTransaction(String orderId, Transaction transaction) {
        ReentrantLock reentrantLock = orderLocks.computeIfAbsent(orderId, e -> new ReentrantLock());
        try {
            boolean locked = reentrantLock.tryLock();
            if (!locked) {
                throw new TransactionException(CONCURRENCY_EXCEPTION);
            }
            Transaction transactionExit = transactionRepository.findById(orderId).orElse(new Transaction());
            if (StringUtils.isBlank(transactionExit.getOrderId())) {
                log.error("transaction not exist! order_id: " + transactionExit.getOrderId());
                throw new TransactionException(TRANSACTION_NOT_EXISTS_EXCEPTION);
            }

            try {
                transaction.setDateUpdate(LocalDateTime.now());
                transaction.setOrderId(orderId);
                transactionRepository.update(orderId, transaction);
            } catch (RuntimeException e) {
                log.error("update transaction error, orderId: {}", transaction.getOrderId(), e);
                throw new TransactionException(SYSTEM_EXCEPTION);
            }
        } finally {
            reentrantLock.unlock();
        }
        return transaction;
    }

    @Override
    public Transaction getTransactionById(String orderId) {
        Transaction transaction = new Transaction();
        ReentrantLock reentrantLock = orderLocks.getOrDefault(orderId, null);
        if (ObjectUtils.isEmpty(reentrantLock)) {
            transaction = transactionRepository.findById(orderId).orElse(transaction);
        } else {
            log.error("transaction being change. orderId: {}", orderId);
            throw new TransactionException(TRANSACTION_CHANGED_EXCEPTION);
        }
        return transaction;
    }

    @Override
    @Cacheable(value = "transactions")
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll().values().stream()
                .filter(e -> orderLocks.containsKey(e.getOrderId()))
                .collect(Collectors.toList());
        log.warn("has some transaction being changed.");
        return transactions;
    }

    @Override
    public Page<Transaction> getAllTransactions(Pageable pageable) {
        List<Transaction> transactions = getAllTransactions();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), transactions.size());
        // 采用内存分页方式，若数据采用数据库存储，则可以基于数据库sql语句做分页
        return new PageImpl<>(transactions.subList(start, end), pageable, transactions.size());
    }
}
