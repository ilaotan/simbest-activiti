/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.apis.impl;

import com.simbest.activiti.apis.HistoryApi;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-06  11:31
 */
@Component
public class HistoryApiImpl implements HistoryApi {
    public transient final Log log = LogFactory.getLog(getClass());

    @Autowired
    private HistoryService historyService;

    /**
     * 获取历史活动
     * @param processInstanceId
     * @return
     */
    public List<HistoricActivityInstance> getActivityByInstance(String processInstanceId) {
        return historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceEndTime().desc()
                .list();
    }


}
