package com.bigotry.pojo;

import org.springframework.stereotype.Component;

/**
 * @author ：JJHu
 * @date ：Created in 2020/11/28 14:00
 * @description：
 * @modified By：
 * @version: $
 */
@Component
public class AdminUser {
    //管理员名
    private String adminName;
    //密码
    private String password;

    public AdminUser() {
    }

    public AdminUser(String adminName, String password) {
        this.adminName = adminName;
        this.password = password;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AdminUser{" +
                "adminName='" + adminName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
