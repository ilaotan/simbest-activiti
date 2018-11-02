/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.listener.jobs;

import com.simbest.activiti.exceptions.NotFoundBusinessException;
import com.simbest.activiti.listener.schedule.model.TaskCallbackLog;
import com.simbest.activiti.listener.schedule.model.TaskCallbackRetry;
import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.cores.admin.authority.service.ISysGroupAdvanceService;
import com.simbest.cores.exceptions.Exceptions;
import com.simbest.cores.service.IGenericService;
import com.simbest.cores.utils.DateUtil;
import com.simbest.cores.utils.SpringContextUtil;

import org.activiti.engine.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 用途：提交用户待办TaskCreateJob和撤销用户待办TaskCompletedJob
 * 作者: lishuyi 
 * 时间: 2016-12-13  17:33 
 */
@Component
public class UserTaskSubmitor {
    public transient final Log log = LogFactory.getLog(getClass());

    @Autowired
    private SpringContextUtil context;

    @Autowired
    private ISysGroupAdvanceService groupAdvanceService;

    @Autowired
    @Qualifier("taskCallbackRetryService")
    private IGenericService<TaskCallbackRetry, Long> taskCallbackRetryService;

    @Autowired
    @Qualifier("taskCallbackLogService")
    private IGenericService<TaskCallbackLog, Long> taskCallbackLogService;

    public void createUserTaskCallback(ActBusinessStatus businessStatus, Task task ,String uniqueCode) {
        Date callbackStartDate = DateUtil.getCurrent();
        Boolean callbackResult = true;
        TaskCreateJob job = null;
        String callbackError = null;
        if (businessStatus == null)
            throw new NotFoundBusinessException();
        try {
            job = (TaskCreateJob) context.getBeanByClass(TaskCreateJob.class);
            job.execution(businessStatus, task,uniqueCode);
        } catch (Exception e) {
            log.error("@@@@Error:" + Exceptions.getStackTraceAsString(e));
            TaskCallbackRetry taskCallbackRetry = new TaskCallbackRetry();
            taskCallbackRetry.setTaskJobClass(job.getClass().getName());
            taskCallbackRetry.setExecuteTimes(1);
            taskCallbackRetry.setLastExecuteDate(DateUtil.getCurrent());
            taskCallbackRetry.setCallbackType("CreateCallback");
            taskCallbackRetry.setActBusinessStatusId(businessStatus.getId());
            taskCallbackRetry.setUniqueCode(uniqueCode);
            int result1 = taskCallbackRetryService.create(taskCallbackRetry);
            log.debug(result1);
            callbackResult = false;
            callbackError = org.apache.commons.lang.StringUtils.substring(Exceptions.getStackTraceAsString(e), 0, 1999);
        } finally {
            TaskCallbackLog taskCallbackLog = new TaskCallbackLog();
            taskCallbackLog.setActBusinessStatusId(businessStatus.getId());
            taskCallbackLog.setCallbackType("CreateCallback");
            taskCallbackLog.setCallbackStartDate(callbackStartDate);
            taskCallbackLog.setCallbackEndDate(DateUtil.getCurrent());
            taskCallbackLog.setCallbackDuration(taskCallbackLog.getCallbackEndDate().getTime() - callbackStartDate.getTime());
            taskCallbackLog.setCallbackResult(callbackResult);
            taskCallbackLog.setCallbackError(callbackError);
            int result2 = taskCallbackLogService.create(taskCallbackLog);
            log.debug(result2);
        }
    }

    public void createGroupTaskCallback(ActBusinessStatus businessStatus, Task task,Integer groupId) {
        if (businessStatus == null)
            throw new NotFoundBusinessException();
        List<String> uniqueCodes = groupAdvanceService.getGroupUser(groupId);
        for (String user : uniqueCodes) {
            createUserTaskCallback(businessStatus, task,user);
        }
    }

    public void removeUserTaskCallback(ActBusinessStatus businessStatus, Task task ,String uniqueCode) {
        Date callbackStartDate = DateUtil.getCurrent();
        Boolean callbackResult = true;
        TaskCompletedJob job = null;
        String callbackError = null;
        if (businessStatus == null)
            throw new NotFoundBusinessException();
        try {
            job = (TaskCompletedJob) context.getBeanByClass(TaskCompletedJob.class);
            job.execution(businessStatus, task,uniqueCode);
        } catch (Exception e) {
            log.error("@@@@Error:" + Exceptions.getStackTraceAsString(e));
            TaskCallbackRetry taskCallbackRetry = new TaskCallbackRetry();
            taskCallbackRetry.setTaskJobClass(job.getClass().getName());
            taskCallbackRetry.setExecuteTimes(1);
            taskCallbackRetry.setLastExecuteDate(DateUtil.getCurrent());
            taskCallbackRetry.setCallbackType("CompletedCallback");
            taskCallbackRetry.setActBusinessStatusId(businessStatus.getId());
            taskCallbackRetry.setUniqueCode(uniqueCode);
            int result1 = taskCallbackRetryService.create(taskCallbackRetry);
            log.debug(result1);
            callbackResult = false;
            callbackError = org.apache.commons.lang.StringUtils.substring(Exceptions.getStackTraceAsString(e), 0, 1999);
        } finally {
            TaskCallbackLog taskCallbackLog = new TaskCallbackLog();
            taskCallbackLog.setActBusinessStatusId(businessStatus.getId());
            taskCallbackLog.setCallbackType("CompletedCallback");
            taskCallbackLog.setCallbackStartDate(callbackStartDate);
            taskCallbackLog.setCallbackEndDate(DateUtil.getCurrent());
            taskCallbackLog.setCallbackDuration(taskCallbackLog.getCallbackEndDate().getTime() - callbackStartDate.getTime());
            taskCallbackLog.setCallbackResult(callbackResult);
            taskCallbackLog.setCallbackError(callbackError);
            int result2 = taskCallbackLogService.create(taskCallbackLog);
            log.debug(result2);
        }
    }

    public void removeGroupTaskCallback(ActBusinessStatus businessStatus, Task task,Integer groupId) {
        if (businessStatus == null)
            throw new NotFoundBusinessException();
        List<String> uniqueCodes = groupAdvanceService.getGroupUser(groupId);
        for (String user : uniqueCodes) {
            removeUserTaskCallback(businessStatus, task,user);
        }
    }
}
