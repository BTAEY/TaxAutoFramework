package com.ey.tax.security;

import com.ey.tax.common.CommonEnums;
import com.ey.tax.core.repository.AccountLoginLogRepository;
import com.ey.tax.core.repository.SysUserRepository;
import com.ey.tax.entity.AccountLoginLogEntity;
import com.ey.tax.entity.SysUser;
import com.ey.tax.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zhuji on 3/22/2018.
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    protected final Log logger = LogFactory.getLog(this.getClass());

    private String defaultTargetUrl;

    private boolean forwardToDestination = false;
    
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    
    @Autowired
    private AccountLoginLogRepository loginLogRepository;

    @Autowired
    private SysUserRepository userRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            SecurityUser user = (SecurityUser) authentication.getPrincipal();
            String ip = this.getIpAddress(request);
            AccountLoginLogEntity loginLogEntity = new AccountLoginLogEntity();
            loginLogEntity.setLoginIp(ip);
            loginLogEntity.setOnlineTime(DateUtil.getNowTimestamp());
            loginLogEntity.setUserId(user.getId());
            loginLogRepository.saveAndFlush(loginLogEntity);
            SysUser sysUser = userRepository.findOne(user.getId());
            sysUser.setLoginStatus(CommonEnums.LoginStatus.ONLINE.getCode().toString());
            sysUser.setLoginCount(sysUser.getLoginCount()+1);
            userRepository.saveAndFlush(sysUser);
        } catch (Exception e) {
            logger.warn("cannot record user login log information.");
        }

        if(this.forwardToDestination){
            logger.info("Login success,Forwarding to "+this.defaultTargetUrl);
            request.getRequestDispatcher(this.defaultTargetUrl).forward(request,response);
        }else{
            logger.info("Login success,Redirecting to "+this.defaultTargetUrl);
            this.redirectStrategy.sendRedirect(request,response,this.defaultTargetUrl);
        }
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-Ip");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        return ip;
    }

    public void setDefaultTargetUrl(String defaultTargetUrl) {
        this.defaultTargetUrl = defaultTargetUrl;
    }

    public void setForwardToDestination(boolean forwardToDestination) {
        this.forwardToDestination = forwardToDestination;
    }
}
