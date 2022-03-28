package com.bigotry.pojo;

import org.springframework.stereotype.Component;

/**
 * @author ：JJHu
 * @date ：Created in 2020/11/28 14:02
 * @description：
 * @modified By：
 * @version: $
 */
@Component
public class User {
    //用户账号
    private String userId;
    //密码
    private String password;
    //姓名
    private String name;
    //身份证号
    private String IDCard;
    //电话
    private String phone;
    //地址
    private String address;
    //部门号
    private Integer deptNo;

    private TbOrder tbOrder;

    public Integer getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(Integer deptNo) {
        this.deptNo = deptNo;
    }

    public TbOrder getTbOrder() {
        return tbOrder;
    }

    public void setTbOrder(TbOrder tbOrder) {
        this.tbOrder = tbOrder;
    }

    public User() {
    }

    public User(String userId, String password, String name, String IDCard, String phone, String address, Integer deptNo, TbOrder tbOrder) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.IDCard = IDCard;
        this.phone = phone;
        this.address = address;
        this.deptNo = deptNo;
        this.tbOrder = tbOrder;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", IDCard='" + IDCard + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", deptNo=" + deptNo +
                ", tbOrder=" + tbOrder +
                '}';
    }
}
