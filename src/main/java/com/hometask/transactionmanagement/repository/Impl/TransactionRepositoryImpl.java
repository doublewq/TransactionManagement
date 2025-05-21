package com.hometask.transactionmanagement.repository.Impl;

import com.hometask.transactionmanagement.model.Transaction;
import com.hometask.transactionmanagement.repository.TransactionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储层实现类
 */
@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    // 线程安全集合 用于数据的内存型存储，持久化可使用mysql或其他数据库
    private final ConcurrentHashMap<String, Transaction> transactionMaps = new ConcurrentHashMap<>();

    @Override
    public Transaction save(Transaction transaction) {
        transactionMaps.put(transaction.getOrderId(), transaction);
        return transaction;
    }

    @Override
    public void delete(String orderId) {
        transactionMaps.remove(orderId);
    }

    @Override
    public Transaction update(String id, Transaction transaction) {
        return transactionMaps.computeIfPresent(id, (key, value) -> transaction);
    }

    @Override
    public Optional<Transaction> findById(String id) {
        return Optional.ofNullable(transactionMaps.get(id));
    }

    /**
     * TODO 基于数据库持久化之后，一般为分页查询出用户数据；避免数据量过大，内存不足
     *
     * @return
     */
    @Override
    public ConcurrentHashMap<String, Transaction> findAll() {
        return transactionMaps;
    }
}
