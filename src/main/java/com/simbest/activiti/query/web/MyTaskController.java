/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.query.web;

import com.simbest.activiti.apis.impl.TaskApiImpl;
import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.activiti.web.ActivitiBaseController;
import com.simbest.cores.model.JsonResponse;
import com.simbest.cores.shiro.AppUserSession;
import com.simbest.cores.utils.pages.PageSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = {"/action/sso/activiti/myTask", "/action/activiti/myTask"})
public class MyTaskController extends ActivitiBaseController {

    @Autowired
    private AppUserSession appUserSession;

    @Autowired
    private TaskApiImpl taskApi;

    /**
     * 查询我的待办
     *
     * @param uniqueCode
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryMyTask/{pageindex}/{pagesize}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JsonResponse queryMyTask(@PathVariable("pageindex") int pageindex, @PathVariable("pagesize") int pagesize) throws Exception {
        JsonResponse response = new JsonResponse();
        response.setResponseid(1);
        PageSupport<ActBusinessStatus> list = taskApi.queryMyTask(appUserSession.getCurrentUser().getUniqueCode(), pageindex, pagesize);
        Map<String, Object> dataMap = wrapQueryResult(list);
        response.setData(dataMap);
        return response;
    }

    /**
     * 查询我的申请
     *
     * @param uniqueCode
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryMyApply/{pageindex}/{pagesize}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JsonResponse queryMyApply(@PathVariable("pageindex") int pageindex, @PathVariable("pagesize") int pagesize) throws Exception {
        JsonResponse response = new JsonResponse();
        response.setResponseid(1);
        PageSupport<ActBusinessStatus> list = taskApi.queryMyApply(appUserSession.getCurrentUser().getUniqueCode(), pageindex, pagesize);
        Map<String, Object> dataMap = wrapQueryResult(list);
        response.setData(dataMap);
        return response;
    }

    /**
     * 查询我的草稿
     *
     * @param uniqueCode
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryMyDraft/{pageindex}/{pagesize}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JsonResponse queryMyDraft(@PathVariable("pageindex") int pageindex, @PathVariable("pagesize") int pagesize) throws Exception {
        JsonResponse response = new JsonResponse();
        response.setResponseid(1);
        PageSupport<ActBusinessStatus> list = taskApi.queryMyDraft(appUserSession.getCurrentUser().getUniqueCode(), pageindex, pagesize);
        Map<String, Object> dataMap = wrapQueryResult(list);
        response.setData(dataMap);
        return response;
    }

    /**
     * 查询我的已办
     *
     * @param uniqueCode
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryMyJoin/{pageindex}/{pagesize}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JsonResponse queryMyJoin(@PathVariable("pageindex") int pageindex, @PathVariable("pagesize") int pagesize) throws Exception {
        JsonResponse response = new JsonResponse();
        response.setResponseid(1);
        PageSupport<ActBusinessStatus> list = taskApi.queryMyJoin(appUserSession.getCurrentUser().getUniqueCode(), pageindex, pagesize);
        Map<String, Object> dataMap = wrapQueryResult(list);
        response.setData(dataMap);
        return response;
    }

    /**
     * 查询任务办理人和候选人
     *
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryToDoUser/{taskId}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JsonResponse queryToDoUser(@PathVariable("taskId") String taskId) throws Exception {
        JsonResponse response = new JsonResponse();
        response.setResponseid(1);
        List<String> list = taskApi.queryToDoUser(taskId);
        Map<String, Object> dataMap = wrapQueryResult(list);
        response.setData(dataMap);
        return response;
    }
}

