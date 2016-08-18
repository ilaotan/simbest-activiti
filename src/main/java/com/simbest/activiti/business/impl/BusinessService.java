/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.business.impl;

import com.simbest.activiti.apis.InstanceApi;
import com.simbest.activiti.apis.TaskApi;
import com.simbest.activiti.business.IBusinessService;
import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.activiti.query.model.BusinessModel;
import com.simbest.activiti.query.service.IActBusinessStatusService;
import com.simbest.cores.exceptions.TransactionRollbackException;
import com.simbest.cores.service.impl.LogicService;
import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;

import java.io.Serializable;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-18  11:46
 */
@CacheConfig(cacheNames = {"runtime:"}, cacheResolver = "genericCacheResolver", keyGenerator = "genericKeyGenerator")
public abstract class BusinessService<T extends BusinessModel<T>, PK extends Serializable>
        extends LogicService<T, PK> implements IBusinessService<T, PK> {

    @Autowired
    protected IActBusinessStatusService statusService;

    @Autowired
    protected ProcessEngine processEngine;

    @Autowired
    protected RepositoryService repositoryService;

    @Autowired
    protected RuntimeService runtimeService;

    @Autowired
    protected TaskService taskService;

    @Autowired
    protected HistoryService historyService;

    @Autowired
    protected IdentityService identityService;

    @Autowired
    protected ManagementService managementService;

    @Autowired
    protected FormService formService;


    @Autowired
    protected TaskApi taskApi;

    @Autowired
    protected InstanceApi instanceApi;

    public BusinessService(SqlSession sqlSession) {
        super(sqlSession);
    }

    @Override
    public T createDraft(T o) {
        o.setIscg(true);
        int ret = super.create(o);
        if (ret > 0) {
            ActBusinessStatus status = new ActBusinessStatus();
            BeanUtils.copyProperties(o, status);
            status.setBusinessKey(o.getId());
            ret = statusService.create(status);
            if (ret > 0)
                return o;
            else
                throw new TransactionRollbackException();
        }
        return null;
    }

    @Override
    public T getBusinessByTask(String taskId) {
        Task task = taskApi.getTask(taskId);
        ProcessInstance pi = instanceApi.getProcessInstance(task.getProcessInstanceId());
        String businessKey = pi.getBusinessKey();
        if (StringUtils.isNotEmpty(businessKey)) {
            return super.getById((PK) Long.valueOf(businessKey));
        }
        return null;
    }

    @Override
    public T getBusinessByBusinessKey(String businessKey) {
        if (StringUtils.isNotEmpty(businessKey)) {
            return super.getById((PK) Long.valueOf(businessKey));
        }
        return null;
    }
}
