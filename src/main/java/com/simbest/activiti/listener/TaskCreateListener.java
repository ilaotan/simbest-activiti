/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.listener;

import com.simbest.activiti.listener.jobs.TaskCreateJob;
import com.simbest.cores.utils.SpringContextUtil;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-10  23:24
 */
@Component
public class TaskCreateListener implements ActivitiEventListener {

    @Autowired
    private SpringContextUtil context;

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
        Object entity = entityEvent.getEntity();
        if (entity instanceof TaskEntity) {
            TaskEntity task = (TaskEntity) entity;
            TaskService taskService = event.getEngineServices().getTaskService();
            ExecutionEntity execution = task.getProcessInstance();
            try {
                TaskCreateJob job = (TaskCreateJob) context.getBeanByClass(TaskCreateJob.class);
                job.execution(event.getEngineServices(), task, execution);
            }catch (NoSuchBeanDefinitionException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

}
