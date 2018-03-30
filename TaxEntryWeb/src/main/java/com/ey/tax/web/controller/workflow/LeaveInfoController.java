package com.ey.tax.web.controller.workflow;

import com.ey.tax.service.ILeaveServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhuji on 3/21/2018.
 */
@RestController
public class LeaveInfoController {

    @RequestMapping("/hello")
    public Object sayHell(){
        return "hello activiti.";
    }

    @Autowired
    private ILeaveServer leaveServer;

    @RequestMapping("/addLeaveInfo")
    public Object addLeaveInfo(String msg){
        leaveServer.addLeaveInfo(msg);
        return "新增成功";
    }
    @RequestMapping("/queryTaskByUserId")
    public Object queryTaskByUserId(String userId){
        leaveServer.queryByUserId(userId);
        return "";
    }

    @RequestMapping("/complete")
    public Object complete(String taskId,String userId, String audit){
        leaveServer.complete(taskId,userId,audit);
        return "审批完成";
    }
}
