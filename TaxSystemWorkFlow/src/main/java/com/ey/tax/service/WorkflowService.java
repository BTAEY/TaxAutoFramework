package com.ey.tax.service;

import com.ey.tax.utils.StringUtil;
import com.ey.tax.vo.WorkflowInfo;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zhuji on 4/10/2018.
 */
@Service
public class WorkflowService {
    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    public ProcessInstance startProcess(String processName){
        return runtimeService.startProcessInstanceByKey(processName);
    }

    public ProcessInstance startProcess(String processName,Map<String, Object> variables){
        return runtimeService.startProcessInstanceByKey(processName,variables);
    }

    //查询待办任务
    public List<Task> findTaskByUserId(String userId){
        return taskService.createTaskQuery().taskCandidateOrAssigned(userId).list();
    }

    public List<WorkflowInfo> findAllDefinedWorkflowList(){
        List<ProcessDefinition> procList = repositoryService.createProcessDefinitionQuery().latestVersion().list();
        List<WorkflowInfo> workflowInfoList = procList.stream().map(proc -> {
            WorkflowInfo workflowInfo = new WorkflowInfo();
            workflowInfo.setDefineId(proc.getId());
            workflowInfo.setName(proc.getName());
            workflowInfo.setKey(proc.getKey());
            workflowInfo.setDescription(proc.getDescription());
            workflowInfo.setDeploymentId(proc.getDeploymentId());
            workflowInfo.setResourceName(StringUtil.getFilename(proc.getResourceName()));
            workflowInfo.setResourcePng(StringUtil.getFilename(proc.getDiagramResourceName()));
            return workflowInfo;
        }).collect(Collectors.toList());
        return workflowInfoList;
    }

    public String getProcessName(String procDefineId){
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(procDefineId);
        if(processDefinition != null){
            return processDefinition.getName();
        }
        return "";
    }

    public void deploy(){
    }

    /**
     * 查看流程实例是否结束
     *
     */
    public Boolean isFinished(String procId){
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procId).singleResult();
        if(pi == null){
            System.out.println("流程结束");
            return true;
        }
        System.out.println("流程没有结束");
        return false;
    }

    /**
     * 查询办理人的历史任务
     * @param procId
     * @param userId
     */
    public void queryHistoryTask(String procId,String userId){
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(procId) //创建历史人物实例查询
                .taskAssignee(userId) //指定历史人物的办理人
                .list();
        if(CollectionUtils.isNotEmpty(list)){
            for(HistoricTaskInstance hti:list){
                System.out.println(hti.getId()+" , "
                        +hti.getName()+" , "
                        +hti.getProcessInstanceId()+" , "
                        +hti.getStartTime()+", "
                        +hti.getEndTime()+ ", "
                        +hti.getDurationInMillis());
            }
        }
    }

    /**
     * 查询历史流程实例
     * @param procId
     */
    public void queryHistoryProcessInstance(String procId){
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(procId).list();

    }

    /**
     * 查询历史活动
     */
    public void queryHistoryActiviti(String procId){
        historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();
    }

    /**
     * 查询[任务/流程]变量
     */
    public void queryProcessVariable(){
//        runtimeService.getVariable(executionId,variableName); 执行对象id和流程变量名称获取变量值
//        runtimeService.getVariables(executionId); 获取所有的流程变量
//        runtimeService.getVariables(executionId,variableNames); 使用执行对象ID，通过设置流程变量的名称存放在集合中，获取指定变量名称的值
    }

    public void setProcessVariable(String executionId){
//        runtimeService.setVariable(executionId,variableName,value);//执行对象id，流程变量名称，流程变量的值 一次只能设置一个
//        runtimeService.setVariables(executionId,Map<>); // 执行对象id和map集合设置流程变量
//        runtimeService.setVariableLocal(executionId,variableName,value);  //与任务id绑定
    }

    public List<Comment> findCommentsByTaskId(String taskId){

        return taskService.getTaskComments(taskId);
    }


}
