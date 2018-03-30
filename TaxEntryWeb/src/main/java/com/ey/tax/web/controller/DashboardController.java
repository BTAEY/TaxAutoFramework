package com.ey.tax.web.controller;

import com.ey.tax.security.SecurityUser;
import com.ey.tax.service.IUserService;
import com.ey.tax.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

/**
 * Created by zhuji on 2/10/2018.
 */
@Controller
public class DashboardController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/")
    public ModelAndView dashboard(Authentication authentication){
        ModelAndView mav = new ModelAndView();
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        UserInfoVo userInfo = userService.findUserById(securityUser.getId());
        mav.addObject("userInfo",userInfo);
        mav.setViewName("dashboard");
        return mav;
    }


}
