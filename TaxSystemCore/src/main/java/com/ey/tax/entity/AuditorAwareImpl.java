package com.ey.tax.entity;

import com.ey.tax.security.SecurityUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by zhuji on 5/9/2018.
 */
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public String getCurrentAuditor() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        SecurityUser user = (SecurityUser) securityContext.getAuthentication().getPrincipal();
        return user.getUsername();
    }
}
