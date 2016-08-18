/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.apis;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-08-18  15:38 
 */
public interface InstanceApi {

    /**
     * 启动流程
     * @param processDefinitionKey
     * @param businessKey
     * @param variables
     */
    ProcessInstance startProcessInstance(String processDefinitionKey, String businessKey, Map<String, Object> variables);

    /**
     * 根据实例Id，获取流程实例
     * @param instanceId
     * @return
     */
    ProcessInstance getProcessInstance(String processInstanceId);

    /**
     * 根据实例Id，获取评论意见
     * @param processInstanceId
     * @return
     */
    List<Comment> getCommentByInstance(String processInstanceId);
}
