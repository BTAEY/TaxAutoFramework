package com.ey.tax.model;

import java.util.List;

/**
 * Created by zhuji on 3/27/2018.
 */
public class RoleModel {
    private Long id;

    private String roleName;

    private Long pid;

    private List<PermissionModel> permissionModels;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<PermissionModel> getPermissionModels() {
        return permissionModels;
    }

    public void setPermissionModels(List<PermissionModel> permissionModels) {
        this.permissionModels = permissionModels;
    }
}
