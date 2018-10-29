/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.listener;

import com.simbest.activiti.business.ICheckUserAgentService;
import com.simbest.activiti.exceptions.NotFoundAssigneeException;
import com.simbest.activiti.listener.jobs.UserTaskSubmitor;
import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.activiti.query.model.ActTaskAssigne;
import com.simbest.activiti.query.service.IActBusinessStatusService;
import com.simbest.activiti.query.service.IActTaskAssigneService;
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
    private SpringContextUtil context;

    @Autowired
    private IActTaskAssigneService assigneService;

    @Autowired
    private IActBusinessStatusService statusService;

    @Autowired
    private UserTaskSubmitor userTaskSubmitor;


    int ret = 0;
//    TaskEntity task = null;//全局变量写在执行体中
    IdentityLinkEntity link = null;
    List<String> assigneeAndCandidates = null;

    /**
     * 任务生成时，通过queryCandidate推送待办通知至候选人
     * 任务分配时，通过getAssignee推送待办通知给办理人
     * 任务分配时，若owner不为空，说明存在委托，查询以前办理人，并撤销待办通知
     * 任务完成时，通过queryToDoUser撤销办理人和代办人待办通知
     *
     * @param event
     */
    @Override
    public void onEvent(ActivitiEvent event) {
        ActBusinessStatus businessStatus = null;
        ActivitiEventType eventType = event.getType();
        ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
        TaskEntity task = new TaskEntity();
        switch (eventType) {
            case TASK_CREATED:
            	task = (TaskEntity) entityEvent.getEntity();
                //检查办理人代理设置情况，如有代理人，任务推送至代理人，不推送原办理人
                checkUserAgent(task, task.getAssignee(), event.getEngineServices().getTaskService());
                //更新业务全局状态表任务信息
                businessStatus = statusService.updateBusinessTaskInfo(task);
                //通知生成待办
                assigneeAndCandidates = assigneService.queryCandidate(task.getId()); //TASK_CREATED推送候选人，TASK_ASSIGNED推送首次办理人及后续转办代理人
                for (String user : assigneeAndCandidates) {
                	log.error("0TASK_CREATED:"+";taskId:"+task.getId()+";taskAss:"+task.getAssignee()+";user:"+user);
                    userTaskSubmitor.createUserTaskCallback(businessStatus, user);
                }
                break;
            case TASK_ASSIGNED: //监听记录任务签收claim、任务分配setAssignee、任务委托的人员delegateTask，但不记录任务候选人/组addCandidateUser/Group，以便用于查询我的已办
                task = (TaskEntity) entityEvent.getEntity();
                String assigneeUser = task.getAssignee();
                ActBusinessStatus oldBusiness = statusService.getByInstance(task.getProcessInstanceId());
                
                //任务owner不为空，说明任务存在委托 
                //处理逻辑：act_task_assigne表中查询所有者是owner的记录，取出当前记录，更新完成时间和取消受让人的待办
                //委派delegateTask的时候不会查出任何数据，因为act_task_assigne事务没有提交，还没有生成当前TaskId数据
                //所以该处理逻辑是 被委派人完成任务把任务还给所有者的时候有意义resolveTask
                if (StringUtils.isNotEmpty(task.getOwner())) { 
                    ActTaskAssigne o = new ActTaskAssigne();
                    o.setProcessInstanceId(task.getProcessInstanceId());
                    o.setExecutionId(task.getExecutionId());
                    o.setTaskId(task.getId());
                    o.setOwner(task.getOwner());
                    List<ActTaskAssigne> delegateTask= (List<ActTaskAssigne>) assigneService.getAll(o);
                    if(delegateTask!=null && delegateTask.size()>0){
                    	String oldAssignee = delegateTask.get(0).getAssignee();
                    	 if (oldBusiness != null && StringUtils.isNotEmpty(oldAssignee)){
//                    		 delegateTask.get(0).setCompleteTime(DateUtil.getCurrent());
//                             assigneService.update(delegateTask.get(0));
                    		 userTaskSubmitor.removeUserTaskCallback(oldBusiness,oldAssignee);
                    	 }
                    }
                    
                    
                }
                /*如果任务所有者把任务交给受让人，那么要取消所有者的待办*/
                if(StringUtils.isNotEmpty(task.getOwner()) && !task.getOwner().equals(assigneeUser)){
                	 userTaskSubmitor.removeUserTaskCallback(oldBusiness,task.getOwner());
                }
                //2.更新业务全局状态表任务信息，
                businessStatus = statusService.updateBusinessTaskInfo(task);
                //3.推送新办理人待办
                if (StringUtils.isNotEmpty(assigneeUser)) {
                    userTaskSubmitor.createUserTaskCallback(businessStatus, assigneeUser); //使用task的Assignee推送新办理人待办
                }

                if (StringUtils.isEmpty(task.getOwner()) || !task.getOwner().equals(assigneeUser)) { //还给任务所有者的时候不需要再创建新任务 这个时候 owner = assignee
	                //4.记录新办理人，以便用于查询我的已办
	                ActTaskAssigne taskAssigne = new ActTaskAssigne();
	                taskAssigne.setProcessDefinitionId(task.getProcessDefinitionId());
	                taskAssigne.setProcessInstanceId(task.getProcessInstanceId());
	                taskAssigne.setExecutionId(task.getExecutionId());
	                taskAssigne.setTaskId(task.getId());
	                taskAssigne.setOwner(task.getOwner());
	                taskAssigne.setAssignee(task.getAssignee());
	                taskAssigne.setAssignTime(DateUtil.getCurrent());
	                ret = assigneService.create(taskAssigne);
                }
                break;
            case TASK_COMPLETED:
                task = (TaskEntity) entityEvent.getEntity();
                if (StringUtils.isEmpty(task.getAssignee())) { //任务必须通过claim或者setAssignee设置办理人才能完成
                    throw new NotFoundAssigneeException();
                }
//                if(task.getAssignee().equals(appUserSession.getCurrentUser().getUniqueCode())){
//                    throw new AssigneeIsNotCurrentUserException();
//                }

                //更新业务全局状态表任务信息
                businessStatus = statusService.updateBusinessTaskInfo(task);

                //更新已办处理完成时间
                ActTaskAssigne unCompleteTask = assigneService.getActTaskAssigne(task.getProcessDefinitionId(), task.getProcessInstanceId(), task.getExecutionId(), task.getId(), task.getAssignee());
                if (unCompleteTask != null) {
                    unCompleteTask.setCompleteTime(DateUtil.getCurrent());
                    assigneService.update(unCompleteTask);
                }

                //通知撤销待办
                assigneeAndCandidates = assigneService.queryToDoUser(task.getId());
                for (String user : assigneeAndCandidates) {
                    userTaskSubmitor.removeUserTaskCallback(businessStatus, user);
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

    private void checkUserAgent(TaskEntity task, String asignee, TaskService taskService) {
        if (StringUtils.isNotEmpty(asignee)) {
            Object bean = context.getBeanByClass(ICheckUserAgentService.class);
            if (bean != null) {
                ICheckUserAgentService checkService = (ICheckUserAgentService) bean;
                if (checkService != null) {
                    String agentCode = checkService.getUserAgentCode(asignee, task.getCreateTime());
                    if (StringUtils.isNotEmpty(agentCode)) {
                        taskService.delegateTask(task.getId(), agentCode);
                    }
                }
            }
        }
    }
}
