package com.simbest.activiti.web;

import com.simbest.activiti.apis.EngineServiceApi;
import com.wordnik.swagger.annotations.ApiOperation;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 部署流程
 */
@Controller
@RequestMapping(value = {"/action/sso/activiti/deployment", "/action/activiti/deployment"})
public class DeploymentController extends ActivitiBaseController {
    public transient final Log log = LogFactory.getLog(getClass());

    @Autowired
    private EngineServiceApi api;

    /**
     * 流程定义列表
     */
    @RequestMapping(value = "/processList", method = RequestMethod.GET)
    @ApiOperation(value = "打开流程定义列表页面", httpMethod = "GET", notes = "打开流程定义列表页面")
    public ModelAndView processList() {
        ModelAndView mav = new ModelAndView("action/activiti/processList");
        List<ProcessDefinition> processDefinitionList = api.getRepositoryService().createProcessDefinitionQuery().list();
        mav.addObject("processDefinitionList", processDefinitionList);
        return mav;
    }

    /**
     * 部署流程资源
     */
    @RequestMapping(value = "/deploy", method = RequestMethod.POST)
    @ApiOperation(value = "部署流程资源", httpMethod = "POST", notes = "部署流程资源",
            produces="application/application/x-www-form-urlencodedn",consumes="application/application/x-www-form-urlencoded")
    public String deploy(@RequestParam(value = "file", required = true) MultipartFile file) {

        // 获取上传的文件名
        String fileName = file.getOriginalFilename();

        try {
            // 得到输入流（字节流）对象
            InputStream fileInputStream = file.getInputStream();

            // 文件的扩展名
            String extension = FilenameUtils.getExtension(fileName);

            // zip或者bar类型的文件用ZipInputStream方式部署
            DeploymentBuilder deployment = api.getRepositoryService().createDeployment();
            if (extension.equals("zip") || extension.equals("bar")) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment.addZipInputStream(zip);
            } else {
                // 其他类型的文件直接部署
                deployment.addInputStream(fileName, fileInputStream);
            }
            deployment.deploy();
        } catch (Exception e) {
            log.error("error on deploy process, because of file input stream");
        }

        return "redirect:process-list";
    }

    /**
     * 读取流程资源
     *
     * @param processDefinitionId 流程定义ID
     * @param resourceName        资源名称
     */
    @RequestMapping(value = "/read-resource", method = RequestMethod.POST)
    @ApiOperation(value = "读取流程资源", httpMethod = "POST", notes = "读取流程资源",
            produces="application/application/x-www-form-urlencodedn",consumes="application/application/x-www-form-urlencoded")
    public void readResource(@RequestParam("pdid") String processDefinitionId, @RequestParam("resourceName") String resourceName, HttpServletResponse response)
            throws Exception {
        ProcessDefinitionQuery pdq = api.getRepositoryService().createProcessDefinitionQuery();
        ProcessDefinition pd = pdq.processDefinitionId(processDefinitionId).singleResult();

        // 通过接口读取
        InputStream resourceAsStream = api.getRepositoryService().getResourceAsStream(pd.getDeploymentId(), resourceName);

        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    /**
     * 删除部署的流程，级联删除流程实例
     *
     * @param deploymentId 流程部署ID
     */
    @RequestMapping(value = "/deleteProcessDefinitionById", method = RequestMethod.POST)
    @ApiOperation(value = "删除部署的流程，级联删除流程实例", httpMethod = "POST", notes = "删除部署的流程，级联删除流程实例",
            produces="application/application/x-www-form-urlencodedn",consumes="application/application/x-www-form-urlencoded")
    public String deleteProcessDefinitionById(@RequestParam("deploymentId") String deploymentId) {
        api.getRepositoryService().deleteDeployment(deploymentId, true);
        return "redirect:process-list";
    }

    /**
     * 删除部署的流程，级联删除流程实例
     *
     * @param deploymentKey 流程部署Key
     */
    @RequestMapping(value = "/deleteProcessDefinitionByKey", method = RequestMethod.POST)
    @ApiOperation(value = "删除部署的流程，级联删除流程实例", httpMethod = "POST", notes = "删除部署的流程，级联删除流程实例",
            produces="application/application/x-www-form-urlencodedn",consumes="application/application/x-www-form-urlencoded")
    public String deleteProcessDefinitionByKey(@RequestParam("deploymentKey") String deploymentKey) throws Exception {
        List<ProcessDefinition> list = api.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(deploymentKey).list();
        for (ProcessDefinition pd : list) {
            api.getRepositoryService().deleteDeployment(pd.getDeploymentId(), true);
        }
        return "redirect:process-list";
    }
}