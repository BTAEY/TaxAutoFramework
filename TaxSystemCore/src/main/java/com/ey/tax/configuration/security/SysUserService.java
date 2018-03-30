package com.ey.tax.configuration.security;

import com.ey.tax.core.dao.PrivilegeDAO;
import com.ey.tax.core.repository.SysUserRepository;
import com.ey.tax.entity.SysUser;
import com.ey.tax.model.PermissionModel;
import com.ey.tax.model.RoleModel;
import com.ey.tax.security.RolePermissionGrantedAuthority;
import com.ey.tax.security.SecurityUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by zhuji on 2/10/2018.
 */
public class SysUserService implements UserDetailsService {
    @Autowired
    SysUserRepository sysUserRepository;

    @Autowired
    private PrivilegeDAO privilegeDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserRepository.findByUserName(username);
        if(user != null){
            List<RoleModel> roles = privilegeDAO.findRolesByUser(user.getId());
            List<GrantedAuthority> grantedAuthorities = roles.stream()
                    .filter(role -> role != null && StringUtils.isNotEmpty(role.getRoleName()))
                    .map(role -> {
                        RolePermissionGrantedAuthority grantedAuthority = new RolePermissionGrantedAuthority(role.getRoleName());
                        List<PermissionModel> permissionModels = role.getPermissionModels();
                        Map<String,PermissionModel> permissionMapping = permissionModels.stream().collect(Collectors.toMap(PermissionModel::getName, Function.identity()));
                        grantedAuthority.setPermissionMapping(permissionMapping);
                        return grantedAuthority;
                    }) //将角色信息添加到GrantedAuthority对象中，在后面进行权限验证是会使用到grantedAuthority对象
                    .collect(Collectors.toList());
            return new SecurityUser(user.getId(),user.getUserName(),user.getPassword(),grantedAuthorities);
        }else{
            throw new UsernameNotFoundException("User: " + username + " do not exist!");
        }
    }
}
