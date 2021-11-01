package com.gxdingo.sg.bean;

import java.io.Serializable;

/**
 * @author: Weaving
 * @date: 2021/10/22
 * @page:
 */
public class BankcardBean implements Serializable {

    /**
     * bankType : ABC
     * name : 中国农业银行
     * icon : https://apimg.alipay.com/combo.png?d=cashier&t=ABC
     */
    private int id;
    private String number;
    private String cardName;
    private String bankType;
    private String name;
    private String icon;
    private String type;

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
