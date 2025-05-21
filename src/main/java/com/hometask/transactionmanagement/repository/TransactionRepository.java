package com.hometask.transactionmanagement.repository;

import com.hometask.transactionmanagement.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 持久化接口层
 */
@Repository
public interface TransactionRepository {
    /**
     * 持久化一个交易单
     *
     * @param transaction
     * @return
     */
    Transaction save(Transaction transaction);

    /**
     * 基于id删除一条交易单
     *
     * @param orderId
     */
    void delete(String orderId);

    /**
     * 更新一条交易单
     *
     * @param id
     * @param transaction
     * @return
     */
    Transaction update(String id, Transaction transaction);

    /**
     * 查询一条交易单
     *
     * @param id
     * @return
     */
    Optional<Transaction> findById(String id);

    /**
     * 获取所有交易单
     *
     * @return
     */
    ConcurrentHashMap<String, Transaction> findAll();
}
