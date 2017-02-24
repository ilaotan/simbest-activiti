/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.query.service.impl;

import com.google.common.collect.Maps;
import com.simbest.activiti.business.IBusinessService;
import com.simbest.activiti.query.mapper.ActBusinessStatusMapper;
import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.activiti.query.model.BusinessModel;
import com.simbest.activiti.query.service.IActBusinessStatusService;
import com.simbest.activiti.support.BusinessServiceDynaEnum;
import com.simbest.cores.admin.authority.model.ShiroUser;
import com.simbest.cores.exceptions.Exceptions;
import com.simbest.cores.exceptions.TransactionRollbackException;
import com.simbest.cores.service.impl.GenericMapperService;
import com.simbest.cores.utils.DateUtil;
import com.simbest.cores.utils.SpringContextUtil;

import org.activiti.engine.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-08-12  11:28 
 */
@Service
public class ActBusinessStatusService extends GenericMapperService<ActBusinessStatus,Long> implements IActBusinessStatusService {
    public final static Log log = LogFactory.getLog(ActBusinessStatusService.class);

    private ActBusinessStatusMapper mapper;
    
    @Autowired
    private BusinessServiceDynaEnum businessServiceDynaEnum;
    
    
    @Autowired
    private SpringContextUtil context;

    public ActBusinessStatusService(SqlSession sqlSession, Class<ActBusinessStatus> persistentMapper) {
        super(sqlSession, persistentMapper);
    }

    @Autowired
    public ActBusinessStatusService(@Qualifier(value="sqlSessionTemplateSimple") SqlSession sqlSession) {
        super(sqlSession);
        this.mapper = sqlSession.getMapper(ActBusinessStatusMapper.class);
        super.setMapper(mapper);
    }

    @Override
    public ActBusinessStatus getByBusiness(String processDefinitionKey, Long businessKey, String code, Boolean iscg){
        ActBusinessStatus status = new ActBusinessStatus();
        status.setProcessDefinitionKey(processDefinitionKey);
        status.setBusinessKey(businessKey);
        status.setCode(code);
        status.setIscg(iscg);
        Collection<ActBusinessStatus> list = mapper.getAll(status);
        return list.size()>0 ? list.iterator().next():null;
    }

    @Override
    public ActBusinessStatus getByInstance(String processInstanceId) {
        Map<String,Object> params = Maps.newHashMap();
        params.put("processInstanceId",processInstanceId);
        Collection<ActBusinessStatus> list = getAll(params);
        if(list != null && list.size()>0)
            return list.iterator().next();
        else
            return null;
    }

    @Override
    public ActBusinessStatus getByTask(String taskId) {
        Map<String,Object> params = Maps.newHashMap();
        params.put("taskId",taskId);
        Collection<ActBusinessStatus> list = getAll(params);
        if(list != null && list.size()>0)
            return list.iterator().next();
        else
            return null;
    }

	@Override
	public int updateByExecutionId(ActBusinessStatus o) {
		// TODO Auto-generated method stub
		return mapper.updateByExecutionId(o);
	}
	
    @Override
    public ActBusinessStatus updateBusinessTaskInfo(Task task) {
        int ret = 0;
        ActBusinessStatus businessStatus = getByInstance(task.getProcessInstanceId());
        if (businessStatus != null) {
            businessStatus.setExecutionId(task.getExecutionId());
            businessStatus.setTaskId(task.getId());
            businessStatus.setTaskKey(task.getTaskDefinitionKey());
            businessStatus.setTaskName(task.getName());
            businessStatus.setTaskOwner(task.getOwner());
            businessStatus.setTaskAssignee(task.getAssignee());
            businessStatus.setDelegationState(task.getDelegationState());
            businessStatus.setTaskStartTime(task.getCreateTime());
            businessStatus.setUpdateTime(DateUtil.getCurrent());
            Object currentSubject = SecurityUtils.getSubject().getPrincipal();
            if (currentSubject != null) {
                ShiroUser currentUser = (ShiroUser) currentSubject;
                businessStatus.setPreviousAssignee(currentUser.getUserId());
                businessStatus.setPreviousAssigneeUniqueCode(currentUser.getUniqueCode());
                businessStatus.setPreviousAssigneeName(currentUser.getUserName());
                businessStatus.setPreviousAssigneeDate(DateUtil.getCurrent());
            }
            
            if (businessStatus.getBusinessKey()!=null) {
                try {
                    Class clazz = Class.forName(businessServiceDynaEnum.value(businessStatus.getProcessDefinitionKey()).meaning());
                    IBusinessService businessService = (IBusinessService) context.getBeanByClass(clazz);
                    BusinessModel business = (BusinessModel) businessService.getById(businessStatus.getBusinessKey());
                    businessStatus.setTitle(business.getTitle());
                    businessStatus.setDemandUserId(business.getDemandUserId());
                    businessStatus.setDemandOrgId(business.getDemandOrgId());
                } catch (Exception e) {
                    Exceptions.printException(e);
                }
            }
            try{
            	ret = mapper.update(businessStatus);
            }catch(Exception e){
            	e.printStackTrace();
            }
            log.debug(ret);
        }

        if (ret > 0)
            return businessStatus;
        else
            throw new TransactionRollbackException();
    }

	@Override
	public ActBusinessStatus getBySuperInstance(String superProcessInstanceId) {
		// TODO Auto-generated method stub
		return mapper.getBySuperInstance(superProcessInstanceId);
	}

	@Override
	public List<ActBusinessStatus> getChildByParentId(Long act_parentId) {
		// TODO Auto-generated method stub
		return mapper.getChildByParentId(act_parentId);
	}
}
