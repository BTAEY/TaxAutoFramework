package com.ey.tax.service.impl;

import com.ey.tax.core.dao.PrivilegeDAO;
import com.ey.tax.core.repository.SysUserRepository;
import com.ey.tax.entity.SysUser;
import com.ey.tax.model.UserModel;
import com.ey.tax.service.IUserService;
import com.ey.tax.vo.UserInfoVo;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhuji on 3/27/2018.
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private PrivilegeDAO privilegeDAO;

    @Autowired
    private Mapper mapper;

    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public UserInfoVo findUserById(Long userId) {
        UserModel userModel = privilegeDAO.findUserById(userId);
        UserInfoVo userInfo = mapper.map(userModel,UserInfoVo.class);
        return userInfo;
    }

    @Override
    public List<UserInfoVo> findAllUserList() {
        List<UserModel> userList = privilegeDAO.findAllUserList();
        List<UserInfoVo> userInfoList = userList.stream().map(item -> mapper.map(item,UserInfoVo.class)).collect(Collectors.toList());
        return userInfoList;
    }
}
