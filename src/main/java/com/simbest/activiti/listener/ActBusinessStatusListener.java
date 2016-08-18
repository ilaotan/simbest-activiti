/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.listener;

import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.activiti.query.model.BusinessModel;
import com.simbest.activiti.query.service.IActBusinessStatusService;
import com.simbest.activiti.business.IBusinessService;
import com.simbest.activiti.support.BusinessServiceDynaEnum;
import com.simbest.cores.admin.authority.model.SysUser;
import com.simbest.cores.admin.authority.service.ISysUserAdvanceService;
import com.simbest.cores.exceptions.Exceptions;
import com.simbest.cores.utils.SpringContextUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * 用途：监听记录实例开始、实例结束、任务创建
 * 作者: lishuyi
 * 时间: 2016-08-10  15:36
 */
@Component
public class ActBusinessStatusListener implements ActivitiEventListener {
    public transient final Log log = LogFactory.getLog(getClass());

    @Autowired
    private IActBusinessStatusService statusService;

    @Autowired
    private BusinessServiceDynaEnum businessServiceDynaEnum;

    @Autowired
    private ISysUserAdvanceService sysUserAdvanceService;

    @Autowired
    private SpringContextUtil context;

    HistoricProcessInstanceEntity historyInstance;
    ActBusinessStatus businessStatus;
    ActivitiEntityEvent entityEvent;
    Map<String, Object> params;
    Collection<ActBusinessStatus> businessStatusList;
    int ret = 0;

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEventType eventType = event.getType();
        switch (eventType) {
            case HISTORIC_PROCESS_INSTANCE_CREATED:
                entityEvent = (ActivitiEntityEvent) event;
                historyInstance = (HistoricProcessInstanceEntity) entityEvent.getEntity();
                businessStatus = new ActBusinessStatus();
                if (StringUtils.isNotEmpty(historyInstance.getBusinessKey())) {
                    try {
                        Class clazz = Class.forName(businessServiceDynaEnum.value(historyInstance.getProcessDefinitionKey()).meaning());
                        IBusinessService businessService = (IBusinessService) context.getBeanByClass(clazz);
                        businessStatus.setBusinessKey(Long.parseLong(historyInstance.getBusinessKey()));
                        BusinessModel business = (BusinessModel) businessService.getById(businessStatus.getBusinessKey());
                        businessStatus.setIscg(business.getIscg());
                        businessStatus.setCode(business.getCode());
                        businessStatus.setTitle(business.getTitle());
                        businessStatus.setCreateUserId(business.getCreateUserId());
                        businessStatus.setCreateUserCode(business.getCreateUserCode());
                        businessStatus.setCreateUserName(business.getCreateUserName());
                        SysUser creator = sysUserAdvanceService.loadByKey(business.getCreateUserId());
                        businessStatus.setCreateOrgId(creator.getSysOrg().getId());
                        businessStatus.setCreateOrgName(creator.getSysOrg().getOrgName());
                    } catch (Exception e) {
                        Exceptions.printException(e);
                    }
                }
                businessStatus.setProcessDefinitionId(historyInstance.getProcessDefinitionId());
                businessStatus.setProcessDefinitionKey(historyInstance.getProcessDefinitionKey());
                ProcessDefinition processDefinition = event.getEngineServices().getRepositoryService().createProcessDefinitionQuery().processDefinitionId(historyInstance.getProcessDefinitionId()).singleResult();
                businessStatus.setProcessDefinitionName(processDefinition.getName());
                businessStatus.setProcessInstanceId(historyInstance.getProcessInstanceId());
                businessStatus.setStartTime(historyInstance.getStartTime());
                ret = statusService.create(businessStatus);
                log.debug(ret);
                break;
            case HISTORIC_PROCESS_INSTANCE_ENDED:
                entityEvent = (ActivitiEntityEvent) event;
                historyInstance = (HistoricProcessInstanceEntity) entityEvent.getEntity();
                ActBusinessStatus businessStatus = statusService.getByInstance(historyInstance.getProcessDefinitionId(), historyInstance.getProcessInstanceId());
                if (businessStatus != null) {
                    HistoryService historyService = event.getEngineServices().getHistoryService();
                    //更新开始节点
                    HistoricActivityInstance startActivityInstance = historyService.createHistoricActivityInstanceQuery().processDefinitionId(historyInstance.getProcessDefinitionId()).processInstanceId(historyInstance.getProcessInstanceId()).activityId(historyInstance.getStartActivityId()).singleResult();
                    businessStatus.setStartActivityId(startActivityInstance.getActivityId());
                    businessStatus.setStartActivityName(startActivityInstance.getActivityName());
                    //更新结束节点
                    businessStatus.setEndActivityId(historyInstance.getEndActivityId());
                    businessStatus.setEndTime(historyInstance.getEndTime());
                    businessStatus.setDuration(historyInstance.getDurationInMillis());
                    HistoricActivityInstance endActivityInstance = historyService.createHistoricActivityInstanceQuery().processDefinitionId(historyInstance.getProcessDefinitionId()).processInstanceId(historyInstance.getProcessInstanceId()).activityId(historyInstance.getEndActivityId()).singleResult();
                    if (endActivityInstance != null) //事务问题，导致结束节点名称无法获取
                        businessStatus.setEndActivityName(endActivityInstance.getActivityName());
                    //置空用户任务信息
                    businessStatus.setTaskId(null);
                    businessStatus.setTaskKey(null);
                    businessStatus.setTaskName(null);
                    businessStatus.setTaskOwner(null);
                    businessStatus.setTaskAssignee(null);
                    businessStatus.setDelegationState(null);
                    businessStatus.setTaskStartTime(null);
                    ret = statusService.update(businessStatus);
                    log.debug(ret);
                }
                break;
        }
    }

    @Override
    public boolean isFailOnException() {
        return true;
    }
}
