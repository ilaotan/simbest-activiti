/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.listener;

import com.simbest.activiti.listener.jobs.UserTaskSubmitor;
import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.activiti.query.model.ActTaskAssigne;
import com.simbest.activiti.query.service.IActBusinessStatusService;
import com.simbest.activiti.query.service.IActTaskAssigneService;
import com.simbest.cores.utils.DateUtil;
import org.activiti.engine.delegate.event.ActivitiCancelledEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用途：监听流程实例取消事件PROCESS_CANCELLED
 * 作者: lishuyi
 * 时间: 2016-12-13  17:29
 */

@Component
public class ProcessInstanceListener implements ActivitiEventListener {
    public transient final Log log = LogFactory.getLog(getClass());

    @Autowired
    private UserTaskSubmitor userTaskSubmitor;

    @Autowired
    private IActBusinessStatusService statusService;

    @Autowired
    private IActTaskAssigneService assigneService;

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEventType eventType = event.getType();
        ActivitiCancelledEvent entityEvent = (ActivitiCancelledEvent) event;
        switch (eventType) {
            case PROCESS_CANCELLED:
                log.warn("Process intance is canceled with instance id :" + entityEvent.getProcessInstanceId());
                List<Task> cancelTasks = event.getEngineServices().getTaskService().createTaskQuery().processInstanceId(entityEvent.getProcessInstanceId()).list();
                for (Task cancelTask : cancelTasks) {
                    //更新业务全局状态表任务信息
                    ActBusinessStatus businessStatus = statusService.updateBusinessTaskInfo(cancelTask);

                    //更新已办处理完成时间
                    ActTaskAssigne unCompleteTask = assigneService.getActTaskAssigne(cancelTask.getProcessDefinitionId(), cancelTask.getProcessInstanceId(), cancelTask.getExecutionId(), cancelTask.getId(), cancelTask.getAssignee());
                    if (unCompleteTask != null) {
                        unCompleteTask.setCompleteTime(DateUtil.getCurrent());
                        assigneService.update(unCompleteTask);
                    }

                    //通知撤销待办
                    List<String> assigneeAndCandidates = assigneService.queryToDoUser(cancelTask.getId());
                    for (String user : assigneeAndCandidates) {
                        userTaskSubmitor.removeUserTaskCallback(businessStatus, user);
                    }
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

}
