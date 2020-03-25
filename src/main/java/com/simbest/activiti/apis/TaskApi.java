/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.apis;

import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.cores.utils.pages.PageSupport;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.task.Task;

import java.util.Date;
import java.util.List;


public interface TaskApi {
    /**
     * 根据任务Id，查询任务
     *
     * @param taskId
     * @return
     */
    Task getTask(String taskId);

    /**
     * 查询人员待办
     *
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    PageSupport<ActBusinessStatus> queryMyTask(final String uniqueCode,String taskKeyCategory, final String code, final String title, final String processDefinitionKeys,final String demandUserName,final List<Integer> demandOrgIdss, final int pageindex, final int pagesize);

    /**
     * 查询人员待办MOA
     *
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    PageSupport<ActBusinessStatus> queryMyTaskMOA(final String uniqueCode, final String code, final String title, final String processDefinitionKeys, final int pageindex, final int pagesize);

    /**
     * 查询人员申请
     *
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    PageSupport<ActBusinessStatus> queryMyApply(final String uniqueCode, final String code, final String title, final String processDefinitionKeys,final Date startTime,final Date endTime,final String delegationState, final int pageindex, final int pagesize);

    /**
     * 查询人员草稿
     *
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    PageSupport<ActBusinessStatus> queryMyDraft(final String uniqueCode, final String code, final String title, final String processDefinitionKeys, final int pageindex, final int pagesize);

    /**
     * 查询人员已办
     *
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    PageSupport<ActBusinessStatus> queryMyJoin(final String uniqueCode, final String code, final String title, final String processDefinitionKeys,final Date startTime,final Date endTime,final String delegationState, final String demandUserName,final List<Integer> demandOrgIdss,final int pageindex, final int pagesize);

    /**
     * 查询任务办理人和候选人
     *
     * @param taskId
     * @return
     */
    List<String> queryToDoUser(final String taskId);

    /**
     * 查询任务办理人
     *
     * @param taskId
     * @return
     */
    String queryAssignee(final String taskId);

    /**
     * 查询任务候选人
     *
     * @param taskId
     * @return
     */
    List<String> queryCandidate(final String taskId);


    List<String> getCandidateUsers(org.activiti.engine.task.Task task);

    List<String> getCandidateGroups(org.activiti.engine.task.Task task);

    /**
     * 根据流程实例，获取下一任务集合
     *
     * @param procInstId
     * @param elString
     * @return
     */
    List<TaskDefinition> getTaskDefinitionList(String procInstId, String elString);


    HistoricTaskInstance historicTaskInstanceByTaskId(String taskId);

    HistoricTaskInstance lastestTaskByTaskDefKeyAndTaskId(String taskId, String taskDefKey);

    Task taskInstanceByTaskId(String taskId);

    /**
     * 任意流
     *
     * @param taskId           当前环节实例ID
     * @param targetTaskDefKey 目标环节定义的KEY
     */
    void change2TargetTaskWithoutTransition(String taskId, String targetTaskDefKey);
}
