package com.ey.tax.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HtmlUtil;
import com.ey.tax.common.AttachmentEnums;
import com.ey.tax.core.dao.WorkFlowDAO;
import com.ey.tax.entity.AttachmentStore;
import com.ey.tax.model.CommentModel;
import com.ey.tax.utils.PropertiesUtil;
import com.ey.tax.utils.StringUtil;
import com.ey.tax.vo.HistoryCommentVo;
import com.ey.tax.vo.WorkflowInfo;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by zhuji on 4/10/2018.
 */
@Service
public class WorkflowFacadeService {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private WorkFlowDAO workFlowDAO;

    @Autowired
    private Mapper mapper;

    /**
     * 根据 process key 启动工作流
     *
     * @param processName
     * @return
     */
    public ProcessInstance startProcess(String processName) {
        return runtimeService.startProcessInstanceByKey(processName);
    }

    /**
     * 根据 process key 启动工作流 , 带启动参数
     *
     * @param processName
     * @param variables
     * @return
     */
    public ProcessInstance startProcess(String processName, Map<String, Object> variables) {
        return runtimeService.startProcessInstanceByKey(processName, variables);
    }

    public void deploy(String wfKey) {
        String resourcePath = PropertiesUtil.getString(wfKey);
        repositoryService.createDeployment().addClasspathResource(resourcePath).deploy();
    }

    /**
     * 查询指定用户的待办任务
     *
     * @param userId
     * @return
     */
    public List<Task> findTaskByUserId(String userId) {
        return taskService.createTaskQuery().taskCandidateOrAssigned(userId).list();
    }

    /**
     * 查找所有的工作流定义
     *
     * @return
     */
    public List<WorkflowInfo> findAllDefinedWorkflowList() {
        List<ProcessDefinition> procList = repositoryService.createProcessDefinitionQuery().latestVersion().list();
        List<WorkflowInfo> workflowInfoList = procList.stream().map(processDefinition -> {
            WorkflowInfo workflowInfo = WorkflowInfo.createBuilder()
                    .defineId(processDefinition.getId())
                    .name(processDefinition.getName())
                    .key(processDefinition.getKey())
                    .deploymentId(processDefinition.getDeploymentId())
                    .resourceName(StringUtil.getFilename(processDefinition.getResourceName()))
                    .resourcePng(StringUtil.getFilename(processDefinition.getDiagramResourceName()))
                    .build();
            return workflowInfo;
        }).collect(Collectors.toList());
        return workflowInfoList;
    }

    /**
     * 查看流程定义图片
     *
     * @param deploymentId
     * @return
     */
    public InputStream getWorkFLowImage(String deploymentId) {
        Optional<String> imageNameOptional = repositoryService.getDeploymentResourceNames(deploymentId).stream().filter(s -> s.indexOf(".png") >= 0).findFirst();
        InputStream in = repositoryService.getResourceAsStream(deploymentId, imageNameOptional.get());
        return in;
    }

    /**
     * 查看流程定义资源文件
     *
     * @param deploymentId
     * @return
     */
    public String getWorkFlowResource(String deploymentId) {
        Optional<String> bpmnNameOptional = repositoryService.getDeploymentResourceNames(deploymentId).stream().filter(s -> s.indexOf(".xml") >= 0).findFirst();
        InputStream in = repositoryService.getResourceAsStream(deploymentId, bpmnNameOptional.get());
        String content = HtmlUtil.escape(IoUtil.read(in, Charset.forName("UTF-8")));
        return content;
    }

