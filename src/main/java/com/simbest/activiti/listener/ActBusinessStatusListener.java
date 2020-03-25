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
import com.simbest.cores.utils.DateUtil;
import com.simbest.cores.utils.SpringContextUtil;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
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
                /*子流程在创建时businessKey为空，需要从参数中拿到子工单的id作为子流程实例的businessKey*/
                if(StringUtils.isEmpty(historyInstance.getBusinessKey()) && !StringUtils.isEmpty(historyInstance.getSuperProcessInstanceId())){
                	RuntimeService runtimeService = (RuntimeService) context.getBeanByName("runtimeService");//获取到runtimeService
                	String processDefinitionKey = historyInstance.getProcessDefinitionKey();
                	/*特殊处理，单独获取变量*/
                	if(processDefinitionKey!=null && ("YZNB_SJJL".equals(processDefinitionKey) || "JF_SJJL".equals(processDefinitionKey) || "GX_SJJL".equals(processDefinitionKey))){
                		Map special_map = (Map) runtimeService.getVariable(historyInstance.getSuperProcessInstanceId(), "businesskeySub_special");
                		List<String> inputUserIds_special = (List<String>) runtimeService.getVariable(historyInstance.getSuperProcessInstanceId(), "inputUserIds_special");
                		for(String userCode : inputUserIds_special){
                			String businessKey = (String) special_map.get(userCode);
                			ActBusinessStatus o = new ActBusinessStatus();//判断是不是草稿提交
                			o.setBusinessKey(Long.parseLong(businessKey));
                			o.setProcessDefinitionKey(historyInstance.getProcessDefinitionKey());
                			List<ActBusinessStatus> list = (List<ActBusinessStatus>) statusService.getAll(o);
                			if(list.size()==0){
                				historyInstance.setBusinessKey(businessKey);
                				break;
                			}
                		}
                	}else{
                		/*在审批时，变量的保存是以主工单的实例id保存，所以这里要根据子工单获取到父工单的id，再获取变量值*/
                		Map map = (Map) runtimeService.getVariable(historyInstance.getSuperProcessInstanceId(), "businesskeySub");
                		List<String> userCodes = (List<String>) runtimeService.getVariable(historyInstance.getSuperProcessInstanceId(), "inputUserIds");
                		String actBusinessId = (String) runtimeService.getVariable(historyInstance.getSuperProcessInstanceId(), "actBusinessId");
                		List<String> sameUserBusinesss = (List<String>) runtimeService.getVariable(historyInstance.getSuperProcessInstanceId(), "sameUserBusinesss");
                		/*true拆分给自己多次，false拆分多个人*/
                		Boolean divTag = (Boolean) runtimeService.getVariable(historyInstance.getSuperProcessInstanceId(), "divTag");
                		if(divTag!=null && divTag){/*拆分给自己多次用这个代码*/
                			List<ActBusinessStatus> actBusiness = statusService.getChildByParentId(Long.parseLong(actBusinessId));
                			String businessKey = sameUserBusinesss.get(actBusiness.size());
                			historyInstance.setBusinessKey(businessKey);
                		}else{/*拆分给多个人用下边的代码*/
                    		for(String userCode : userCodes){
                    			String businessKey = (String) map.get(userCode);
                    			ActBusinessStatus o = new ActBusinessStatus();//判断是不是草稿提交
                    			o.setBusinessKey(Long.parseLong(businessKey));
                    			o.setProcessDefinitionKey(historyInstance.getProcessDefinitionKey());
                    			List<ActBusinessStatus> list = (List<ActBusinessStatus>) statusService.getAll(o);
                    			if(list.size()==0){
                    				historyInstance.setBusinessKey(businessKey);
                    				break;
                    			}
                    		}
                		}

                	}
                	
                	ActBusinessStatus superActBusinessStatus = statusService.getBySuperInstance(historyInstance.getSuperProcessInstanceId());//获取父工单状态
                	businessStatus.setAct_parentId(superActBusinessStatus.getId());
//                	if(superActBusinessStatus.getIsparent()==null){
//                		/*字段标记主工单，标记值1*/
//                		superActBusinessStatus.setIsparent(ActBusinessStatusConstant.parent);
//                	}
//                	if(superActBusinessStatus.getAct_parentId()!=null){
//                		/*字段标记主工单，如果父工单已经拆分了一次，当前的的工单标记值为2
//                		 * 场景描述为张旭办理的工单标记是1，王守初办理的是2*/
//                		superActBusinessStatus.setIsparent(ActBusinessStatusConstant.centre);
//                	}
                	
                	if(superActBusinessStatus.getIsparent()==null){
                		 /*字段标记主工单，标记值1*/
                		 superActBusinessStatus.setIsparent(ActBusinessStatusConstant.parent);
                	}else if(superActBusinessStatus.getAct_parentId()!=null&&superActBusinessStatus.getIsparent()==null){
                		 /*字段标记主工单，如果父工单已经拆分了一次，当前的的工单标记值为2
                		  * 场景描述为张旭办理的工单标记是1，王守初办理的是2*/
                		 superActBusinessStatus.setIsparent(ActBusinessStatusConstant.centre);
                	}else if(superActBusinessStatus.getAct_parentId()!=null&&superActBusinessStatus.getIsparent()!=null){
                		 /*字段标记主工单，如果父工单已经拆分了2次，当前的的工单标记值为3
                		  * 场景描述为张旭办理的工单标记是1，王守初办理的是2,曹瑞波为3*/
                		 superActBusinessStatus.setIsparent(ActBusinessStatusConstant.SUBLEVEL);
                	}
                	
                	statusService.update(superActBusinessStatus);
                	
                }
                if (StringUtils.isNotEmpty(historyInstance.getBusinessKey())) {
                    try {
                        Class clazz = Class.forName(businessServiceDynaEnum.value(historyInstance.getProcessDefinitionKey()).meaning());
                        IBusinessService businessService = (IBusinessService) context.getBeanByClass(clazz);
                        businessStatus.setBusinessKey(Long.parseLong(historyInstance.getBusinessKey()));
                        BusinessModel business = (BusinessModel) businessService.getById(businessStatus.getBusinessKey());
                        businessStatus.setIscg(business.getIscg());
                        businessStatus.setCode(business.getCode());
                        businessStatus.setEnabled(true);
                        businessStatus.setRemoved(false);
                        businessStatus.setTitle(business.getTitle());
                        businessStatus.setDemandUserId(business.getDemandUserId());
                        if(business.getDemandUserId()!=null){
                        	SysUser demandUser = sysUserAdvanceService.loadByKey(business.getDemandUserId());
							if(demandUser!=null){
								businessStatus.setDemandUserName(demandUser.getUsername());
							}
                        }
                        businessStatus.setDemandOrgId(business.getDemandOrgId());
                        businessStatus.setCreateUserId(business.getCreateUserId());
                        businessStatus.setCreateUserCode(business.getCreateUserCode());
                        businessStatus.setCreateUserName(business.getCreateUserName());
                        SysUser creator = sysUserAdvanceService.loadByKey(business.getCreateUserId());
                        businessStatus.setCreateOrgId(creator.getSysOrg().getId());
                        businessStatus.setCreateOrgName(creator.getSysOrg().getOrgName());
                        businessStatus.setCreateTime(DateUtil.getCurrent());
                        businessStatus.setUpdateTime(DateUtil.getCurrent());
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
                if(StringUtils.isNotEmpty(historyInstance.getBusinessKey())){
                	ActBusinessStatus o = new ActBusinessStatus();//判断是不是草稿提交
                	o.setBusinessKey(Long.parseLong(historyInstance.getBusinessKey()));
//                	o.setCode(businessStatus.getCode());
                	/*要求所有的草稿必须自定义写ProcessDefinitionKey*/
                    o.setProcessDefinitionKey(historyInstance.getProcessDefinitionKey());
                	List<ActBusinessStatus> list = (List<ActBusinessStatus>) statusService.getAll(o);
                	if(list!=null && list.size()>0){
                		businessStatus.setId(list.get(0).getId());
                		ret = statusService.update(businessStatus);
                	}else{
                		ret = statusService.create(businessStatus);
                	}
                }else{
                	ret = statusService.create(businessStatus);
                }
                log.debug(ret);
                break;
            case HISTORIC_PROCESS_INSTANCE_ENDED:
                entityEvent = (ActivitiEntityEvent) event;
                historyInstance = (HistoricProcessInstanceEntity) entityEvent.getEntity();
                ActBusinessStatus businessStatus = statusService.getByInstance(historyInstance.getProcessInstanceId());
                if (businessStatus != null) {
                    HistoryService historyService = event.getEngineServices().getHistoryService();
                    //更新开始节点
                    HistoricActivityInstance startActivityInstance = historyService.createHistoricActivityInstanceQuery().processInstanceId(historyInstance.getProcessInstanceId()).activityId(historyInstance.getStartActivityId()).singleResult();
                    businessStatus.setStartActivityId(startActivityInstance.getActivityId());
                    businessStatus.setStartActivityName(startActivityInstance.getActivityName());
                    //更新结束节点
                    businessStatus.setEndActivityId(historyInstance.getEndActivityId());
                    businessStatus.setEndTime(historyInstance.getEndTime());
                    businessStatus.setDuration(historyInstance.getDurationInMillis());
                    if(historyInstance.getEndActivityId()!=null){//手动删除的时候为空
                    	HistoricActivityInstance endActivityInstance = historyService.createHistoricActivityInstanceQuery().processInstanceId(historyInstance.getProcessInstanceId()).activityId(historyInstance.getEndActivityId()).singleResult();
                    	if (endActivityInstance != null) //事务问题，导致结束节点名称无法获取
                            businessStatus.setEndActivityName(endActivityInstance.getActivityName());
                    }
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
