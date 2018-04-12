package com.ey.tax.service;

import com.ey.tax.model.UserModel;
import com.ey.tax.vo.UserInfoVo;

import java.util.List;

/**
 * Created by zhuji on 3/27/2018.
 */
public interface IUserService {
    UserInfoVo findUserById(Long userId);

    List<UserInfoVo> findAllUserList();
}
