package com.hometask.transactionmanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 交易单实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    /**
     * 交易单id
     * 由系统生成的id，非外部传入，故无需参数校验
     */
    @JsonProperty("order_id")
    private String orderId;

    /**
     * 交易方 转出账号id
     */
    @Pattern(regexp = "^[a-zA-Z0-9_]{32}$", message = "account not illegal")
    @JsonProperty("from_account")
    private String fromAccount;

    /**
     * 交易方 转入账号id
     */
    @Pattern(regexp = "^[a-zA-Z0-9_]{32}$", message = "account not illegal")
    @JsonProperty("to_account")
    private String toAccount;

    /**
     * 交易金额
     */
    @Digits(integer = 9, fraction = 5) // 9位整数，5位小数，根据业务诉求调整
    @JsonProperty("amount")
    private double amount;

    /**
     * 创建交易的用户
     */
    @Size(min = 2, max = 20, message = "user id length 2-20")
    @JsonProperty("user_create")
    private String userCreate;


    /**
     * 更新交易的用户
     */
    @Size(min = 2, max = 20, message = "user id length 2-20")
    @JsonProperty("user_update")
    private String userUpdate;

    /**
     * 交易单创建时间
     */
    @FutureOrPresent
    @JsonProperty("date_create")
    private LocalDateTime dateCreate;

    /**
     * 交易单更新时间
     */
    @FutureOrPresent
    @JsonProperty("date_update")
    private LocalDateTime dateUpdate;

    /**
     * 备注描述信息
     */
    @Pattern(regexp = "^[a-zA-Z0-9_\\u4e00-\\u9fa5 ]{0,200}$", message = "special characters are not allowed. and length max 200")
    @JsonProperty("desc")
    private String desc;

    // 交易类型、交易状态。。。
}
