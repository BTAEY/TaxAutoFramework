package com.ey.tax.web.controller;

import cn.hutool.json.JSONUtil;
import com.ey.tax.entity.SysMenu;
import com.ey.tax.security.SecurityUser;
import com.ey.tax.service.IMenuService;
import com.ey.tax.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

/**
 * Created by zhuji on 3/26/2018.
 */
@RestController
public class LoadMenuController {
    @Autowired
    private IMenuService menuService;

    @RequestMapping(value = "/menu/load",produces = "application/json",method = RequestMethod.GET)
    public String loadMenu(Authentication authentication){
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        List<MenuVo> menuList = menuService.loadMenus(securityUser.getId());
        return JSONUtil.toJsonStr(menuList);
    }
}
