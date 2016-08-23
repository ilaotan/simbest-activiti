/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.query.service.impl;

import com.google.common.collect.Maps;
import com.simbest.activiti.query.mapper.ActBusinessStatusMapper;
import com.simbest.activiti.query.model.ActBusinessStatus;
import com.simbest.activiti.query.service.IActBusinessStatusService;
import com.simbest.cores.service.impl.GenericMapperService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-08-12  11:28 
 */
@Service
public class ActBusinessStatusService extends GenericMapperService<ActBusinessStatus,Long> implements IActBusinessStatusService {
    public final static Log log = LogFactory.getLog(ActBusinessStatusService.class);

    private ActBusinessStatusMapper mapper;

    public ActBusinessStatusService(SqlSession sqlSession, Class<ActBusinessStatus> persistentMapper) {
        super(sqlSession, persistentMapper);
    }

    @Autowired
    public ActBusinessStatusService(@Qualifier(value="sqlSessionTemplateSimple") SqlSession sqlSession) {
        super(sqlSession);
        this.mapper = sqlSession.getMapper(ActBusinessStatusMapper.class);
        super.setMapper(mapper);
    }

    @Override
    public ActBusinessStatus getByBusiness(String processDefinitionKey, Long businessKey, String code, Boolean iscg){
        ActBusinessStatus status = new ActBusinessStatus();
        status.setProcessDefinitionKey(processDefinitionKey);
        status.setBusinessKey(businessKey);
        status.setCode(code);
        status.setIscg(iscg);
        Collection<ActBusinessStatus> list = mapper.getAll(status);
        return list.size()>0 ? list.iterator().next():null;
    }

    @Override
    public ActBusinessStatus getByInstance(String processDefinitionId, String processInstanceId) {
        Map<String,Object> params = Maps.newHashMap();
        params.put("processDefinitionId",processDefinitionId);
        params.put("processInstanceId",processInstanceId);
        Collection<ActBusinessStatus> list = getAll(params);
        if(list != null && list.size()>0)
            return list.iterator().next();
        else
            return null;
    }

    @Override
    public ActBusinessStatus getByTask(String taskId) {
        Map<String,Object> params = Maps.newHashMap();
        params.put("taskId",taskId);
        Collection<ActBusinessStatus> list = getAll(params);
        if(list != null && list.size()>0)
            return list.iterator().next();
        else
            return null;
    }
}
