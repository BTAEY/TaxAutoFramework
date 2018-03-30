package com.ey.tax.entity;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by zhuji on 2/10/2018.
 */
@Entity
@Table(name="t_sys_user")
public class SysUser extends AccessoryEntity{
    @Column(name="username")
    private String userName;

    @Column(name="password")
    private String password;

    @Column(name="enable")
    private boolean enable = true;

    /**
     * online or offline
     */
    @Column(name="login_status")
    private String loginStatus;

    /**
     * statistic login count
     */
    @Column(name="login_count")
    private Integer loginCount = 0;

    @Column(name="header_img")
    private String headerImg;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public String getHeaderImg() {
        return headerImg;
    }

    public void setHeaderImg(String headerImg) {
        this.headerImg = headerImg;
    }
}
