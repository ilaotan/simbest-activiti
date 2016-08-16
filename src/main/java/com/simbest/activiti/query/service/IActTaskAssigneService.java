package com.simbest.activiti.query.service;

import com.simbest.activiti.query.model.ActTaskAssigne;
import com.simbest.cores.service.IGenericService;

import java.util.List;

public interface IActTaskAssigneService extends IGenericService<ActTaskAssigne, Long> {
    /**
     * 查询任务办理人和候选人
     * @param taskId
     * @return
     */
    List<String> queryToDoUser(final String taskId);

    /**
     * 查询任务办理人
     * @param taskId
     * @return
     */
    String queryAssignee(String taskId);

    /**
     * 查询任务候选人
     * @param taskId
     * @return
     */
    List<String> queryCandidate(String taskId);
}