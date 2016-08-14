/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.listener;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.BaseEntityEventListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-08-10  17:37 
 */
@Component
public class EntityEventListener extends BaseEntityEventListener {
    public transient final Log log = LogFactory.getLog(getClass());

    @Override
    protected void onCreate(ActivitiEvent event) {
        ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
        log.debug("创建了实体：" + entityEvent.getEntity());
        super.onCreate(event);
    }

    @Override
    protected void onDelete(ActivitiEvent event) {
        ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
        log.debug("删除了实体：" + entityEvent.getEntity());
        super.onDelete(event);
    }

    @Override
    protected void onUpdate(ActivitiEvent event) {
        ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
        log.debug("更新了实体：" + entityEvent.getEntity());
        super.onUpdate(event);
    }

}
