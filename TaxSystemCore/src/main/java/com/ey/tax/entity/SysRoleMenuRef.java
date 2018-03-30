package com.ey.tax.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 角色-菜单关联表 ： 多对多关联
 */
@Entity
@Table(name="t_sys_role_menu_ref")
public class SysRoleMenuRef extends BaseEntity {
    @Column(name="menu_id")
    private Long menuId;

    @Column(name="role_id")
    private Long roleId;

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
