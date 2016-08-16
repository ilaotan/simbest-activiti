/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.apis;

import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.activiti.query.service.IActTaskAssigneService;
import com.simbest.activiti.query.service.ICustomTaskService;
import com.simbest.cores.utils.pages.PageSupport;
import org.activiti.engine.impl.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-06  11:31
 */
@Component
public class TaskApi extends TaskServiceImpl {

    @Autowired
    private ICustomTaskService taskService;

    @Autowired
    private IActTaskAssigneService assigneService;

    /**
     * 查询我的待办
     *
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    public PageSupport<ActBusinessStatus> queryMyTask(final String uniqueCode, final int pageindex, final int pagesize) {
        return taskService.queryMyTask(uniqueCode, pageindex, pagesize);
    }

    /**
     * 查询我的申请
     *
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    public PageSupport<ActBusinessStatus> queryMyApply(final String uniqueCode, final int pageindex, final int pagesize) {
        return taskService.queryMyApply(uniqueCode, pageindex, pagesize);
    }

    /**
     * 查询我的已办
     *
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    public PageSupport<ActBusinessStatus> queryMyJoin(final String uniqueCode, final int pageindex, final int pagesize) {
        return taskService.queryMyJoin(uniqueCode, pageindex, pagesize);
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
}
