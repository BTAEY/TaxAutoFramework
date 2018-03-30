package com.ey.tax.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * 用户登录日志记录表
 */
@Entity
@Table(name="t_login_log")
public class AccountLoginLogEntity extends BaseEntity{
    @Column(name="online_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date onlineTime;

    @Column(name="offline_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date offlineTime;

    @Column(name="stay_time")
    private BigDecimal stayTime;

    @Column(name="login_ip")
    private String loginIp;

    @Column(name="user_id")
    private Long userId;

    public Date getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Date getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineTime(Date offlineTime) {
        this.offlineTime = offlineTime;
    }

    public BigDecimal getStayTime() {
        return stayTime;
    }

    public void setStayTime(BigDecimal stayTime) {
        this.stayTime = stayTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
