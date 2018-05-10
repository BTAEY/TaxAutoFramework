package com.ey.tax.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HtmlUtil;
import com.ey.tax.common.AttachmentEnums;
import com.ey.tax.core.dao.WorkFlowDAO;
import com.ey.tax.entity.AttachmentStore;
import com.ey.tax.enums.WorkFlowState;
import com.ey.tax.model.CommentModel;
import com.ey.tax.utils.PropertiesUtil;
import com.ey.tax.utils.StringUtil;
import com.ey.tax.vo.HistoryCommentVo;
import com.ey.tax.vo.WorkflowInfo;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowElementsContainer;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.GroupEntityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by zhuji on 4/10/2018.
 */
@Service
public class WorkflowFacadeService {
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private WorkFlowDAO workFlowDAO;

    @Autowired
    private Mapper mapper;

    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;

    public void deploy(String wfKey) {
        String resourcePath = PropertiesUtil.getString(wfKey);
        repositoryService.createDeployment().addClasspathResource(resourcePath).name("UserDeployManually").key(wfKey).deploy();
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

    public Task findTask(String taskId){
        return taskService.createTaskQuery().taskId(taskId).singleResult();
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

    public ProcessInstance findProcessInstance(String procInst){
        return runtimeService.createProcessInstanceQuery().processInstanceId(procInst).singleResult();
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
     * 获取流程跟踪图
     * @param procInstId
     * @return
     */
    public InputStream getWorkFlowProgressImage(String procInstId){
        //  获取历史流程实例
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(procInstId).singleResult();

        // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInstId).orderByHistoricActivityInstanceId().asc().list();

        // 已执行的节点ID集合
        List<String> executedActivityIdList = new ArrayList<String>();
        int index = 1;
        for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
            executedActivityIdList.add(activityInstance.getActivityId());

            logger.debug("第[" + index + "]个已执行节点=" + activityInstance.getActivityId() + " : " +activityInstance.getActivityName());
            index++;
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());

        FlowElementsContainer flowElementsContainer = bpmnModel.getMainProcess();
        // 已执行的线集合
        List<String> flowIds = getHighLightedFlows(flowElementsContainer,historicActivityInstanceList);

        // 获取流程图图像字符流
        ProcessDiagramGenerator pec = processEngineConfiguration.getProcessDiagramGenerator();
        //配置字体
        InputStream imageStream = pec.generateDiagram(bpmnModel, "png", executedActivityIdList, flowIds,"宋体","微软雅黑","黑体",null,2.0);

        return imageStream;
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
     * 根据Key查找工作流定义信息
     *
     * @param wfKey
     * @return
     */
    public WorkflowInfo getWorkFlowInfoByKey(String wfKey) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(wfKey).latestVersion().singleResult();
        return convertProcessDefinition2WorkflowInfo(processDefinition);
    }

    /**
     * 根据流程定义Id查看流程定义信息
     * @param processDefinitionId
     * @return
     */
    public WorkflowInfo getWorkFlowInfoById(String processDefinitionId){
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        return convertProcessDefinition2WorkflowInfo(processDefinition);
    }

    /**
     * 流程定义基本信息转换
     * @param processDefinition
     * @return
     */
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
     * 获取所有已完结工作流
     *
     * @return
     */
    public List<WorkflowInfo> getHistoricWorkFlows(boolean finished) {
        List<HistoricProcessInstance> processInstances;
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
        if(finished){
            processInstances = query.finished().orderByProcessInstanceId().asc().list();
        }else{
            processInstances = query.unfinished().orderByProcessInstanceId().asc().list();
        }
        List<WorkflowInfo> workflowInfoList = processInstances.stream().map(i -> {
            WorkflowInfo.Builder builder = WorkflowInfo.createBuilder();
            builder.procInstId(i.getId())
                    .name(i.getProcessDefinitionName())
                    .startTime(i.getStartTime())
                    .endTime(i.getEndTime())
                    .key(i.getProcessDefinitionKey())
                    .businessKey(i.getBusinessKey());
            if(i.getDurationInMillis() != null){
                builder.duration(i.getDurationInMillis() / 1000);
            }
            WorkflowInfo workflowInfo = builder.build();
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

    /**
     * 根据任务Id 查找附件
     * @param taskId
     * @return
     */
    public List<Attachment> getAttachmentByTaskId(String taskId){
        List<Attachment> attachments = taskService.getTaskAttachments(taskId);

        return attachments;
    }

    public Attachment getAttachmentById(String attachmentId){
        return taskService.getAttachment(attachmentId);
    }

    /**
     * 根据流程实例Id 查找附件
     * @param procInstId
     * @return
     */
    public List<Attachment> getAttachmentByProcInstId(String procInstId){
        List<Attachment> attachments = taskService.getProcessInstanceAttachments(procInstId);

        return attachments;
    }

    /**
     * 指定任务办理人
     * @param taskId
     * @param userId
     */
    public void assignTask(String taskId,String userId){
        taskService.setAssignee(taskId,userId);
    }

    /**
     * 拾取任务，将组任务分配给个人任务
     * @param taskId
     */
    public void claimTask(String taskId,String userId){
        //可以分配给组任务中的成员，也可以使非组任务中的成员
        taskService.claim(taskId,userId);
    }

    public String findTaskAssignmentType(String taskId){
        List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(taskId);
        if(CollectionUtil.isNotEmpty(identityLinks)){
            for(IdentityLink identityLink:identityLinks){
               if(IdentityLinkType.CANDIDATE.equals(identityLink.getType())){
                   return IdentityLinkType.CANDIDATE;
               }else if(IdentityLinkType.ASSIGNEE.equals(identityLink.getType())){
                   return IdentityLinkType.ASSIGNEE;
               }
            }
        }
        return null;
    }


    /**
     * 查看流程实例是否结束
     *
     * @param procInstId
     * @return
     */
    public Boolean isFinished(String procInstId) {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();
        if (pi == null || pi.isEnded()) {
            logger.debug("流程已经结束");
            return true;
        }
        logger.debug("流程没有结束");
        return false;
    }

    /**
     * 获取流程状态
     * @param procInstId
     * @return
     */
    public WorkFlowState getWorkFlowState(String procInstId){
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();

        if(pi != null && pi.isSuspended()){
            return WorkFlowState.Suspended;
        }
        if(pi != null && pi.isEnded()){
            return WorkFlowState.Finished;
        }
        Job deadLetterJob = managementService.createDeadLetterJobQuery().processInstanceId(procInstId).singleResult();
        if(deadLetterJob != null){
            return WorkFlowState.DeadLetter;
        }
        return WorkFlowState.UnderWay;
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
     * 查询[任务/流程]变量
     */
    public <T> T queryProcessVariable(String executionId,String variableName) {
        return (T) runtimeService.getVariable(executionId,variableName); //执行对象id和流程变量名称获取变量值
//        runtimeService.getVariables(executionId); 获取所有的流程变量
//        runtimeService.getVariables(executionId,variableNames); 使用执行对象ID，通过设置流程变量的名称存放在集合中，获取指定变量名称的值
    }

    /**
     * 向组任务中添加成员
     * @param taskId
     * @param userId
     */
    public void addGroupUser(String taskId,String userId){
        taskService.addCandidateUser(taskId,userId);
    }

    /**
     * 将成员从组任务中删除
     * @param taskId
     * @param userId
     */
    public void deleteGroupUser(String taskId,String userId){
        taskService.deleteCandidateUser(taskId,userId);
    }

    /**
     * 添加用户角色组
     */
    public void addGroup(){
        //创建角色
        identityService.saveGroup(new GroupEntityImpl());
        identityService.createMembership("userId","groupId");
    }

    public List<String> getHighLightedFlows(FlowElementsContainer process,List<HistoricActivityInstance> historicActivityInstances){
        List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //24小时制
        // 对历史流程节点进行遍历
        for (int i = 0; i < historicActivityInstances.size() - 1; i++){
            HistoricActivityInstance currentActivityInst = historicActivityInstances.get(i);
            String activityId = currentActivityInst.getActivityId();
            // 得到节点定义的详细信息
            FlowNode activityImpl = (FlowNode)process.getFlowElement(activityId);
            // 表示当前节点属于内嵌子流程节点
            if(activityImpl == null){
                continue;
            }
            //判断是否有子流程
            if(activityImpl instanceof SubProcess){
                final SubProcess subProcess = (SubProcess) activityImpl;
                List<HistoricActivityInstance> subProcessHiActivityInstances = historicActivityInstances.stream().filter((historicActivityInstance) -> {
                    Map<String,FlowElement> subFlowElementMap = subProcess.getFlowElementMap();
                    if(subFlowElementMap.containsKey(historicActivityInstance.getActivityId())){
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
                List<String> subHighlightFlows = getHighLightedFlows(subProcess,subProcessHiActivityInstances);
                highFlows.addAll(subHighlightFlows);
            }

            List<FlowNode> sameStartTimeNodes = new ArrayList<>();// 用以保存后续开始时间相同的节点
            FlowNode sameActivityImpl1 = null;
            HistoricActivityInstance activityImpl_ = currentActivityInst;// 当前节点
            //往后遍历历史节点
            for(int k = i + 1 ; k <= historicActivityInstances.size() - 1; k++){

                HistoricActivityInstance activityImp2_ = historicActivityInstances.get(k);// 后续第1个节点

                if ( activityImpl_.getActivityType().equals("userTask") && activityImp2_.getActivityType().equals("userTask") &&
                        df.format(activityImpl_.getStartTime()).equals(df.format(activityImp2_.getStartTime()))   ) //都是usertask，且主节点与后续节点的开始时间相同，说明不是真实的后继节点
                {
                    continue;
                }
                else
                {
                    sameActivityImpl1 = (FlowNode)process.getFlowElement(historicActivityInstances.get(k).getActivityId());//找到紧跟在后面的一个节点
                    break;
                }
            }
            // 将后面第一个节点放在时间相同节点的集合里
            sameStartTimeNodes.add(sameActivityImpl1);

            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++){
                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点

                if (df.format(activityImpl1.getStartTime()).equals(df.format(activityImpl2.getStartTime()))  )
                {// 如果第一个节点和第二个节点开始时间相同保存
                    FlowNode sameActivityImpl2 = (FlowNode)process.getFlowElement(activityImpl2.getActivityId());
                    sameStartTimeNodes.add(sameActivityImpl2);
                }
                else
                {// 有不相同跳出循环
                    break;
                }
            }
            // 取出节点的所有出去的线
            List<SequenceFlow> pvmTransitions = activityImpl.getOutgoingFlows() ;

            // 对所有的线进行遍历
            for (SequenceFlow pvmTransition : pvmTransitions){
                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                FlowNode pvmActivityImpl = (FlowNode)process.getFlowElement(pvmTransition.getTargetRef());
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }
        }

        return highFlows;
    }
}
