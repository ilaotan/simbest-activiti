/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.apis.impl;

import com.google.common.collect.Maps;
import com.simbest.activiti.apis.InstanceApi;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-17  22:50
 */
@Component
public class InstanceApiImpl implements InstanceApi {
    private final Map<String, ProcessDefinitionEntity> processDefinitions = Maps.newHashMap();

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskApiImpl taskApi;

    /**
     * 启动流程
     * @param processDefinitionKey
     * @param businessKey
     * @param variables
     */
    public ProcessInstance startProcessInstance(String processDefinitionKey, String businessKey, Map<String, Object> variables){
        return runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
    }

    /**
     * 根据实例Id，获取流程实例
     * @param instanceId
     * @return
     */
    public ProcessInstance getProcessInstance(String processInstanceId){
        return runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    }

    /**
     * 根据实例Id，获取评论意见
     * @param processInstanceId
     * @return
     */
    public List<Comment> getCommentByInstance(String processInstanceId) {
        return taskService.getProcessInstanceComments(processInstanceId);
    }
}
