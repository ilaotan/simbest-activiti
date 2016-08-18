/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.apis;

import org.activiti.engine.repository.Deployment;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-08-18  15:38 
 */
public interface DeploymentApi {

    /**
     * 部署流程
     * @param is
     * @param fileName
     * @return
     */
    Deployment deployProcess(InputStream is, String fileName);

    /**
     * 获取流程部署图片资源
     *
     * @param deploymentId
     * @param imageName
     * @return
     */
    InputStream findImageInputStream(String deploymentId,
                                            String imageName);
}
