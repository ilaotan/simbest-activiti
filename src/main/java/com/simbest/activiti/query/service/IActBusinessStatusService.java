/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.query.service;

import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.cores.service.IGenericService;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-08-12  11:28 
 */
public interface IActBusinessStatusService extends IGenericService<ActBusinessStatus,Long> {

    ActBusinessStatus getByInstance(String processDefinitionId, String processInstanceId);

    ActBusinessStatus getByTask(String taskId);
}
