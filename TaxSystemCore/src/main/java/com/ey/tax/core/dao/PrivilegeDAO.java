package com.ey.tax.core.dao;

import com.ey.tax.entity.SysPermission;
import com.ey.tax.entity.SysRole;
import com.ey.tax.entity.SysUser;
import com.ey.tax.model.RoleModel;
import com.ey.tax.model.UserModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhuji on 3/26/2018.
 */
@Repository
public interface PrivilegeDAO {
    List<RoleModel> findRolesByUser(Long userId);

    UserModel findUserById(Long userId);

    List<SysPermission> findAllPermissions();
}
