/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.query.web;

import com.simbest.activiti.apis.TaskApi;
import com.simbest.activiti.web.ActivitiBaseController;
import com.simbest.cores.model.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = {"/action/sso/activiti/myTask", "/action/activiti/myTask"})
public class MyTaskController extends ActivitiBaseController {

    @Autowired
    private TaskApi taskApi;

    /**
     * 查询我的待办
     * @param uniqueCode
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryMyTask/{uniqueCode}/{pageindex}/{pagesize}", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ResponseBody
    public JsonResponse queryMyTask(@PathVariable("uniqueCode") String uniqueCode, @PathVariable("pageindex") int pageindex, @PathVariable("pagesize") int pagesize) throws Exception {
        JsonResponse response = new JsonResponse();
        response.setResponseid(1);
        response.setData(taskApi.queryMyTask(uniqueCode, pageindex, pagesize));
        return response;
    }

    /**
     * 查询我的申请
     * @param uniqueCode
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryMyApply/{uniqueCode}/{pageindex}/{pagesize}", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ResponseBody
    public JsonResponse queryMyApply(@PathVariable("uniqueCode") String uniqueCode, @PathVariable("pageindex") int pageindex, @PathVariable("pagesize") int pagesize) throws Exception {
        JsonResponse response = new JsonResponse();
        response.setResponseid(1);
        response.setData(taskApi.queryMyApply(uniqueCode, pageindex, pagesize));
        return response;
    }

    /**
     * 查询我的已办
     * @param uniqueCode
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryMyJoin/{uniqueCode}/{pageindex}/{pagesize}", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ResponseBody
    public JsonResponse queryMyJoin(@PathVariable("uniqueCode") String uniqueCode, @PathVariable("pageindex") int pageindex, @PathVariable("pagesize") int pagesize) throws Exception {
        JsonResponse response = new JsonResponse();
        response.setResponseid(1);
        response.setData(taskApi.queryMyJoin(uniqueCode, pageindex, pagesize));
        return response;
    }
}

