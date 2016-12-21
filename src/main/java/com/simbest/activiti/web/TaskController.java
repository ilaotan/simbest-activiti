/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.web;

import com.simbest.activiti.apis.EngineServiceApi;
import com.wordnik.swagger.annotations.ApiOperation;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
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
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping(value = {"/action/sso/activiti/task", "/action/activiti/task"})
public class TaskController extends ActivitiBaseController {
    public transient final Log log = LogFactory.getLog(getClass());

    @Autowired
    private EngineServiceApi api;
    
    private static String TASK_LIST = "redirect:/chapter6/task/list";

    /**
     * 读取启动流程的表单字段
     */
    @RequestMapping(value = "task/list", method = RequestMethod.POST)
    @ApiOperation(value = "读取启动流程的表单字段", httpMethod = "POST", notes = "读取启动流程的表单字段",
            produces="application/application/x-www-form-urlencodedn",consumes="application/application/x-www-form-urlencoded")
    public ModelAndView todoTasks(HttpSession session) throws Exception {
        String viewName = "chapter6/task-list";
        ModelAndView mav = new ModelAndView(viewName);

        /*// 读取直接分配给当前人或者已经签收的任务
        List<Task> doingTasks = api.getTaskService().createTaskQuery().taskAssignee(user.getId()).list();

        // 等待签收的任务
        List<Task> waitingClaimTasks = api.getTaskService().createTaskQuery().taskCandidateUser(user.getId()).list();

        // 合并两种任务
        List<Task> allTasks = new ArrayList<Task>();
        allTasks.addAll(doingTasks);
        allTasks.addAll(waitingClaimTasks);*/

        // 5.16版本可以使用一下代码待办查询
        List<Task> tasks = api.getTaskService().createTaskQuery().taskCandidateOrAssigned("lishuyi").list();

        mav.addObject("tasks", tasks);
        return mav;
    }

    /**
     * 签收任务
     */
    @RequestMapping(value = "task/claim/{id}", method = RequestMethod.POST)
    @ApiOperation(value = "签收任务", httpMethod = "POST", notes = "签收任务",
            produces="application/application/x-www-form-urlencodedn",consumes="application/application/x-www-form-urlencoded")
    public String claim(@PathVariable("id") String taskId, HttpSession session, RedirectAttributes redirectAttributes) {
        api.getTaskService().claim(taskId, "lishuyi");
        redirectAttributes.addFlashAttribute("message", "任务已签收");
        return TASK_LIST;
    }

    /**
     * 读取用户任务的表单字段
     */
    @RequestMapping(value = "task/getform/{taskId}", method = RequestMethod.POST)
    @ApiOperation(value = "读取用户任务的表单字段", httpMethod = "POST", notes = "读取用户任务的表单字段",
            produces="application/application/x-www-form-urlencodedn",consumes="application/application/x-www-form-urlencoded")
    public ModelAndView readTaskForm(@PathVariable("taskId") String taskId) throws Exception {
        String viewName = "chapter6/task-form";
        ModelAndView mav = new ModelAndView(viewName);
        TaskFormData taskFormData = api.getFormService().getTaskFormData(taskId);
        if (taskFormData.getFormKey() != null) { //动态表单
            Object renderedTaskForm = api.getFormService().getRenderedTaskForm(taskId);
            Task task = api.getTaskService().createTaskQuery().taskId(taskId).singleResult();
            mav.addObject("task", task);
            mav.addObject("taskFormData", renderedTaskForm);
            mav.addObject("hasFormKey", true);
        } else {
            mav.addObject("taskFormData", taskFormData);//formkey表单
        }
        return mav;
    }

    /**
     * 读取启动流程的表单字段
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "task/complete/{taskId}", method = RequestMethod.POST)
    @ApiOperation(value = "读取启动流程的表单字段", httpMethod = "POST", notes = "读取启动流程的表单字段",
            produces="application/application/x-www-form-urlencodedn",consumes="application/application/x-www-form-urlencoded")
    public String completeTask(@PathVariable("taskId") String taskId, HttpServletRequest request) throws Exception {
        TaskFormData taskFormData = api.getFormService().getTaskFormData(taskId);
        String formKey = taskFormData.getFormKey();
        // 从请求中获取表单字段的值
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        Map<String, String> formValues = new HashMap<String, String>();

        if (StringUtils.isNotBlank(formKey)) { // formkey表单
            Map<String, String[]> parameterMap = request.getParameterMap();
            Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();
            for (Map.Entry<String, String[]> entry : entrySet) {
                String key = entry.getKey();
                formValues.put(key, entry.getValue()[0]);
            }
        } else { // 动态表单
            for (FormProperty formProperty : formProperties) {
                if (formProperty.isWritable()) {
                    String value = request.getParameter(formProperty.getId());
                    formValues.put(formProperty.getId(), value);
                }
            }
        }
        api.getFormService().submitTaskFormData(taskId, formValues);
        return TASK_LIST;
    }

}

