package com.ey.tax.service.impl;

import com.ey.tax.core.dao.PrivilegeDAO;
import com.ey.tax.model.UserModel;
import com.ey.tax.service.IUserService;
import com.ey.tax.vo.UserInfoVo;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhuji on 3/27/2018.
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private PrivilegeDAO privilegeDAO;

    @Autowired
    private Mapper mapper;

    @Override
    public UserInfoVo findUserById(Long userId) {
        UserModel userModel = privilegeDAO.findUserById(userId);
        UserInfoVo userInfo = mapper.map(userModel,UserInfoVo.class);
        return userInfo;
    }
}
