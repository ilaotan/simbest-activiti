/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.query.service;

import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.cores.utils.pages.PageSupport;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-06  11:31
 */
public interface IMyTaskService {

    /**
     * 查询我的待办
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    PageSupport<ActBusinessStatus> queryMyTask(final String uniqueCode, final int pageindex, final int pagesize);

    /**
     * 查询我的申请
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    PageSupport<ActBusinessStatus> queryMyApply(final String uniqueCode, final int pageindex, final int pagesize);
}
