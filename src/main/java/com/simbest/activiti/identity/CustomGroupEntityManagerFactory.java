/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.identity;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.GroupIdentityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-08-05  13:51 
 */
//@Service
public class CustomGroupEntityManagerFactory implements SessionFactory {

    @Autowired
    private CustomGroupEntityManager customGroupEntityManager;

    public void setCustomGroupEntityManager(CustomGroupEntityManager customGroupEntityManager) {
        this.customGroupEntityManager = customGroupEntityManager;
    }

    @Override
    public Class<?> getSessionType() {
        // 返回原始的GroupIdentityManager类型
        return GroupIdentityManager.class;
    }

    @Override
    public Session openSession() {
        return customGroupEntityManager;
    }
}
