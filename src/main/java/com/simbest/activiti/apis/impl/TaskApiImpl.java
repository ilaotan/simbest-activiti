/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.apis.impl;

import com.google.common.collect.Lists;
import com.simbest.activiti.apis.DefinitionApi;
import com.simbest.activiti.apis.TaskApi;
import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.activiti.query.service.IActTaskAssigneService;
import com.simbest.activiti.query.service.ICustomTaskService;
import com.simbest.cores.utils.pages.PageSupport;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-06  11:31
 */
@Component
public class TaskApiImpl implements TaskApi {
    public transient final Log log = LogFactory.getLog(getClass());

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ICustomTaskService customTaskService;

    @Autowired
    private IActTaskAssigneService assigneService;

    @Autowired
    private DefinitionApi definitionApi;



    /**
     * 根据任务Id，查询任务
     *
     * @param taskId
     * @return
     */
    public Task getTask(String taskId) {
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        return task;
    }

    /**
     * 查询人员待办
     *
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    public PageSupport<ActBusinessStatus> queryMyTask(final String uniqueCode, final int pageindex, final int pagesize) {
        return customTaskService.queryMyTask(uniqueCode, pageindex, pagesize);
    }
    
    /**
     * 查询人员待办数量
     * @param uniqueCode
     * @return
     */
	public Integer queryMyTaskCount(String uniqueCode) {
		 return customTaskService.queryMyTaskCount(uniqueCode);
	}

    /**
     * 查询人员申请
     *
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    public PageSupport<ActBusinessStatus> queryMyApply(final String uniqueCode, final int pageindex, final int pagesize) {
        return customTaskService.queryMyApply(uniqueCode, pageindex, pagesize);
    }

    /**
     * 查询人员草稿
     *
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    public PageSupport<ActBusinessStatus> queryMyDraft(final String uniqueCode, final int pageindex, final int pagesize) {
        return customTaskService.queryMyDraft(uniqueCode, pageindex, pagesize);
    }

    /**
     * 查询人员已办
     *
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    public PageSupport<ActBusinessStatus> queryMyJoin(final String uniqueCode, final int pageindex, final int pagesize) {
        return customTaskService.queryMyJoin(uniqueCode, pageindex, pagesize);
    }

    /**
     * 查询任务办理人和候选人
     *
     * @param taskId
     * @return
     */
    public List<String> queryToDoUser(final String taskId) {
        return assigneService.queryToDoUser(taskId);
    }

    /**
     * 查询任务办理人
     *
     * @param taskId
     * @return
     */
    public String queryAssignee(final String taskId) {
        return assigneService.queryAssignee(taskId);
    }

    /**
     * 查询任务候选人
     *
     * @param taskId
     * @return
     */
    public List<String> queryCandidate(final String taskId) {
        return assigneService.queryCandidate(taskId);
    }


    public List<String> getCandidateUsers(org.activiti.engine.task.Task task) {
        List<String> ret = Lists.newLinkedList();
        ProcessDefinitionEntity processDefinition = definitionApi.getDefinitionEntity(task.getProcessDefinitionId());
        Set<Expression> candidateUserIdExpressions = processDefinition.getTaskDefinitions().get(task.getTaskDefinitionKey()).getCandidateUserIdExpressions();
        for (Expression expression : candidateUserIdExpressions) {
            ret.add(expression.getExpressionText());
        }
        return ret;
    }

    public List<String> getCandidateGroups(org.activiti.engine.task.Task task) {
        List<String> ret = Lists.newLinkedList();
        ProcessDefinitionEntity processDefinition = definitionApi.getDefinitionEntity(task.getProcessDefinitionId());
        Set<Expression> candidateUserIdExpressions = processDefinition.getTaskDefinitions().get(task.getTaskDefinitionKey()).getCandidateGroupIdExpressions();
        for (Expression expression : candidateUserIdExpressions) {
            ret.add(expression.getExpressionText());
        }
        return ret;
    }

    /**
     * 根据流程实例，获取下一任务集合
     *
     * @param procInstId
     * @param elString
     * @return
     */
    public List<TaskDefinition> getTaskDefinitionList(String procInstId, String elString) {
        List<TaskDefinition> taskDefinitionList = new ArrayList<TaskDefinition>();
        //执行实例
        ExecutionEntity execution = (ExecutionEntity) runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();
        ProcessDefinitionEntity processDefinition = definitionApi.getDefinitionEntity(execution.getProcessDefinitionId());
        //当前实例的执行到哪个节点
        String activitiId = execution.getActivityId();
        //获得当前任务的所有节点
        List<ActivityImpl> activitiList = processDefinition.getActivities();
        String id = null;
        for (ActivityImpl activityImpl : activitiList) {
            id = activityImpl.getId();
            if (activitiId.equals(id)) {
                log.debug("当前任务：" + activityImpl.getProperty("name"));
                if (StringUtils.isEmpty(elString))
                    taskDefinitionList = nextTaskDefinition(activityImpl, activityImpl.getId());
                else
                    taskDefinitionList = nextTaskDefinition(activityImpl, activityImpl.getId(), elString);
            }
        }
        return taskDefinitionList;
    }

