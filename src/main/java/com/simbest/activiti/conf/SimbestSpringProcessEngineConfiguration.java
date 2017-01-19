/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.conf;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.cmd.SetProcessDefinitionVersionCmd;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.autodeployment.AutoDeploymentStrategy;

import java.util.List;

/**
 * 用途：自动部署流程定义时，同步迁移升级未执行完毕的流程实例
 * 参考:
 * https://community.alfresco.com/thread/222403-process-instances-migration
 * http://www.programcreek.com/java-api-examples/index.php?source_dir=xbpm5-master/activiti-engine/src/test/java/org/activiti/engine/test/db/ProcessInstanceMigrationTest.java
 */
public class SimbestSpringProcessEngineConfiguration extends SpringProcessEngineConfiguration {

    @Override
    protected void autoDeployResources(ProcessEngine processEngine) {
        if(this.deploymentResources != null && this.deploymentResources.length > 0) {
            AutoDeploymentStrategy strategy = this.getAutoDeploymentStrategy(this.deploymentMode);
            strategy.deployResources(this.deploymentName, this.deploymentResources, processEngine.getRepositoryService());

            //流程定义与在途流程实例在完成自动部署后，同步升级
            CommandExecutor commandExecutor = this.getCommandExecutor();
            List<ProcessDefinition> latestProcessDefinitionList = this.getRepositoryService().createProcessDefinitionQuery().latestVersion().list();
            for(ProcessDefinition latestProcessDefinition : latestProcessDefinitionList) {
                List<ProcessInstance> pis = this.getRuntimeService().createProcessInstanceQuery().processDefinitionKey(latestProcessDefinition.getKey()).list();
                for (ProcessInstance pi : pis) {
                    commandExecutor.execute(new SetProcessDefinitionVersionCmd(pi.getId(), latestProcessDefinition.getVersion()));
                }
            }
        }

    }
}
