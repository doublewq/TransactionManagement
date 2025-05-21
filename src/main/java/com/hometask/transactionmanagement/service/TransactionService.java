package com.hometask.transactionmanagement.service;

import com.hometask.transactionmanagement.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 服务层接口
 */
public interface TransactionService {
    /**
     * 创建交易单
     *
     * @param transaction 交易单实体参数
     * @return 创建成功的交易单
     */
    String crateTransaction(Transaction transaction);

    /**
     * 基于id删除交易单
     *
     * @param orderId 交易单id
     */
    void deleteTransaction(String orderId);

    /**
     * 修改交易单
     *
     * @param orderId     交易单id
     * @param transaction 交易单实体详情
     * @return 交易单实体详情
     */
    Transaction updateTransaction(String orderId, Transaction transaction);

    /**
     * 基于id获取单个交易单详情
     *
     * @param orderId 交易单id
     * @return 交易单实体详情
     */
    Transaction getTransactionById(String orderId);


    /**
     * 获取所有交易单，返回列表
     *
     * @return 交易列表
     */
    List<Transaction> getAllTransactions();

    /**
     * 基于分页参数返回交易单列表
     *
     * @param pageable 分页参数
     * @return 分页的交易单信息
     */
    Page<Transaction> getAllTransactions(Pageable pageable);
}
