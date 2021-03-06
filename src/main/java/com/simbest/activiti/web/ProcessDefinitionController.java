/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.web;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-08-05  9:06 
 */

import com.simbest.activiti.apis.EngineServiceApi;
import com.wordnik.swagger.annotations.ApiOperation;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping(value = {"/action/sso/activiti/procdef","/action/activiti/procdef" })
public class ProcessDefinitionController extends ActivitiBaseController{
    public transient final Log log = LogFactory.getLog(getClass());

    @Autowired
    private EngineServiceApi api;

    /**
     * 读取启动流程的表单字段
     */
    @RequestMapping(value = "getform/start/{processDefinitionId}", method = RequestMethod.GET)
    @ApiOperation(value = "读取启动流程的表单字段", httpMethod = "POST", notes = "读取启动流程的表单字段",
            produces="application/application/x-www-form-urlencodedn",consumes="application/application/x-www-form-urlencoded")
    public ModelAndView readStartForm(@PathVariable("processDefinitionId") String processDefinitionId) throws Exception {
        ProcessDefinition processDefinition = api.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        boolean hasStartFormKey = processDefinition.hasStartFormKey();

        // 根据是否有formkey属性判断使用哪个展示层
        String viewName = "chapter6/start-process-form";
        ModelAndView mav = new ModelAndView(viewName);

        // 判断是否有formkey属性
        if (hasStartFormKey) {
            Object renderedStartForm = api.getFormService().getRenderedStartForm(processDefinitionId);
            mav.addObject("startFormData", renderedStartForm);
            mav.addObject("processDefinition", processDefinition);
        } else { // 动态表单字段
            StartFormData startFormData = api.getFormService().getStartFormData(processDefinitionId);
            mav.addObject("startFormData", startFormData);
        }
        mav.addObject("hasStartFormKey", hasStartFormKey);
        mav.addObject("processDefinitionId", processDefinitionId);
        return mav;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "process-instance/start/{processDefinitionId}", method = RequestMethod.POST)
    @ApiOperation(value = "启动流程实例", httpMethod = "POST", notes = "启动流程实例",
            produces="application/application/x-www-form-urlencodedn",consumes="application/application/x-www-form-urlencoded")
    public String startProcessInstance(@PathVariable("processDefinitionId") String pdid, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        ProcessDefinition processDefinition = api.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(pdid).singleResult();
        boolean hasStartFormKey = processDefinition.hasStartFormKey();

        Map<String, String> formValues = new HashMap<String, String>();

        if (hasStartFormKey) { // formkey表单
            Map<String, String[]> parameterMap = request.getParameterMap();
            Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();
            for (Map.Entry<String, String[]> entry : entrySet) {
                String key = entry.getKey();
                formValues.put(key, entry.getValue()[0]);
            }
        } else { // 动态表单
            // 先读取表单字段在根据表单字段的ID读取请求参数值
            StartFormData formData = api.getFormService().getStartFormData(pdid);

            // 从请求中获取表单字段的值
            List<FormProperty> formProperties = formData.getFormProperties();
            for (FormProperty formProperty : formProperties) {
                String value = request.getParameter(formProperty.getId());
                formValues.put(formProperty.getId(), value);
            }
        }

        // 获取当前登录的用户
        api.getIdentityService().setAuthenticatedUserId("lishuyi");

        // 提交表单字段并启动一个新的流程实例
        ProcessInstance processInstance = api.getFormService().submitStartFormData(pdid, formValues);
        log.debug(String.format("start a processinstance: %s", processInstance.toString()));
        redirectAttributes.addFlashAttribute("message", "流程已启动，实例ID：" + processInstance.getId());
        return "redirect:/chapter5/process-list";
    }

}
