/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.apis.impl;

import com.simbest.activiti.apis.DeploymentApi;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-17  22:50
 */
@Component
public class DeploymentApiImpl implements DeploymentApi {
    @Autowired
    private RepositoryService repositoryService;

    /**
     * 部署流程
     * @param is
     * @param fileName
     * @return
     */
    public Deployment deployProcess(InputStream is, String fileName){
        // 文件的扩展名
        String extension = FilenameUtils.getExtension(fileName);
        // zip或者bar类型的文件用ZipInputStream方式部署
        DeploymentBuilder deployment = repositoryService.createDeployment();
        if (extension.equals("zip") || extension.equals("bar")) {
            ZipInputStream zip = new ZipInputStream(is);
            deployment.addZipInputStream(zip);
        } else {
            // 其他类型的文件直接部署
            deployment.addInputStream(fileName, is);
        }
        return deployment.deploy();
    }

    /**
     * 获取流程部署图片资源
     *
     * @param deploymentId
     * @param imageName
     * @return
     */
    public InputStream findImageInputStream(String deploymentId,
                                            String imageName) {
        return repositoryService.getResourceAsStream(deploymentId, imageName);
    }
}
