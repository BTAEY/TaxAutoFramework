package com.ey.tax.model;

import com.ey.tax.common.CommonEnums;

import java.util.Date;
import java.util.List;

/**
 * Created by zhuji on 3/27/2018.
 */
public class UserModel {
    private Long id;

    private String name;

    private List<RoleModel> roles;

    private CommonEnums.LoginStatus loginStatus;

    private Boolean enable;

    private Integer loginCount;

    private String createBy;

    private Date createDate;

    private String lastModifiedBy;

    private Date lastModifiedDate;

    private String headerImg;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RoleModel> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleModel> roles) {
        this.roles = roles;
    }

    public CommonEnums.LoginStatus getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(CommonEnums.LoginStatus loginStatus) {
        this.loginStatus = loginStatus;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
