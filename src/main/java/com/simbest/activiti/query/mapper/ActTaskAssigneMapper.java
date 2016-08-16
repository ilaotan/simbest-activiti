package com.simbest.activiti.query.mapper;

import com.simbest.activiti.query.model.ActTaskAssigne;
import com.simbest.cores.mapper.IGenericMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ActTaskAssigneMapper extends IGenericMapper<ActTaskAssigne,Long> {

    /**
     * 查询任务办理人
     * @param taskId
     * @return
     */
    @Select("SELECT RES.assignee_ FROM ACT_RU_TASK RES WHERE res.id_=#{taskId} AND res.assignee_ IS NOT NULL")
    List<String> queryAssignee(@Param("taskId") String taskId);

    /**
     * 查询任务候选人
     * @param taskId
     * @return
     */
    @Select("SELECT i.user_ID_ FROM ACT_RU_IDENTITYLINK I WHERE i.task_id_=#{taskId} AND i.type_='candidate' AND i.user_ID_ IS NOT NULL\n" +
            "UNION ALL\n" +
            "SELECT g.user_id FROM sys_user_group g INNER JOIN ACT_RU_IDENTITYLINK I ON g.group_id=i.group_id_ WHERE i.task_id_=#{taskId} AND i.type_='candidate' AND i.group_id_ IS NOT NULL")
    List<String> queryCandidate(@Param("taskId") String taskId);
}
