package com.ey.tax.service;

import com.ey.tax.model.PermissionModel;
import com.ey.tax.security.RolePermissionGrantedAuthority;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhuji on 3/6/2018.
 */
@Service
public class MyAccessDecisionManager implements AccessDecisionManager {
    /**
     * decide 方法是判定是否拥有权限的决策方法
     * authentication 是SysUserService中循环添加到GrantedAuthority对象的权限信息集合
     * object 包含客户端发起的请求request信息，可转换为 HttpServletRequest request = ((FilterInvocation)object).getHttpRequest();
     * configAttributes 为MyInvocationSecurityMetadataSource的getAttributes(Object object)方法的返回结果，此方法是为了判定请求的url是否在权限表中，
     * 如果在权限表中则返回给decide方法，如果不在权限表中则放行
     * @param authentication
     * @param object
     * @param configAttributes
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if(null == configAttributes || configAttributes.size() <= 0){
            return ;
        }
        ConfigAttribute c;
        String needRole;
        for(Iterator<ConfigAttribute> iter = configAttributes.iterator();iter.hasNext();){
            c = iter.next();
            needRole = c.getAttribute();
            for(GrantedAuthority ga : authentication.getAuthorities()){
                if(ga instanceof RolePermissionGrantedAuthority){
                    RolePermissionGrantedAuthority mga = (RolePermissionGrantedAuthority) ga;
                    Map<String,PermissionModel> permissionMapping = mga.getPermissionMapping();
                    if(permissionMapping.containsKey(needRole.trim())){
                        return;
                    }
                }
            }
        }
        throw new AccessDeniedException("no right");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
