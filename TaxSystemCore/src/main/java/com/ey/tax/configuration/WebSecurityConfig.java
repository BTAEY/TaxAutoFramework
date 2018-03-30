package com.ey.tax.configuration;

import com.ey.tax.configuration.security.SysUserService;
import com.ey.tax.security.LoginSuccessHandler;
import com.ey.tax.security.CustomLogoutSuccessHandler;
import com.ey.tax.service.MyFilterSecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * Created by zhuji on 2/9/2018.
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private CustomLogoutSuccessHandler logoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        loginSuccessHandler.setDefaultTargetUrl("/");
        loginSuccessHandler.setForwardToDestination(false);
        logoutSuccessHandler.setDefaultTargetUrl("/login");
        http.authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin().successHandler(loginSuccessHandler)
                .permitAll()
                .and()
                .logout().logoutSuccessHandler(logoutSuccessHandler)
                .permitAll()
        ;
        http.sessionManagement().maximumSessions(1);
        http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);
        http.exceptionHandling().accessDeniedPage("/403").authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(sysUserService()).passwordEncoder(new Md5PasswordEncoder());
    }

    @Bean
    UserDetailsService sysUserService(){
        return new SysUserService();
    }

    @Bean
    public DefaultMethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler(){
        DefaultMethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler = new DefaultMethodSecurityExpressionHandler();
        defaultMethodSecurityExpressionHandler.setDefaultRolePrefix("");
        return defaultMethodSecurityExpressionHandler;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler(){
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setDefaultRolePrefix("");
        return defaultWebSecurityExpressionHandler;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/static/**")
                .and().ignoring().antMatchers("/static/**")
                .and().ignoring().antMatchers("/bootstrap/**")
                .and().ignoring().antMatchers("/fragments/**")
                .and().ignoring().antMatchers("/**/*.js")
                .and().ignoring().antMatchers("/**/*.css")
                .and().ignoring().antMatchers("/**/*.woff");
    }
}
