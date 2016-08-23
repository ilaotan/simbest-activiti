/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.apis;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import java.util.List;
import java.util.Map;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-08-18  15:38 
 */
public interface HistoryApi {

    /**
     * 获取历史活动
     * @param processInstanceId
     * @return
     */
    List<HistoricActivityInstance> getActivityByInstance(String processInstanceId);
}
