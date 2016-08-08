/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.query.web;

import com.simbest.activiti.query.manager.CustomRunningTaskManager;
import com.simbest.activiti.web.ActivitiBaseController;
import com.simbest.cores.model.JsonResponse;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping(value = {"/action/sso/activiti/runningTask", "/action/activiti/runningTask"})
public class CustomRunningTaskController extends ActivitiBaseController {

    @Autowired
    private CustomRunningTaskManager manager;

    /**
     * 读取启动流程的表单字段
     */
    @RequestMapping(value = "/getRunningTasks/{loginName}")
    @ResponseBody
    public JsonResponse getRunningTasks(@PathVariable("loginName") String loginName) throws Exception {
        JsonResponse response = new JsonResponse();
        response.setResponseid(1);
        response.setData(manager.getRunningTasks(loginName, 0, 2));
        return response;
    }


}

