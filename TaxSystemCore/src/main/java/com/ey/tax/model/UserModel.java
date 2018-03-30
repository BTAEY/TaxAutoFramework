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

    private String createman;

    private Date createdate;

    private String updateman;

    private Date updatedate;

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

    public String getCreateman() {
        return createman;
    }

    public void setCreateman(String createman) {
        this.createman = createman;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getUpdateman() {
        return updateman;
    }

    public void setUpdateman(String updateman) {
        this.updateman = updateman;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public String getHeaderImg() {
        return headerImg;
    }

    public void setHeaderImg(String headerImg) {
        this.headerImg = headerImg;
    }
}