    /**
     * 根据Key查找工作流基本信息
     *
     * @param wfKey
     * @return
     */
    public WorkflowInfo getWorkFlowInfoByKey(String wfKey) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(wfKey).singleResult();
        return convertProcessDefinition2WorkflowInfo(processDefinition);
    }

    /**
     * 根据Id查找工作流基本信息
     * @param procDefId
     * @return
     */
    public WorkflowInfo getWorkFlowInfoById(String procDefId){
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
        return convertProcessDefinition2WorkflowInfo(processDefinition);
    }

    private WorkflowInfo convertProcessDefinition2WorkflowInfo(ProcessDefinition processDefinition){
        if(processDefinition == null){
            return null;
        }
        return WorkflowInfo.createBuilder()
                .defineId(processDefinition.getId())
                .name(processDefinition.getName())
                .key(processDefinition.getKey())
                .deploymentId(processDefinition.getDeploymentId())
                .resourceName(StringUtil.getFilename(processDefinition.getResourceName()))
                .resourcePng(StringUtil.getFilename(processDefinition.getDiagramResourceName()))
                .build();

    }

    /**
     * 获取所有以完结工作流
     *
     * @return
     */
    public List<WorkflowInfo> getHistoricWorkFlowInfos() {
        List<HistoricProcessInstance> processInstances = historyService.createHistoricProcessInstanceQuery()
                .finished().orderByProcessInstanceId().asc().list();

        List<WorkflowInfo> workflowInfoList = processInstances.stream().map(i -> {
            WorkflowInfo workflowInfo = WorkflowInfo.createBuilder()
                    .name(i.getProcessDefinitionName())
                    .duration(i.getDurationInMillis() / 1000)
                    .startTime(i.getStartTime())
                    .endTime(i.getEndTime())
                    .key(i.getProcessDefinitionKey())
                    .build();
            return workflowInfo;
        }).collect(Collectors.toList());
        return workflowInfoList;
    }

    /**
     * 完成任务
     *
     * @param taskId
     */
    public void completeTask(String taskId, TaskPropertyResolver propertyResolver) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        if (propertyResolver.comments() != null && propertyResolver.comments().length > 0) {
            for (String comment : propertyResolver.comments()) {
                taskService.addComment(taskId, processInstanceId, comment);
            }
        }

        if (propertyResolver.variables() != null) {
            taskService.setVariables(taskId, propertyResolver.variables());
        }

        if (CollectionUtil.isNotEmpty(propertyResolver.attachments())) {
            for (AttachmentStore attachmentStore : propertyResolver.attachments()) {
                if (AttachmentEnums.StoreType.Local.equals(attachmentStore.getStoreType())) {
                    taskService.createAttachment(attachmentStore.getAttachmentType().getCode().toString(), taskId, processInstanceId, attachmentStore.getAttachmentName(), "", attachmentStore.getPhysicalPath());
                } else {
                    taskService.createAttachment(attachmentStore.getAttachmentType().getCode().toString(), taskId, processInstanceId, attachmentStore.getAttachmentName(), "", attachmentStore.getRemotePath());
                }
            }
        }

        taskService.complete(taskId);
    }

    /**
     * 获取流程所有批注信息
     * @param procInstId
     * @return
     */
    public List<HistoryCommentVo> getProcessInstanceComments(String procInstId){
        List<CommentModel> commentModels = workFlowDAO.findCommentsByProcInstId(procInstId);
        List<HistoryCommentVo> commentVos = commentModels.stream().map(c -> {
            HistoryCommentVo commentVo = mapper.map(c,HistoryCommentVo.class);
            return commentVo;
        }).collect(Collectors.toList());
        return commentVos;
    }

    public String getProcessName(String procDefineId) {
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(procDefineId);
        if (processDefinition != null) {
            return processDefinition.getName();
        }
        return "";
    }

    /**
     * 查看流程实例是否结束
     *
     * @param procId
     * @return
     */
    public Boolean isFinished(String procId) {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procId).singleResult();
        if (pi == null) {
            System.out.println("流程结束");
            return true;
        }
        System.out.println("流程没有结束");
        return false;
    }

    /**
     * 查询办理人的历史任务
     *
     * @param procId
     * @param userId
     */
    public void queryHistoryTask(String procId, String userId) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(procId) //创建历史人物实例查询
                .taskAssignee(userId) //指定历史人物的办理人
                .list();
        if (CollectionUtils.isNotEmpty(list)) {
            for (HistoricTaskInstance hti : list) {
                System.out.println(hti.getId() + " , "
                        + hti.getName() + " , "
                        + hti.getProcessInstanceId() + " , "
                        + hti.getStartTime() + ", "
                        + hti.getEndTime() + ", "
                        + hti.getDurationInMillis());
            }
        }
    }

    /**
     * 查询历史流程实例
     *
     * @param procId
     */
    public void queryHistoryProcessInstance(String procId) {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(procId).list();

    }

    /**
     * 查询历史活动
     */
    public void queryHistoryActiviti(String procId) {
        historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();
    }

    /**
     * 查询[任务/流程]变量
     */
    public <T> T queryProcessVariable(String executionId,String variableName) {
        return (T) runtimeService.getVariable(executionId,variableName); //执行对象id和流程变量名称获取变量值
//        runtimeService.getVariables(executionId); 获取所有的流程变量
//        runtimeService.getVariables(executionId,variableNames); 使用执行对象ID，通过设置流程变量的名称存放在集合中，获取指定变量名称的值
    }

    public void setProcessVariable(String executionId) {
//        runtimeService.setVariable(executionId,variableName,value);//执行对象id，流程变量名称，流程变量的值 一次只能设置一个
//        runtimeService.setVariables(executionId,Map<>); // 执行对象id和map集合设置流程变量
//        runtimeService.setVariableLocal(executionId,variableName,value);  //与任务id绑定
    }

    public List<Comment> findCommentsByTaskId(String taskId) {

        return taskService.getTaskComments(taskId);
    }


}
