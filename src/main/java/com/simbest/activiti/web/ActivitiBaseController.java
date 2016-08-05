/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.web;

import org.activiti.engine.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-08-05  9:07 
 */
public class ActivitiBaseController {

    public transient final Log log = LogFactory.getLog(getClass());

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
}
