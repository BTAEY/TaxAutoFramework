package com.ey.tax.service;

import com.ey.tax.utils.StringUtil;
import com.ey.tax.vo.WorkflowInfo;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
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
}
