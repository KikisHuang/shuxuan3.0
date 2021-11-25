package com.gxdingo.sg.bean;

import java.io.Serializable;

/**
 * @author: Weaving
 * @date: 2021/11/11
 * @page: 交易记录数据
 */
public class TransactionBean implements Serializable {


    /**
     * id : 269
     * type : 30
     * amount : 2.0
     * balance : 9105.01
     * description : 邀请新用户注册奖励
     * createTime : 2021-10-14T05:10:31.000+00:00
     */

    private Integer id;
    private Integer type;
    private Double amount;
    private Double balance;
    private String description;
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
