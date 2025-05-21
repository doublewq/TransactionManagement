package com.hometask.transactionmanagement.controller;

import com.hometask.transactionmanagement.model.Transaction;
import com.hometask.transactionmanagement.service.TransactionService;
import com.hometask.transactionmanagement.utils.BeanConverter;
import com.hometask.transactionmanagement.utils.RequestContent;
import com.hometask.transactionmanagement.vo.TransactionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * 控制器
 */
@RestController
@RequestMapping("/api/transaction")
@Validated
@Slf4j
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<String> createTransaction(@RequestBody @Valid Transaction transaction) {
        // 获取登录用户id（此处标识为请求头的userId)
        transaction.setUserCreate(RequestContent.getUserId());
        String orderId = transactionService.crateTransaction(transaction);
        log.info("create transaction success. orderId {}", orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable @Pattern(regexp = "^[\\w]{19,64}$", message = "id illegal") String id) {
        // 权限校验, 检查当前用户是否有修改权限 TODO
        transactionService.deleteTransaction(id);
        log.info("delete transaction success. orderId {}", id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionVo> updateTransaction(@PathVariable(value = "id") @Pattern(regexp = "^[\\w]{19,64}$", message = "id illegal") String id,
                                                           @RequestBody Transaction transaction) {
        // 权限校验, 检查当前用户是否有修改权限 TODO
        // 校验通过，则进行下一步修改
        transaction.setUserUpdate(RequestContent.getUserId());
        Transaction updateTransaction = transactionService.updateTransaction(id, transaction);
        TransactionVo transactionVo = BeanConverter.toTransactionVo(updateTransaction);
        log.info("update transaction success. orderId {}", id);
        return ResponseEntity.ok(transactionVo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable @Pattern(regexp = "^[\\w]{19,64}$", message = "id illegal") String id) {
        // 权限校验, 检查当前用户是否有查询权限 TODO
        Transaction transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping
    public ResponseEntity<Page<Transaction>> getAllTransactionsWithPage(@RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
                                                                        @RequestParam(value = "size", defaultValue = "10") @Min(0) @Max(100) int size) {
        // 权限校验, 检查当前用户是否有查询权限 TODO
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> pageTransaction = transactionService.getAllTransactions(pageable);
        return ResponseEntity.ok(pageTransaction);
    }

}
