/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.apis;

import com.google.common.collect.Maps;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-17  22:50
 */
public interface DefinitionApi {

    /**
     * 获得流程定义实体
     *
     * @param processDefinitionId
     * @return
     */
    ProcessDefinitionEntity getDefinitionEntity(String processDefinitionId);

    /**
     * 获得流程定义
     *
     * @param processDefinitionId
     * @return
     */
    ProcessDefinition getDefinition(String processDefinitionId);

    /**
     * 根据实例，获得流程定义
     *
     * @param processInstanceId
     * @return
     */
    ProcessDefinition getDefinitionByInstance(String processInstanceId);

    /**
     * 根据任务，获得流程定义
     *
     * @param taskId
     * @return
     */
    ProcessDefinition getDefinitionByTask(String taskId);

    /**
     * 根据流程定义Key，删除流程定义
     * @param deploymentKey
     */
    void deleteDefinitionByKey(String deploymentKey);
}
