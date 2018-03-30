package com.ey.tax.vo;

/**
 * Created by zhuji on 3/27/2018.
 */
public class RoleVo {
    private Long roleId;

    private String roleName;

    private Long pid;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }
}
