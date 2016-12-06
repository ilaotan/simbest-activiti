/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.query.service;

import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.cores.utils.pages.PageSupport;

import java.util.List;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-08-16  15:55 
 */
public interface ICustomTaskService {
    /**
     * 查询我的待办
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    PageSupport<ActBusinessStatus> queryMyTask(String uniqueCode, int pageindex, int pagesize);

    /**
     * 查询我的申请
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    PageSupport<ActBusinessStatus> queryMyApply(String uniqueCode, int pageindex, int pagesize);

    /**
     * 查询我的草稿
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    PageSupport<ActBusinessStatus> queryMyDraft(String uniqueCode, int pageindex, int pagesize);

    /**
     * 查询我的已办
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    PageSupport<ActBusinessStatus> queryMyJoin(String uniqueCode, int pageindex, int pagesize);

    /**
     * 查询我的待办数量
     * @param uniqueCode
     * @return
     */
	Integer queryMyTaskCount(String uniqueCode);


}
