package com.ey.tax.service;

import com.ey.tax.entity.LeaveInfo;

import java.util.List;

public interface ILeaveServer {

    // 新增请假单
    void addLeaveInfo(String msg);

    //  查询代办
    List<LeaveInfo> queryByUserId(String userId);

    //完成任务
    void complete(String taskId,String userId,String audit);
}
