/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.query.service;

import java.util.List;

import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.cores.service.IGenericService;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Task;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-08-12  11:28 
 */
public interface IActBusinessStatusService extends IGenericService<ActBusinessStatus,Long> {

    ActBusinessStatus getByBusiness(String processDefinitionKey, Long businessKey, String code, Boolean iscg);

    ActBusinessStatus getByInstance(String processInstanceId);

    ActBusinessStatus getByTask(String taskId);
    
    /**
     * 根据executionId 更新ActBusinessStatus
     * @param o
     * @return
     */
    int updateByExecutionId(ActBusinessStatus o);

    ActBusinessStatus updateBusinessTaskInfo(Task task);

	ActBusinessStatus getBySuperInstance(String superProcessInstanceId);
	
	List<ActBusinessStatus> getChildByParentId(Long act_parentId);
}
