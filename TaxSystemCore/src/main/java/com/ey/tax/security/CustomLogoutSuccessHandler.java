package com.ey.tax.security;

import cn.hutool.core.date.DateUnit;
import com.ey.tax.common.CommonEnums;
import com.ey.tax.core.repository.AccountLoginLogRepository;
import com.ey.tax.core.repository.SysUserRepository;
import com.ey.tax.entity.AccountLoginLogEntity;
import com.ey.tax.entity.SysUser;
import com.ey.tax.utils.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by zhuji on 3/22/2018.
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    protected final Log logger = LogFactory.getLog(CustomLogoutSuccessHandler.class);

    @Autowired
    private AccountLoginLogRepository loginLogRepository;

    @Autowired
    private SysUserRepository userRepository;

    private String defaultTargetUrl;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            SecurityUser user = (SecurityUser) authentication.getPrincipal();
            AccountLoginLogEntity loginLogEntity = loginLogRepository.findTop1ByUserIdOrderByOnlineTimeDesc(user.getId());
            BigDecimal stayTime = new BigDecimal(cn.hutool.core.date.DateUtil.between(loginLogEntity.getOnlineTime(),DateUtil.getNowTimestamp(), DateUnit.MINUTE));
            loginLogEntity.setOfflineTime(DateUtil.getNowTimestamp());
            loginLogEntity.setStayTime(stayTime);
            loginLogRepository.saveAndFlush(loginLogEntity);
            SysUser sysUser = userRepository.findOne(user.getId());
            sysUser.setLoginStatus(CommonEnums.LoginStatus.OFFLINE.getCode().toString());
            userRepository.saveAndFlush(sysUser);
        } catch (Exception e) {
            logger.warn("cannot record user logout log information.");
        }
        this.redirectStrategy.sendRedirect(request,response,this.defaultTargetUrl);
    }

    public void setDefaultTargetUrl(String defaultTargetUrl) {
        this.defaultTargetUrl = defaultTargetUrl;
    }
}
