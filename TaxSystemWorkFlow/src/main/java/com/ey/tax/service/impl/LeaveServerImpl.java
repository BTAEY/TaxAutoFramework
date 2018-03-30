package com.ey.tax.service.impl;

import com.ey.tax.act.LeaveService;
import com.ey.tax.entity.LeaveInfo;
import com.ey.tax.service.ILeaveServer;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class LeaveServerImpl implements ILeaveServer {
    @Autowired
    private LeaveService leaveService;

    @Override
    public void addLeaveInfo(String msg) {
        LeaveInfo leaveInfo = new LeaveInfo();
        leaveInfo.setLeaveMsg(msg);
        leaveInfo.setId(1L);
        // insert to db

        //start process
        leaveService.startProcess("1");
    }

    @Resource
    private RuntimeService runtimeService;

    @Override
    public List<LeaveInfo> queryByUserId(String userId) {
        List<Task> taskByUserId = leaveService.findTaskByUserId(userId);
        List<LeaveInfo> result = new ArrayList<>();
        for(Task task: taskByUserId){
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            String businessKey = processInstance.getBusinessKey();
            //SELECT leave info
            //LeaveInfo leaveInfo =
            //leaveInfo.setTaskId(task.getId);
        }
        return result;
    }

    @Override
    public void complete(String taskId, String userId, String audit) {
        leaveService.completeTaskByUser(taskId,userId,audit);
    }
}
