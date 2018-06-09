/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.query.service.impl;

import com.simbest.activiti.query.mapper.CustomTaskMapper;
import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.activiti.query.service.ICustomTaskService;
import com.simbest.cores.utils.pages.PageSupport;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CustomTaskService implements ICustomTaskService {
    @Autowired
    private CustomTaskMapper mapper;

    /**
     * 查询我的待办
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    public PageSupport<ActBusinessStatus> queryMyTask(String uniqueCode,String code,String title,String processDefinitionKeys, int pageindex, int pagesize){
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("uniqueCode", uniqueCode);
    	map.put("code", code);
    	map.put("title", title);
    	map.put("processDefinitionKeys", processDefinitionKeys);
    	List<ActBusinessStatus> list = mapper.queryMyTask(map, new RowBounds(pageindex, pagesize));
        Integer count = mapper.countMyTask(map);
        PageSupport ps = new PageSupport(list, count, pageindex, pagesize);
        return ps;
    }
    
    /**
     * 查询我的待办数量
     * @param uniqueCode
     * @return
     */
    public Integer queryMyTaskCount(String uniqueCode){
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("uniqueCode", uniqueCode);
        Integer count = mapper.countMyTask(map);
        return count;
    }

    /**
     * 查询我的申请
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    @Override
    public PageSupport<ActBusinessStatus> queryMyApply(String uniqueCode,String code,String title,String processDefinitionKeys,Date startTime,Date endTime,String delegationState, int pageindex, int pagesize) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("uniqueCode", uniqueCode);
    	map.put("code", code);
    	map.put("title", title);
    	map.put("processDefinitionKeys", processDefinitionKeys);
    	map.put("startTime", startTime);
    	map.put("endTime", endTime);
    	
    	if(delegationState!=null){
    		if("0".equals(delegationState)){//未结束
    			map.put("endTimenull", "1");
    		}
    		if("1".equals(delegationState)){//已结束
    			map.put("endTimevalue", "1");
    		}
    		if("2".equals(delegationState)){//已终止
    			map.put("isstop", "1");
    		}
    	}
    	
    	List<ActBusinessStatus> list = mapper.queryMyApply(map, new RowBounds(pageindex, pagesize));
        Integer count = mapper.countMyApply(map);
        PageSupport ps = new PageSupport(list, count, pageindex, pagesize);
        return ps;
    }
    
    /**
     * 查询我的申请
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    @Override
    public List<ActBusinessStatus> queryMyApplyList(String uniqueCode,String code,String title,String processDefinitionKeys,Date startTime,Date endTime,String delegationState) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("uniqueCode", uniqueCode);
    	map.put("code", code);
    	map.put("title", title);
    	map.put("processDefinitionKeys", processDefinitionKeys);
    	map.put("startTime", startTime);
    	map.put("endTime", endTime);
    	if(delegationState!=null){
    		if("0".equals(delegationState)){//未结束
    			map.put("endTimenull", "1");
    		}
    		if("1".equals(delegationState)){//已结束
    			map.put("endTimevalue", "1");
    		}
    		if("2".equals(delegationState)){//已终止
    			map.put("isstop", "1");
    		}
    	}
        List<ActBusinessStatus> list = mapper.queryMyApply(map);
        return list;
    }

    /**
     * 查询我的草稿
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    @Override
    public PageSupport<ActBusinessStatus> queryMyDraft(String uniqueCode,String code,String title,String processDefinitionKeys, int pageindex, int pagesize){
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("uniqueCode", uniqueCode);
    	map.put("code", code);
    	map.put("title", title);
    	map.put("processDefinitionKeys", processDefinitionKeys);
    	List<ActBusinessStatus> list = mapper.queryMyDraft(map, new RowBounds(pageindex, pagesize));
        Integer count = mapper.countMyDraft(map);
        PageSupport ps = new PageSupport(list, count, pageindex, pagesize);
        return ps;
    }

    /**
     * 查询我的已办
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    @Override
    public PageSupport<ActBusinessStatus> queryMyJoin(String uniqueCode,String code,String title,String processDefinitionKeys,Date startTime,Date endTime,String delegationState, int pageindex, int pagesize) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("uniqueCode", uniqueCode);
    	map.put("code", code);
    	map.put("title", title);
    	map.put("processDefinitionKeys", processDefinitionKeys);
    	map.put("startTime", startTime);
    	map.put("endTime", endTime);
    	if(delegationState!=null){
    		if("0".equals(delegationState)){//未结束
    			map.put("endTimenull", "1");
    		}
    		if("1".equals(delegationState)){//已结束
    			map.put("endTimevalue", "1");
    		}
    		if("2".equals(delegationState)){//已终止
    			map.put("isstop", "1");
    		}
    	}
    	List<ActBusinessStatus> list = mapper.queryMyJoin(map, new RowBounds(pageindex, pagesize));
        Integer count = mapper.countMyJoin(map);
        PageSupport ps = new PageSupport(list, count, pageindex, pagesize);
        return ps;
    }
    
    
    /**
     * 查询我的已办
     * @param uniqueCode
     * @param pageindex
     * @param pagesize
     * @return
     */
    @Override
    public List<ActBusinessStatus> queryMyJoinList(String uniqueCode,String code,String title,String processDefinitionKeys,Date startTime,Date endTime,String delegationState) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("uniqueCode", uniqueCode);
    	map.put("code", code);
    	map.put("title", title);
    	map.put("processDefinitionKeys", processDefinitionKeys);
    	map.put("startTime", startTime);
    	map.put("endTime", endTime);
    	if(delegationState!=null){
    		if("0".equals(delegationState)){//未结束
    			map.put("endTimenull", "1");
    		}
    		if("1".equals(delegationState)){//已结束
    			map.put("endTimevalue", "1");
    		}
    		if("2".equals(delegationState)){//已终止
    			map.put("isstop", "1");
    		}
    	}
    	List<ActBusinessStatus> list = mapper.queryMyJoin(map);
        return list;
    }





    public void setMapper(CustomTaskMapper mapper) {
        this.mapper = mapper;
    }
}
