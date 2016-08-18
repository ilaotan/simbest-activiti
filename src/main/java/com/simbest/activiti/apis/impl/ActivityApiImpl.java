/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.apis.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.simbest.activiti.apis.ActivityApi;
import com.simbest.activiti.apis.DefinitionApi;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
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
public class ActivityApiImpl implements ActivityApi {
    private final Map<String, ProcessDefinitionEntity> processDefinitions = Maps.newHashMap();

    @Autowired
    private DefinitionApi definitionApi;

    @Autowired
    private RuntimeService runtimeService;

    /**
     * 获取当前运行节点
     *
     * @param processInstanceId
     * @return
     */
    public List<ActivityImpl> getRunningActivity(String processInstanceId) {
        List<ActivityImpl> result = Lists.newArrayList();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        ProcessDefinitionEntity processDefinitionEntity = definitionApi.getDefinitionEntity(processInstance.getProcessDefinitionId());
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).list();
        for (Execution execution : executions) {
            String activityId = execution.getActivityId();
            ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
            result.add(activityImpl);
        }
        return result;
    }

    /**
     * 获取当前实例活动坐标信息
     *
     * @param processInstanceId
     * @return
     */
    public List<Map<String, Object>> getRunningActivityLocation(String processInstanceId) {
        List<Map<String, Object>> result = Lists.newArrayList();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        ProcessDefinitionEntity processDefinitionEntity = definitionApi.getDefinitionEntity(processInstance.getProcessDefinitionId());
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).list();
        for (Execution execution : executions) {
            String activityId = execution.getActivityId();
            ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
            Map<String, Object> map = Maps.newHashMap();
            map.put("x", activityImpl.getX());
            map.put("y", activityImpl.getY());
            map.put("width", activityImpl.getWidth());
            map.put("height", activityImpl.getHeight());
            result.add(map);
        }
        return result;
    }

}
