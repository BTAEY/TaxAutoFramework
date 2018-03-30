package com.ey.tax.entity;

import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 菜单表
 */
@Entity
@Table(name="t_sys_menu")
public class SysMenu extends BaseEntity {

    @Column(name="menu_name")
    @NaturalId
    private String menuName;

    @Column(name="menu_url")
    private String menuUrl;

    @Column(name="description")
    private String description;

    @Column(name="pid")
    private Long parentId;

    @Column(name="menu_level")
    private Integer menuLevel;

    @Column(name="menu_order")
    private Integer menuOrder;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(Integer menuLevel) {
        this.menuLevel = menuLevel;
    }

    public Integer getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }
}
