/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.apis;

import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.cores.utils.pages.PageSupport;
import org.activiti.engine.ManagementService;
import org.activiti.engine.impl.TaskServiceImpl;
import org.activiti.engine.impl.cmd.AbstractCustomSqlExecution;
import org.activiti.engine.impl.cmd.CustomSqlExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-06  11:31
 */
@Component
public class TaskApi extends TaskServiceImpl{

    @Autowired
    protected ManagementService managementService;

    /**
     * 查询我的待办
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    public PageSupport<ActBusinessStatus> queryMyTask(final String uniqueCode, final int pageindex, final int pagesize) {
        CustomSqlExecution<TaskMapper, PageSupport<ActBusinessStatus>> customSqlExecution =
                new AbstractCustomSqlExecution<TaskMapper, PageSupport<ActBusinessStatus>>(TaskMapper.class) {
                    @Override
                    public PageSupport<ActBusinessStatus> execute(TaskMapper mapper) {
                        List<ActBusinessStatus> list = mapper.queryTaskCandidateOrAssigned(uniqueCode, pageindex, pagesize);
                        Integer count = mapper.countTaskCandidateOrAssigned(uniqueCode);
                        PageSupport ps = new PageSupport(list, count, pageindex, pagesize);
                        return ps;
                    }
                };
        return managementService.executeCustomSql(customSqlExecution);
    }

    /**
     * 查询我的申请
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    public PageSupport<ActBusinessStatus> queryMyApply(final String uniqueCode, final int pageindex, final int pagesize) {
        CustomSqlExecution<TaskMapper, PageSupport<ActBusinessStatus>> customSqlExecution =
                new AbstractCustomSqlExecution<TaskMapper, PageSupport<ActBusinessStatus>>(TaskMapper.class) {
                    @Override
                    public PageSupport<ActBusinessStatus> execute(TaskMapper mapper) {
                        List<ActBusinessStatus> list = mapper.queryMyApply(uniqueCode, pageindex, pagesize);
                        Integer count = mapper.countMyApply(uniqueCode);
                        PageSupport ps = new PageSupport(list, count, pageindex, pagesize);
                        return ps;
                    }
                };
        return managementService.executeCustomSql(customSqlExecution);
    }

    /**
     * 查询我的已办
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    public PageSupport<ActBusinessStatus> queryMyJoin(final String uniqueCode, final int pageindex, final int pagesize) {
        CustomSqlExecution<TaskMapper, PageSupport<ActBusinessStatus>> customSqlExecution =
                new AbstractCustomSqlExecution<TaskMapper, PageSupport<ActBusinessStatus>>(TaskMapper.class) {
                    @Override
                    public PageSupport<ActBusinessStatus> execute(TaskMapper mapper) {
                        List<ActBusinessStatus> list = mapper.queryMyJoin(uniqueCode, pageindex, pagesize);
                        Integer count = mapper.countMyJoin(uniqueCode);
                        PageSupport ps = new PageSupport(list, count, pageindex, pagesize);
                        return ps;
                    }
                };
        return managementService.executeCustomSql(customSqlExecution);
    }

}
