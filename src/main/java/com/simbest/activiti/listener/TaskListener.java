/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.listener;

import com.simbest.activiti.business.IBusinessService;
import com.simbest.activiti.business.ICheckUserAgentService;
import com.simbest.activiti.exceptions.NotFoundAssigneeException;
import com.simbest.activiti.exceptions.NotFoundBusinessException;
import com.simbest.activiti.listener.jobs.TaskCompletedJob;
import com.simbest.activiti.listener.jobs.TaskCreateJob;
import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.activiti.query.model.ActTaskAssigne;
import com.simbest.activiti.query.service.IActBusinessStatusService;
import com.simbest.activiti.query.service.IActTaskAssigneService;
import com.simbest.activiti.query.service.ICustomTaskService;
import com.simbest.cores.admin.authority.model.ShiroUser;
import com.simbest.cores.admin.authority.service.ISysGroupAdvanceService;
import com.simbest.cores.exceptions.Exceptions;
import com.simbest.cores.utils.DateUtil;
import com.simbest.cores.utils.SpringContextUtil;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用途：监听记录任务签收、任务分配、任务委托的人员
 * 作者: lishuyi
 * 时间: 2016-08-10  15:36
 */

/**
 * Activiti事件监听顺序
 * VARIABLE_CREATED
 * ENTITY_INITIALIZED
 * HISTORIC_PROCESS_INSTANCE_CREATED
 * HISTORIC_ACTIVITY_INSTANCE_CREATED
 * PROCESS_STARTED
 * ACTIVITY_STARTED
 * HISTORIC_ACTIVITY_INSTANCE_ENDED
 * SEQUENCEFLOW_TAKEN
 * TASK_CREATED
 * TASK_ASSIGNED
 * ENTITY_UPDATED
 * TASK_COMPLETED
 * ENTITY_DELETED
 */
@Component
public class TaskListener implements ActivitiEventListener {
    public transient final Log log = LogFactory.getLog(getClass());

    @Autowired
    private IActTaskAssigneService assigneService;

    @Autowired
    private IActBusinessStatusService statusService;

    @Autowired
    private SpringContextUtil context;

    @Autowired
    private ICustomTaskService taskService;

    @Autowired
    private ISysGroupAdvanceService groupAdvanceService;

    int ret = 0;
    TaskEntity task = null;
    IdentityLinkEntity link = null;
    List<String> assigneeAndCandidates = null;

    @Override
    public void onEvent(ActivitiEvent event) {
        ActBusinessStatus businessStatus = null;
        ActivitiEventType eventType = event.getType();
        ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
        switch (eventType) {
            case TASK_CREATED:
                task = (TaskEntity) entityEvent.getEntity();
                //检查用户代理
                checkUserAgent(task,task.getAssignee(),event.getEngineServices().getTaskService());
                //更新业务全局状态表任务信息
                businessStatus = updateBusinessTaskInfo(task);
                //通知生成待办
                assigneeAndCandidates = assigneService.queryToDoUser(task.getId());
                for (String user : assigneeAndCandidates) {
                    createUserTaskCallback(businessStatus, user);
                }
                break;
            case TASK_ASSIGNED: //监听记录任务签收claim、任务分配setAssignee、任务委托的人员delegateTask，但不记录任务候选人/组addCandidateUser/Group，以便用于查询我的已办
                task = (TaskEntity) entityEvent.getEntity();
                ActBusinessStatus oldBusiness = statusService.getByInstance(task.getProcessDefinitionId(), task.getProcessInstanceId());
                removeUserTaskCallback(oldBusiness, oldBusiness.getTaskAssignee()); //用BusinessStatus的Assignee删除原办理人待办

                //2.更新业务全局状态表任务信息，
                businessStatus = updateBusinessTaskInfo(task);

                //3.推送新办理人待办
                if (StringUtils.isNotEmpty(task.getAssignee())) {
                    createUserTaskCallback(businessStatus, task.getAssignee()); //使用task的Assignee推送新办理人待办
                }

                //4.记录新办理人，以便用于查询我的已办
                ActTaskAssigne taskAssigne = new ActTaskAssigne();
                taskAssigne.setProcessDefinitionId(task.getProcessDefinitionId());
                taskAssigne.setProcessInstanceId(task.getProcessInstanceId());
                taskAssigne.setExecutionId(task.getExecutionId());
                taskAssigne.setTaskId(task.getId());
                taskAssigne.setOwner(task.getOwner());
                taskAssigne.setAssignee(task.getAssignee());
                taskAssigne.setHandleTime(DateUtil.getCurrent());
                ret = assigneService.create(taskAssigne);
                log.debug(ret);
                break;
            case TASK_COMPLETED:
                task = (TaskEntity) entityEvent.getEntity();
                if (StringUtils.isEmpty(task.getAssignee())) //任务必须通过claim或者setAssignee设置办理人才能完成
                    throw new NotFoundAssigneeException();

                //更新业务全局状态表任务信息
                businessStatus = updateBusinessTaskInfo(task);

                //通知撤销待办
                assigneeAndCandidates = assigneService.queryToDoUser(task.getId());
                for (String user : assigneeAndCandidates) {
                    removeUserTaskCallback(businessStatus, user);
                }
                break;
            case ENTITY_INITIALIZED:
                //不做任何处理，因为ENTITY_INITIALIZED事件早于所有事件，因此由于事务未提交，查询不到任何数据，没有办法调用发起人待办
                break;
            case ENTITY_DELETED:
                if (entityEvent.getEntity() instanceof IdentityLinkEntity) {
                    link = (IdentityLinkEntity) entityEvent.getEntity();
                    log.debug(link);
                }
                break;
            default:
                log.debug("------------------------------------------------");
                log.debug("捕获到事件[Do Nothing]：" + eventType.name() + ", type=" + event.getType() + " ToString is :" + ToStringBuilder.reflectionToString(event));

        }
    }

