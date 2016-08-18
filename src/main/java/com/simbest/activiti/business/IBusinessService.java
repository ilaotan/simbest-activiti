/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.business;

import com.simbest.activiti.query.model.BusinessModel;
import com.simbest.cores.service.ILogicService;

import java.io.Serializable;


/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-12  11:28
 */
public interface IBusinessService<T extends BusinessModel<T>, PK extends Serializable> extends ILogicService<T, PK> {
    T createDraft(T o);

    T getBusinessByTask(String taskId);

    T getBusinessByBusinessKey(String businessKey);
}