    /**
     * 获取所有节点
     *
     * @param activityImpl
     * @param activityId
     * @return
     */
    private List<TaskDefinition> nextTaskDefinition(ActivityImpl activityImpl, String activityId) {
        List<TaskDefinition> taskDefinitionList = new ArrayList<TaskDefinition>();//所有的任务实例
        List<TaskDefinition> nextTaskDefinition = new ArrayList<TaskDefinition>();//逐个获取的任务实例
        TaskDefinition taskDefinition = null;
        if ("userTask".equals(activityImpl.getProperty("type")) && !activityId.equals(activityImpl.getId())) {
            taskDefinition = ((UserTaskActivityBehavior) activityImpl.getActivityBehavior()).getTaskDefinition();
            taskDefinitionList.add(taskDefinition);
        } else {
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
            List<PvmTransition> outTransitionsTemp = null;
            for (PvmTransition tr : outTransitions) {
                PvmActivity ac = tr.getDestination(); //获取线路的终点节点
                //如果是互斥网关或者是并行网关
                if ("exclusiveGateway".equals(ac.getProperty("type")) || "parallelGateway".equals(ac.getProperty("type"))) {
                    outTransitionsTemp = ac.getOutgoingTransitions();
                    if (outTransitionsTemp.size() == 1) {
                        nextTaskDefinition = nextTaskDefinition((ActivityImpl) outTransitionsTemp.get(0).getDestination(), activityId);
                        taskDefinitionList.addAll(nextTaskDefinition);
                    } else if (outTransitionsTemp.size() > 1) {
                        for (PvmTransition tr1 : outTransitionsTemp) {
                            nextTaskDefinition = nextTaskDefinition((ActivityImpl) tr1.getDestination(), activityId);
                            taskDefinitionList.addAll(nextTaskDefinition);
                        }
                    }
                } else if ("userTask".equals(ac.getProperty("type"))) {
                    taskDefinition = ((UserTaskActivityBehavior) ((ActivityImpl) ac).getActivityBehavior()).getTaskDefinition();
                    taskDefinitionList.add(taskDefinition);
                } else {
                    log.debug((String) ac.getProperty("type"));
                }
            }
        }
        return taskDefinitionList;
    }

    /**
     * 根据网关表达式变量值获取特定下一节点
     *
     * @param activityImpl
     * @param activityId
     * @param elString
     * @return
     */
    private List<TaskDefinition> nextTaskDefinition(ActivityImpl activityImpl, String activityId, String elString) {
        List<TaskDefinition> taskDefinitionList = new ArrayList<TaskDefinition>();//所有的任务实例
        List<TaskDefinition> nextTaskDefinition = new ArrayList<TaskDefinition>();//逐个获取的任务实例
        TaskDefinition taskDefinition = null;
        if ("userTask".equals(activityImpl.getProperty("type")) && !activityId.equals(activityImpl.getId())) {
            taskDefinition = ((UserTaskActivityBehavior) activityImpl.getActivityBehavior()).getTaskDefinition();
            taskDefinitionList.add(taskDefinition);
        } else {
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
            List<PvmTransition> outTransitionsTemp = null;
            for (PvmTransition tr : outTransitions) {
                PvmActivity ac = tr.getDestination(); //获取线路的终点节点
                //如果是互斥网关或者是并行网关
                if ("exclusiveGateway".equals(ac.getProperty("type")) || "parallelGateway".equals(ac.getProperty("type"))) {
                    outTransitionsTemp = ac.getOutgoingTransitions();
                    if (outTransitionsTemp.size() == 1) {
                        nextTaskDefinition = nextTaskDefinition((ActivityImpl) outTransitionsTemp.get(0).getDestination(), activityId, elString);
                        taskDefinitionList.addAll(nextTaskDefinition);
                    } else if (outTransitionsTemp.size() > 1) {
                        for (PvmTransition tr1 : outTransitionsTemp) {
                            Object s = tr1.getProperty("conditionText");
                            if (elString.equals(StringUtils.trim(s.toString()))) {
                                nextTaskDefinition = nextTaskDefinition((ActivityImpl) tr1.getDestination(), activityId, elString);
                                taskDefinitionList.addAll(nextTaskDefinition);
                            }
                        }
                    }
                } else if ("userTask".equals(ac.getProperty("type"))) {
                    taskDefinition = ((UserTaskActivityBehavior) ((ActivityImpl) ac).getActivityBehavior()).getTaskDefinition();
                    taskDefinitionList.add(taskDefinition);
                } else {
                    log.debug((String) ac.getProperty("type"));
                }
            }
        }
        return taskDefinitionList;
    }


    public HistoricTaskInstance historicTaskInstanceByTaskId(String taskId) {
        return historyService.createHistoricTaskInstanceQuery().taskId(taskId).includeProcessVariables()
                .includeTaskLocalVariables().singleResult();
    }

    public HistoricTaskInstance lastestTaskByTaskDefKeyAndTaskId(String taskId , String taskDefKey) {
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(taskInstanceByTaskId(taskId).getProcessInstanceId())
                .taskDefinitionKey(taskDefKey).orderByHistoricTaskInstanceStartTime().desc().list();
        if (historicTaskInstanceList != null && historicTaskInstanceList.size() >0) {
            return historicTaskInstanceList.get(0);
        }
        throw new NullPointerException("节点未流转过，无法回退！");
    }

    public Task taskInstanceByTaskId(String taskId) {
        return taskService.createTaskQuery().taskId(taskId).singleResult();
    }

    /**
     * 任意流
     *
     * @param taskId
     *          当前环节实例ID
     * @param targetTaskDefKey
     *          目标环节定义的KEY
     */
    public void change2TargetTaskWithoutTransition(String taskId, String targetTaskDefKey) {
        Task currentTask = taskInstanceByTaskId(taskId);
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(currentTask.getProcessDefinitionId());
        ActivityImpl activity = processDefinition.findActivity(currentTask.getTaskDefinitionKey());
        activity.getOutgoingTransitions().clear();
        activity.createOutgoingTransition().setDestination(processDefinition.findActivity(targetTaskDefKey));
    }



	
}
