package com.ey.tax.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * 角色表
 */
@Entity
@Table(name="t_sys_role")
public class SysRole extends AccessoryEntity {
    @Column(name = "role_name")
    private String roleName;

    @Column(name="pid")
    private Long parentId;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
