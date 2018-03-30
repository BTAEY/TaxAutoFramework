package com.ey.tax.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 组织机构表
 */
@Entity
@Table(name="t_sys_organization")
public class SysOrganization extends AccessoryEntity {

    @Column(name="org_name")
    private String orgName;

    @Column(name="description")
    private String description;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
