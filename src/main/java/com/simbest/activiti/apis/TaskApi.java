/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.apis;

import com.google.common.collect.Lists;
import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.cores.utils.pages.PageSupport;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-08-18  15:38 
 */
public interface TaskApi {
    /**
     * 根据任务Id，查询任务
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
    PageSupport<ActBusinessStatus> queryMyTask(final String uniqueCode, final int pageindex, final int pagesize);

    /**
     * 查询人员申请
     *
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    PageSupport<ActBusinessStatus> queryMyApply(final String uniqueCode, final int pageindex, final int pagesize);

    /**
     * 查询人员草稿
     *
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    PageSupport<ActBusinessStatus> queryMyDraft(final String uniqueCode, final int pageindex, final int pagesize);

    /**
     * 查询人员已办
     *
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    PageSupport<ActBusinessStatus> queryMyJoin(final String uniqueCode, final int pageindex, final int pagesize);

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



}