    @Override
    public boolean isFailOnException() {
        return true;
    }

    private ActBusinessStatus updateBusinessTaskInfo(TaskEntity task) {
        int ret = 0;
        ActBusinessStatus businessStatus = statusService.getByInstance(task.getProcessDefinitionId(), task.getProcessInstanceId());
        if (businessStatus != null) {
            businessStatus.setExecutionId(task.getExecutionId());
            businessStatus.setTaskId(task.getId());
            businessStatus.setTaskKey(task.getTaskDefinitionKey());
            businessStatus.setTaskName(task.getName());
            businessStatus.setTaskOwner(task.getOwner());
            businessStatus.setTaskAssignee(task.getAssignee());
            businessStatus.setDelegationState(task.getDelegationState());
            //businessStatus.setTaskStartTime(task.getCreateTime());
            businessStatus.setTaskStartTime(DateUtil.getCurrent());
            Object currentSubject = SecurityUtils.getSubject().getPrincipal();
            if (currentSubject != null) {
                ShiroUser currentUser = (ShiroUser) currentSubject;
                businessStatus.setPreviousAssignee(currentUser.getUserId());
                businessStatus.setPreviousAssigneeUniqueCode(currentUser.getUniqueCode());
                businessStatus.setPreviousAssigneeName(currentUser.getUserName());
                businessStatus.setPreviousAssigneeDate(DateUtil.getCurrent());
            }
            ret = statusService.update(businessStatus);
            log.debug(ret);
        }
        return ret > 0 ? businessStatus : null;
    }

    private void createUserTaskCallback(ActBusinessStatus businessStatus, String uniqueCode) {
        if (businessStatus == null)
            throw new NotFoundBusinessException();
        try {
            TaskCreateJob job = (TaskCreateJob) context.getBeanByClass(TaskCreateJob.class);
            job.execution(businessStatus, uniqueCode);
        } catch (Exception e) {
            log.error("@@@@Error:" + Exceptions.getStackTraceAsString(e));
        }
    }

    private void createGroupTaskCallback(ActBusinessStatus businessStatus, String groupId) {
        if (businessStatus == null)
            throw new NotFoundBusinessException();
        List<String> uniqueCodes = groupAdvanceService.getGroupUser(groupId);
        for (String user : uniqueCodes) {
            createUserTaskCallback(businessStatus, user);
        }
    }

    private void removeUserTaskCallback(ActBusinessStatus businessStatus, String uniqueCode) {
        if (businessStatus == null)
            throw new NotFoundBusinessException();
        try {
            TaskCompletedJob job = (TaskCompletedJob) context.getBeanByClass(TaskCompletedJob.class);
            job.execution(businessStatus, uniqueCode);
        } catch (Exception e) {
            log.error("@@@@Error:" + Exceptions.getStackTraceAsString(e));
        }
    }

    private void removeGroupTaskCallback(ActBusinessStatus businessStatus, String groupId) {
        if (businessStatus == null)
            throw new NotFoundBusinessException();
        List<String> uniqueCodes = groupAdvanceService.getGroupUser(groupId);
        for (String user : uniqueCodes) {
            removeUserTaskCallback(businessStatus, user);
        }
    }

    private void checkUserAgent(TaskEntity task, String asignee, TaskService taskService){
        if(StringUtils.isNotEmpty(asignee)) {
            Object bean = context.getBeanByClass(ICheckUserAgentService.class);
            if (bean != null) {
                ICheckUserAgentService checkService = (ICheckUserAgentService) bean;
                if (checkService != null) {
                    String agentCode = checkService.getUserAgentCode(asignee);
                    if(StringUtils.isNotEmpty(agentCode)){
                        taskService.delegateTask(task.getId(), agentCode);
                    }
                }
            }
        }
    }
}
