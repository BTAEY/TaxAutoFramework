package com.ey.tax.security;

import com.ey.tax.model.PermissionModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.security.core.GrantedAuthority;

import java.util.Map;

/**
 * Created by zhuji on 3/26/2018.
 */
public class RolePermissionGrantedAuthority implements GrantedAuthority{
    private final String role;

    private Map<String,PermissionModel> permissionMapping;

    public RolePermissionGrantedAuthority(String role) {
        this.role = role;
    }

    public Map<String, PermissionModel> getPermissionMapping() {
        return permissionMapping;
    }

    public void setPermissionMapping(Map<String, PermissionModel> permissionMapping) {
        this.permissionMapping = permissionMapping;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RolePermissionGrantedAuthority that = (RolePermissionGrantedAuthority) o;

        return new EqualsBuilder()
                .append(role, that.role)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(role)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "RolePermissionGrantedAuthority{" +
                "role='" + role + '\'' +
                ", permissions=" + permissionMapping +
                '}';
    }
}
