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
    List<String> queryToDoUser(String taskId);
}