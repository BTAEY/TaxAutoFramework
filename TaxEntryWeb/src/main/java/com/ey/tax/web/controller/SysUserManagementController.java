package com.ey.tax.web.controller;

import com.ey.tax.security.SecurityUser;
import com.ey.tax.service.IUserService;
import com.ey.tax.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

/**
 * Created by zhuji on 4/9/2018.
 */
@Controller
public class SysUserManagementController {
    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/admin/menu/user",method = RequestMethod.GET)
    public ModelAndView userPage(Principal principal){
        SecurityUser loginUser = (SecurityUser) principal;
        //load all user list
        List<UserInfoVo> userInfoVoList = userService.findAllUserList();
        ModelAndView mav = new ModelAndView();
        mav.addObject("users",userInfoVoList);
        mav.setViewName("userPage");
        return mav;
    }
}
