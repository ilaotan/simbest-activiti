/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.apis.impl;

import com.google.common.collect.Maps;
import com.simbest.activiti.apis.DefinitionApi;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-17  22:50
 */
@Component
public class DefinitionApiImpl implements DefinitionApi {
    private final Map<String, ProcessDefinitionEntity> processDefinitionEntitys = Maps.newHashMap();

    private final Map<String, ProcessDefinition> processDefinitions = Maps.newHashMap();

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    /**
     * 获得流程定义实体
     *
     * @param processDefinitionId
     * @return
     */
    public ProcessDefinitionEntity getDefinitionEntity(String processDefinitionId) {
        ProcessDefinitionEntity entity = processDefinitionEntitys.get(processDefinitionId);
        if (entity == null) {
            ProcessDefinitionEntity loadedProcessDefinition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
            processDefinitions.put(processDefinitionId, loadedProcessDefinition);
            entity = loadedProcessDefinition;
        }
        return entity;
    }

    /**
     * 获得流程定义
     *
     * @param processDefinitionId
     * @return
     */
    public ProcessDefinition getDefinition(String processDefinitionId) {
        ProcessDefinition definition = processDefinitions.get(processDefinitionId);
        if (definition == null) {
            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()//创建流程定义查询对象，对应表act_re_procdef
                    .processDefinitionId(processDefinitionId)//使用流程定义ID查询
                    .singleResult();
            processDefinitions.put(processDefinitionId, definition);
            definition = pd;
        }
        return definition;
    }

    /**
     * 根据实例，获得流程定义
     *
     * @param processInstanceId
     * @return
     */
    public ProcessDefinition getDefinitionByInstance(String processInstanceId) {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        return getDefinition(pi.getProcessDefinitionId());
    }

    /**
     * 根据任务，获得流程定义
     *
     * @param taskId
     * @return
     */
    public ProcessDefinition getDefinitionByTask(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return getDefinition(task.getProcessDefinitionId());
    }

    /**
     * 根据流程定义Key，删除流程定义
     * @param deploymentKey
     */
    public void deleteDefinitionByKey(String deploymentKey){
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionKey(deploymentKey).list();
        for(ProcessDefinition pd :list){
            repositoryService.deleteDeployment(pd.getDeploymentId(), true);
        }
    }
}
