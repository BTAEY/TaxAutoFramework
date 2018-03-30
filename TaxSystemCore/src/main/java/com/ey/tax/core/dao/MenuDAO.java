package com.ey.tax.core.dao;

import com.ey.tax.model.MenuModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhuji on 3/26/2018.
 */
@Repository
public interface MenuDAO {
    List<MenuModel> findMenuItemsByUserId(Long userId);
}
