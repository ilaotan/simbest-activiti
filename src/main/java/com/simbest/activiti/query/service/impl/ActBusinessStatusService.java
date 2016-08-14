/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.query.service.impl;

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
}
