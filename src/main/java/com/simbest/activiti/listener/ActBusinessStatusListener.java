/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.listener;

import com.google.common.collect.Maps;
import com.simbest.activiti.apis.EngineServiceApi;
import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.activiti.query.model.BusinessModel;
import com.simbest.activiti.query.service.IActBusinessStatusService;
import com.simbest.activiti.query.service.IBusinessService;
import com.simbest.activiti.support.BusinessServiceDynaEnum;
import com.simbest.cores.admin.authority.model.SysUser;
import com.simbest.cores.admin.authority.service.ISysUserAdvanceService;
import com.simbest.cores.utils.SpringContextUtil;
import com.simbest.cores.utils.configs.CoreConfig;
import com.simbest.cores.utils.enums.FileClassDynaEnum;
import org.activiti.engine.HistoryService;
import org.activiti.engine.delegate.event.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
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
    private ISysUserAdvanceService sysUserAdvanceService;

    @Autowired
    private BusinessServiceDynaEnum businessServiceDynaEnum;

    @Autowired
    private SpringContextUtil context;

    HistoricProcessInstanceEntity historyInstance;
    ActBusinessStatus businessStatus;
    ActivitiEntityEvent entityEvent;
    TaskEntity task;
    Map<String,Object> params;
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
                businessStatus.setBusinessKey(historyInstance.getBusinessKey());
                if(StringUtils.isNotEmpty(historyInstance.getBusinessKey())){
                    try {
                        Class clazz = Class.forName(businessServiceDynaEnum.value(historyInstance.getProcessDefinitionKey()).meaning());
                        IBusinessService businessService = (IBusinessService)context.getBeanByClass(clazz);
                        BusinessModel business = (BusinessModel) businessService.getById(historyInstance.getBusinessKey());
                        businessStatus.setIscg(business.getIscg());
                        businessStatus.setCode(business.getCode());
                        businessStatus.setTitle(business.getTitle());
                    }catch(Exception e){

                    }
                }
                businessStatus.setProcessDefinitionId(historyInstance.getProcessDefinitionId());
                businessStatus.setProcessDefinitionKey(historyInstance.getProcessDefinitionKey());
                ProcessDefinition processDefinition = event.getEngineServices().getRepositoryService().createProcessDefinitionQuery().processDefinitionId(historyInstance.getProcessDefinitionId()).singleResult();
                businessStatus.setProcessDefinitionName(processDefinition.getName());
                businessStatus.setProcessInstanceId(historyInstance.getProcessInstanceId());
                businessStatus.setStartTime(historyInstance.getStartTime());
                SysUser startUser = sysUserAdvanceService.loadByCustom("uniqueCode", historyInstance.getStartUserId());
                if(null != startUser){
                    businessStatus.setCreateOrgId(startUser.getSysOrg().getId());
                    businessStatus.setCreateOrgName(startUser.getSysOrg().getOrgName());
                    businessStatus.setCreateUserId(startUser.getId());
                    businessStatus.setCreateUserCode(startUser.getUniqueCode());
                    businessStatus.setCreateUserName(startUser.getUsername());
                }
                ret = statusService.create(businessStatus);
                log.debug(ret);
                break;
            case HISTORIC_PROCESS_INSTANCE_ENDED:
                entityEvent = (ActivitiEntityEvent) event;
                historyInstance = (HistoricProcessInstanceEntity) entityEvent.getEntity();
                params = Maps.newHashMap();
                params.put("processDefinitionId", historyInstance.getProcessDefinitionId());
                params.put("processInstanceId", historyInstance.getProcessInstanceId());
                businessStatusList = statusService.getAll(params);
                if(businessStatusList.size()>0){
                    businessStatus = businessStatusList.iterator().next();
                    HistoryService historyService = event.getEngineServices().getHistoryService();
                    HistoricActivityInstance startActivityInstance = historyService.createHistoricActivityInstanceQuery().processDefinitionId(historyInstance.getProcessDefinitionId()).processInstanceId(historyInstance.getProcessInstanceId()).activityId(historyInstance.getStartActivityId()).singleResult();
                    businessStatus.setStartActivityId(startActivityInstance.getActivityId());
                    businessStatus.setStartActivityName(startActivityInstance.getActivityName());

                    businessStatus.setEndActivityId(historyInstance.getEndActivityId());
                    businessStatus.setEndTime(historyInstance.getEndTime());
                    businessStatus.setDuration(historyInstance.getDurationInMillis());
                    HistoricActivityInstance endActivityInstance = historyService.createHistoricActivityInstanceQuery().processDefinitionId(historyInstance.getProcessDefinitionId()).processInstanceId(historyInstance.getProcessInstanceId()).activityId(historyInstance.getEndActivityId()).singleResult();
                    if(endActivityInstance != null)
                        businessStatus.setEndActivityName(endActivityInstance.getActivityName());
                    ret = statusService.update(businessStatus);
                    log.debug(ret);
                }
                break;
            default:
                log.debug("------------------------------------------------");
                log.debug("捕获到事件[Do Nothing]：" + eventType.name() + ", type=" + event.getType()+" ToString is :"+ToStringBuilder.reflectionToString(event));
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
