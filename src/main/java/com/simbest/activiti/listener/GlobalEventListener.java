/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.listener;

import com.simbest.cores.utils.configs.CoreConfig;
import org.activiti.engine.delegate.event.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-10  15:36
 */
@Component
public class GlobalEventListener implements ActivitiEventListener {
    public transient final Log log = LogFactory.getLog(getClass());
    
    @Autowired
    private CoreConfig config;


    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEventType eventType = event.getType();
        switch (eventType) {
            case ENGINE_CREATED:
                log.debug("引擎初始化成功！");
                break;
            case ENGINE_CLOSED:
                log.debug("引擎已关闭！");
                break;
            case ENTITY_CREATED:
                ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
                log.debug("创建了实体：" + entityEvent.getEntity());
                break;
            case ENTITY_INITIALIZED:
                entityEvent = (ActivitiEntityEvent) event;
                log.debug("实体初始化完毕：" + entityEvent.getEntity());
                break;
            case ACTIVITY_COMPLETED:
                ActivitiActivityEvent activityEvent = (ActivitiActivityEvent) event;
                log.debug("活动执行完毕：" + activityEvent.getActivityName()+activityEvent.getActivityType()+activityEvent.getActivityId());
                break;
            default:
                log.debug("捕获到事件[需要处理]：" + eventType.name() + ", type=" + event.getType()+" ToString is :"+ToStringBuilder.reflectionToString(event));
                log.debug("------------------------------------------------");
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
