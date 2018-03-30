package com.ey.tax.act;

import com.ey.tax.core.dao.LeaveInfoDAO;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuji on 3/21/2018.
 */
@Service
public class LeaveService {
    @Autowired
    private LeaveInfoDAO leaveInfoDAO;

    @Resource
    private RuntimeService runtimeService;

    //${leaveService.findProjectManger(execution)}
    //查询相关项目经理
    public List<String> findProjectManager(DelegateExecution execution){
        //真实项目中查询人员表，返回项目经理的userId
        return Arrays.asList("project1","project2");
    }

    //${leaveService.findHr(execution)}
    public List<String> findHr(DelegateExecution execution){
        return Arrays.asList("hr1","hr2");
    }

    //#{leaveService.changeStatus(execution,'pass')}
    public void changeStatus(DelegateExecution execution,String status){
        System.out.println("修改业务单据状态 - "+status);
        String processBusinessKey = execution.getProcessInstanceBusinessKey();
        //get leaveInfo and update status
    }

    // 启动流程
    // bizKey ： 业务id
    public void startProcess(String bizkey){
        runtimeService.startProcessInstanceByKey("LeaveProcess",bizkey);
    }

    @Resource
    private TaskService taskService;

    //查询相关审批人
    public List<Task> findTaskByUserId(String userId){
        return taskService.createTaskQuery().taskCandidateOrAssigned(userId).list();
    }

    //审批

    /**
     *
     * @param taskId 审批哪个任务
     *               userId: 谁审批的
     *               audit: 通过，驳回
     */
    public void completeTaskByUser(String taskId,String userId, String audit){
        //1. 认领任务
        taskService.claim(taskId,userId);
        Map<String,Object> var = new HashMap<>();
        var.put("audit",audit);
        taskService.complete(taskId,var);
    }

}
