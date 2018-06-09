/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.query.mapper;

import com.simbest.activiti.query.model.ActBusinessStatus;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface CustomTaskMapper {

	/*我的待办*/
    List<ActBusinessStatus> queryMyTask(Map<String,Object> map, RowBounds rowBounds);
    
    List<ActBusinessStatus> queryMyTask(Map<String,Object> map);
    
    Integer countMyTask(Map<String,Object> map);
    
    /*我的申请*/
    List<ActBusinessStatus> queryMyApply(Map<String,Object> map, RowBounds rowBounds);
    
    List<ActBusinessStatus> queryMyApply(Map<String,Object> map);

    Integer countMyApply(Map<String,Object> map);
    
    /*我的草稿*/
    List<ActBusinessStatus> queryMyDraft(Map<String,Object> map, RowBounds rowBounds);
    
    List<ActBusinessStatus> queryMyDraft(Map<String,Object> map);
    
    Integer countMyDraft(Map<String,Object> map);

    
    /*我的已办*/
    List<ActBusinessStatus> queryMyJoin(Map<String,Object> map, RowBounds rowBounds);
    
    List<ActBusinessStatus> queryMyJoin(Map<String,Object> map);
    
    Integer countMyJoin(Map<String,Object> map);
}
