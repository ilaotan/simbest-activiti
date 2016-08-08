/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.query.mapper;

import com.simbest.activiti.query.model.CustomRunningTask;
import com.simbest.cores.utils.pages.PageSupport;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-08-06  11:14 
 */
public interface CustomRunningTaskMapper {

    String querySql = "SELECT DISTINCT  \n" +
            "              res.*,arp.id_ AS processDefinitionId,arp.key_ AS processDefinitionKey,arp.name_ AS processDefinitionName, are.business_key_\n" +
            "            FROM act_ru_task res INNER JOIN act_re_procdef arp ON res.proc_def_id_ = arp.id_  INNER JOIN act_ru_execution are ON res.execution_id_=are.id_\n" +
            "            WHERE (  \n" +
            "             EXISTS(SELECT link.user_id_  \n" +
            "                          FROM act_ru_identitylink link  \n" +
            "                          WHERE (user_id_ = #{loginName} AND link.task_id_ = res.id_ AND type_='candidate')  \n" +
            "                           OR  (user_id_ = #{loginName} AND link.proc_inst_id_ = res.proc_inst_id_ AND type_='participant')  \n" +
            "                           OR (link.task_id_ = res.id_ AND type_='candidate' AND group_id_  IN(SELECT group_id FROM sys_user_group WHERE user_id=#{loginName}))  \n" +
            "                          )  \n" +
            "                    OR res.assignee_ = #{loginName}  \n" +
            "                    OR res.owner_ = #{loginName}  \n" +
            "                  )  \n" +
            "            ORDER BY res.create_time_ DESC";

    String countSql = "SELECT COUNT(DISTINCT res.id_)  \n" +
            "            FROM act_ru_task res INNER JOIN act_re_procdef arp ON res.proc_def_id_ = arp.id_  INNER JOIN act_ru_execution are ON res.execution_id_=are.id_\n" +
            "            WHERE (  \n" +
            "             EXISTS(SELECT link.user_id_  \n" +
            "                          FROM act_ru_identitylink link  \n" +
            "                          WHERE (user_id_ = 'zhangsan2' AND link.task_id_ = res.id_ AND type_='candidate')  \n" +
            "                           OR  (user_id_ = 'zhangsan2' AND link.proc_inst_id_ = res.proc_inst_id_ AND type_='participant')  \n" +
            "                           OR (link.task_id_ = res.id_ AND type_='candidate' AND group_id_  IN(SELECT group_id FROM sys_user_group WHERE user_id='zhangsan2'))  \n" +
            "                          )  \n" +
            "                    OR res.assignee_ = 'zhangsan2'  \n" +
            "                    OR res.owner_ = 'zhangsan2'  \n" +
            "                  )  \n" +
            "            ORDER BY res.create_time_ DESC";

    /**
     * 查询所有正在运行的任务
     *
     * @return
     */
    @Select("SELECT * FROM ( "+querySql+" ) tbl LIMIT #{pageindex},#{pagesize}")
    @Results(value = {
            @Result(id = true, property = "taskId", column = "id_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "taskKey", column = "task_def_key_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "taskName", column = "name_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "owner", column = "owner_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "assignee", column = "assignee_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "businessKey", column = "business_key_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "processDefinitionId", column = "processDefinitionId", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "processDefinitionKey", column = "processDefinitionKey", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "processDefinitionName", column = "processDefinitionName", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "processInstanceId", column = "proc_inst_id_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "executionId", column = "execution_id_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "parentTaskId", column = "parent_task_id_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "createTime", column = "create_time_", javaType = Date.class, jdbcType = JdbcType.DATE),
            @Result(property = "priority", column = "priority_", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "description", column = "description_", javaType = String.class, jdbcType = JdbcType.VARCHAR)
    })
    List<CustomRunningTask> getRunningTasks(@Param("loginName") String loginName, @Param("pageindex")int pageindex, @Param("pagesize")int pagesize);

    @Select(countSql)
    Integer countRunningTasks(@Param("loginName") String loginName);

}
