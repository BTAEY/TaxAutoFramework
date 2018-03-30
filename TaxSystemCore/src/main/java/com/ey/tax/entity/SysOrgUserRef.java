package com.ey.tax.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by zhuji on 3/26/2018.
 */
@Entity
@Table(name="t_sys_org_user_ref")
public class SysOrgUserRef extends BaseEntity {

    @Column(name="org_id")
    private Long orgId;

    @Column(name="user_id")
    private Long userId;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
