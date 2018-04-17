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

import java.util.List;


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
        List<ActBusinessStatus> list = mapper.queryMyTask(uniqueCode,code,title,processDefinitionKeys, new RowBounds(pageindex, pagesize));
        Integer count = mapper.countMyTask(uniqueCode,code,title,processDefinitionKeys);
        PageSupport ps = new PageSupport(list, count, pageindex, pagesize);
        return ps;
    }
    
    /**
     * 查询我的待办数量
     * @param uniqueCode
     * @return
     */
    public Integer queryMyTaskCount(String uniqueCode){
        Integer count = mapper.countMyTask(uniqueCode,null,null,null);
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
    public PageSupport<ActBusinessStatus> queryMyApply(String uniqueCode,String code,String title,String processDefinitionKeys, int pageindex, int pagesize) {
        List<ActBusinessStatus> list = mapper.queryMyApply(uniqueCode,code,title,processDefinitionKeys, new RowBounds(pageindex, pagesize));
        Integer count = mapper.countMyApply(uniqueCode,code,title,processDefinitionKeys);
        PageSupport ps = new PageSupport(list, count, pageindex, pagesize);
        return ps;
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
        List<ActBusinessStatus> list = mapper.queryMyDraft(uniqueCode,code,title,processDefinitionKeys, new RowBounds(pageindex, pagesize));
        Integer count = mapper.countMyDraft(uniqueCode,code,title,processDefinitionKeys);
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
    public PageSupport<ActBusinessStatus> queryMyJoin(String uniqueCode,String code,String title,String processDefinitionKeys, int pageindex, int pagesize) {
        List<ActBusinessStatus> list = mapper.queryMyJoin(uniqueCode,code,title,processDefinitionKeys, new RowBounds(pageindex, pagesize));
        Integer count = mapper.countMyJoin(uniqueCode,code,title,processDefinitionKeys);
        PageSupport ps = new PageSupport(list, count, pageindex, pagesize);
        return ps;
    }





    public void setMapper(CustomTaskMapper mapper) {
        this.mapper = mapper;
    }
}
