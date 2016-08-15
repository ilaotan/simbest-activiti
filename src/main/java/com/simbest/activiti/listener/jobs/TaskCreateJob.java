/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.listener.jobs;

import com.simbest.cores.admin.authority.model.SysUser;
import org.activiti.engine.EngineServices;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

import java.util.List;

/**
 * 用途：用户创建时执行的任务
 * 作者: lishuyi 
 * 时间: 2016-08-11  10:30 
 */
public interface TaskCreateJob {

    void execution(TaskEntity task, List<String> toDoUsers);
}
