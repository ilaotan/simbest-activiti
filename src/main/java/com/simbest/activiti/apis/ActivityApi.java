/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.apis;

import org.activiti.engine.impl.pvm.process.ActivityImpl;

import java.util.List;
import java.util.Map;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-08-18  15:38 
 */
public interface ActivityApi {

    /**
     * 获取当前运行节点
     * @param processInstanceId
     * @return
     */
    List<ActivityImpl> getRunningActivity(String processInstanceId);

    /**
     * 获取当前实例活动坐标信息
     *
     * @param processInstanceId
     * @return
     */
    List<Map<String, Object>> getRunningActivityLocation(String processInstanceId);
}
