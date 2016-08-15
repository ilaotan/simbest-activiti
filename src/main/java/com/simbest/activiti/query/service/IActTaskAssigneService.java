package com.simbest.activiti.query.service;

import com.simbest.activiti.query.model.ActTaskAssigne;
import com.simbest.cores.service.IGenericService;

import java.util.List;

public interface IActTaskAssigneService extends IGenericService<ActTaskAssigne, Long> {
    List<String> queryToDoUser(String taskId);
}