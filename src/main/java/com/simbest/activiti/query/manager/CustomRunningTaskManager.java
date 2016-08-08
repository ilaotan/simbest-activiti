/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.query.manager;

import com.simbest.activiti.query.mapper.CustomRunningTaskMapper;
import com.simbest.activiti.query.model.CustomRunningTask;
import com.simbest.cores.utils.pages.PageSupport;
import org.activiti.engine.ManagementService;
import org.activiti.engine.impl.cmd.AbstractCustomSqlExecution;
import org.activiti.engine.impl.cmd.CustomSqlExecution;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-06  11:31
 */
@Service
public class CustomRunningTaskManager {

    @Autowired
    protected ManagementService managementService;

    public PageSupport<CustomRunningTask> getRunningTasks(final String loginName, final int pageindex, final int pagesize) {
        CustomSqlExecution<CustomRunningTaskMapper, PageSupport<CustomRunningTask>> customSqlExecution =
                new AbstractCustomSqlExecution<CustomRunningTaskMapper, PageSupport<CustomRunningTask>>(CustomRunningTaskMapper.class) {
                    @Override
                    public PageSupport<CustomRunningTask> execute(CustomRunningTaskMapper mapper) {
                        List<CustomRunningTask> list = mapper.getRunningTasks(loginName, pageindex, pagesize);
                        Integer count = mapper.countRunningTasks(loginName);
                        PageSupport ps = new PageSupport(list, count, pageindex, pagesize);
                        return ps;
                    }
                };
        return managementService.executeCustomSql(customSqlExecution);
    }
}
