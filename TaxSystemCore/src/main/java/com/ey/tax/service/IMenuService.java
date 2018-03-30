package com.ey.tax.service;

import com.ey.tax.vo.MenuVo;

import java.util.List;

public interface IMenuService {
    List<MenuVo> loadMenus(Long userId);
}
