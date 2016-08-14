/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.query.service.impl;

import com.simbest.activiti.query.mapper.MyTaskMapper;
import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.activiti.query.service.IMyTaskService;
import com.simbest.cores.utils.pages.PageSupport;
import org.activiti.engine.ManagementService;
import org.activiti.engine.impl.cmd.AbstractCustomSqlExecution;
import org.activiti.engine.impl.cmd.CustomSqlExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-06  11:31
 */
@Service
public class MyTaskService implements IMyTaskService {

    @Autowired
    protected ManagementService managementService;

    public PageSupport<ActBusinessStatus> queryMyTask(final String uniqueCode, final int pageindex, final int pagesize) {
        CustomSqlExecution<MyTaskMapper, PageSupport<ActBusinessStatus>> customSqlExecution =
                new AbstractCustomSqlExecution<MyTaskMapper, PageSupport<ActBusinessStatus>>(MyTaskMapper.class) {
                    @Override
                    public PageSupport<ActBusinessStatus> execute(MyTaskMapper mapper) {
                        List<ActBusinessStatus> list = mapper.queryTaskCandidateOrAssigned(uniqueCode, pageindex, pagesize);
                        Integer count = mapper.countTaskCandidateOrAssigned(uniqueCode);
                        PageSupport ps = new PageSupport(list, count, pageindex, pagesize);
                        return ps;
                    }
                };
        return managementService.executeCustomSql(customSqlExecution);
    }

    @Override
    public PageSupport<ActBusinessStatus> queryMyApply(final String uniqueCode, final int pageindex, final int pagesize) {
        CustomSqlExecution<MyTaskMapper, PageSupport<ActBusinessStatus>> customSqlExecution =
                new AbstractCustomSqlExecution<MyTaskMapper, PageSupport<ActBusinessStatus>>(MyTaskMapper.class) {
                    @Override
                    public PageSupport<ActBusinessStatus> execute(MyTaskMapper mapper) {
                        List<ActBusinessStatus> list = mapper.queryMyApply(uniqueCode, pageindex, pagesize);
                        Integer count = mapper.countMyApply(uniqueCode);
                        PageSupport ps = new PageSupport(list, count, pageindex, pagesize);
                        return ps;
                    }
                };
        return managementService.executeCustomSql(customSqlExecution);
    }
}
