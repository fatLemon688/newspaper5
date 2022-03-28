package com.bigotry.pojo;

import org.springframework.stereotype.Component;

/**
 * @author ：JJHu
 * @date ：Created in 2020/12/2 20:28
 * @description：
 * @modified By：
 * @version: $
 */
@Component
public class TbOrder {
    //订单编号
    private String orderNo;
    //用户编号
    private String userId;
    //报刊代号
    private String codeName;
    //订阅份数
    private Integer subscriptNo;
    //订阅月数
    private Integer subscriptMonth;
    //所需金额
    private Integer totalPrice;
    private NewsPaper paper;
    public TbOrder() {
    }

    public TbOrder(String orderNo, String userId, String codeName, Integer subscriptNo, Integer subscriptMonth, Integer totalPrice, NewsPaper paper) {
        this.orderNo = orderNo;
        this.userId = userId;
        this.codeName = codeName;
        this.subscriptNo = subscriptNo;
        this.subscriptMonth = subscriptMonth;
        this.totalPrice = totalPrice;
        this.paper = paper;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public Integer getSubscriptNo() {
        return subscriptNo;
    }

    public void setSubscriptNo(Integer subscriptNo) {
        this.subscriptNo = subscriptNo;
    }

    public Integer getSubscriptMonth() {
        return subscriptMonth;
    }

    public void setSubscriptMonth(Integer subscriptMonth) {
        this.subscriptMonth = subscriptMonth;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public NewsPaper getPaper() {
        return paper;
    }

    public void setPaper(NewsPaper paper) {
        this.paper = paper;
    }

    @Override
    public String toString() {
        return "TbOrder{" +
                "orderNo='" + orderNo + '\'' +
                ", userId='" + userId + '\'' +
                ", codeName='" + codeName + '\'' +
                ", subscriptNo=" + subscriptNo +
                ", subscriptMonth=" + subscriptMonth +
                ", totalPrice=" + totalPrice +
                ", paper=" + paper +
                '}';
    }
}
