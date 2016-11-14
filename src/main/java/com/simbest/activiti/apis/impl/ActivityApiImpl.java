/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.apis.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.simbest.activiti.apis.ActivityApi;
import com.simbest.activiti.apis.DefinitionApi;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-17  22:50
 */
@Component
public class ActivityApiImpl implements ActivityApi {
    public transient final Log log = LogFactory.getLog(getClass());

    private final Map<String, ProcessDefinitionEntity> processDefinitions = Maps.newHashMap();

    @Autowired
    private DefinitionApi definitionApi;

    @Autowired
    private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
    @Override
    public ActivityImpl getStartActivity(String processDefinitionId) {
        ActivityImpl result = null;
        List<ActivityImpl> activitiList = getAllActivity(processDefinitionId);
        for (ActivityImpl act : activitiList) {
            Iterator<Map.Entry<String, Object>> properties = act.getProperties().entrySet().iterator();
            while (properties.hasNext()) {
                Map.Entry<String, Object> entry = properties.next();
                if ("type".equals(entry.getKey()) && "startEvent".equals(entry.getValue()))
                    result = act;
            }
        }
        return result;
    }

    @Override
    public ActivityImpl getEndActivity(String processDefinitionId) {
        ActivityImpl result = null;
        List<ActivityImpl> activitiList = getAllActivity(processDefinitionId);
        for (ActivityImpl act : activitiList) {
            Iterator<Map.Entry<String, Object>> properties = act.getProperties().entrySet().iterator();
            while (properties.hasNext()) {
                Map.Entry<String, Object> entry = properties.next();
                if ("type".equals(entry.getKey()) && "endEvent".equals(entry.getValue()))
                    result = act;
            }
        }
        return result;
    }

    @Override
    public List<ActivityImpl> getAllActivity(String processDefinitionId) {
        ProcessDefinitionEntity processDefinitionEntity = definitionApi.getDefinitionEntity(processDefinitionId);
        return processDefinitionEntity.getActivities();
    }

    /**
     * 获取当前运行节点
     *
     * @param processInstanceId
     * @return
     */
    public List<ActivityImpl> getCurrentActivity(String processInstanceId) {
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
    public List<Map<String, Object>> getCurrentActivityLocation(String processInstanceId) {
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

	@Override
	public ActivityImpl getCurrentActivityByTaskId(String taskId) {
		Task task = taskService.createTaskQuery()//
				.taskId(taskId)//使用任务ID查询
				.singleResult();
		//2：获取流程定义ID
		String processDefinitionId = task.getProcessDefinitionId();
		//3：查询ProcessDefinitionEntiy对象
		ProcessDefinitionEntity processDefinitionEntity = definitionApi.getDefinitionEntity(processDefinitionId);
		//获取当前活动的id
		String activityId = task.getTaskDefinitionKey();
		//4：获取当前的活动
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
		return activityImpl;
	}

	@Override
	public List<ActivityImpl> getNextActivitysByTaskId(String taskId) {
		List<ActivityImpl> list = new ArrayList();
		Task task = taskService.createTaskQuery()//
				.taskId(taskId)//使用任务ID查询
				.singleResult();
		//2：获取流程定义ID
		String processDefinitionId = task.getProcessDefinitionId();
		//3：查询ProcessDefinitionEntiy对象
		ProcessDefinitionEntity processDefinitionEntity = definitionApi.getDefinitionEntity(processDefinitionId);
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(task.getTaskDefinitionKey());
		//5：获取当前活动完成之后连线的名称
		List<PvmTransition> pvmList = activityImpl.getOutgoingTransitions();
		if(pvmList!=null && pvmList.size()>0){
			for(PvmTransition pvm:pvmList){
				PvmActivity pvmActivity  = pvm.getDestination();
				String activityId = pvmActivity.getId();
				ActivityImpl subactivityImpl = processDefinitionEntity.findActivity(activityId);
				list.add(subactivityImpl);
			}
		}
		return list;
	}

}
