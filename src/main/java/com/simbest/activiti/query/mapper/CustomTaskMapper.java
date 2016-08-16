/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.query.mapper;

import com.simbest.activiti.query.model.ActBusinessStatus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-06  11:14
 */
public interface CustomTaskMapper {

    /************************** 根据ACT_RU_TASK，查询我的待办 Start *************************************************/
    String taskCandidateOrAssignedSQL = "SELECT DISTINCT\n" +
            "              S.*\n" +
            "            FROM ACT_RU_TASK RES INNER JOIN  act_business_status S ON res.ID_=s.taskId AND res.PROC_INST_ID_=s.processInstanceId\n" +
            "              LEFT JOIN ACT_RU_IDENTITYLINK I\n" +
            "                ON I.TASK_ID_ = RES.ID_\n" +
            "            WHERE (RES.ASSIGNEE_ = #{uniqueCode}\n" +
            "                    OR (RES.ASSIGNEE_ IS NULL\n" +
            "                        AND (I.USER_ID_ = #{uniqueCode}\n" +
            "                              OR I.GROUP_ID_ IN(SELECT\n" +
            "                                                  g.GROUP_ID\n" +
            "                                                FROM sys_user_group g\n" +
            "                                                WHERE g.USER_ID = #{uniqueCode})))) ORDER BY S.taskStartTime DESC ";
    String taskCandidateOrAssignedSQLCountSQL = "SELECT COUNT(DISTINCT S.id) " +
            "            FROM ACT_RU_TASK RES INNER JOIN  act_business_status S ON res.ID_=s.taskId AND res.PROC_INST_ID_=s.processInstanceId\n" +
            "              LEFT JOIN ACT_RU_IDENTITYLINK I\n" +
            "                ON I.TASK_ID_ = RES.ID_\n" +
            "            WHERE (RES.ASSIGNEE_ = #{uniqueCode}\n" +
            "                    OR (RES.ASSIGNEE_ IS NULL\n" +
            "                        AND (I.USER_ID_ = #{uniqueCode}\n" +
            "                              OR I.GROUP_ID_ IN(SELECT\n" +
            "                                                  g.GROUP_ID\n" +
            "                                                FROM sys_user_group g\n" +
            "                                                WHERE g.USER_ID = #{uniqueCode}))))";

    @Select("SELECT * FROM ( " + taskCandidateOrAssignedSQL + " ) tbl LIMIT #{pageindex},#{pagesize}")
    List<ActBusinessStatus> queryMyTask(@Param("uniqueCode") String uniqueCode, @Param("pageindex") int pageindex, @Param("pagesize") int pagesize);
    @Select(taskCandidateOrAssignedSQLCountSQL)
    Integer countMyTask(@Param("uniqueCode") String uniqueCode);
    /************************** 根据ACT_RU_TASK，查询我的待办 End *************************************************/


    /************************** 根据act_business_status，查询我的申请 Start *************************************************/
    @Select("SELECT * FROM ( SELECT * FROM act_business_status s WHERE s.createUserCode=#{uniqueCode} ORDER BY S.startTime DESC ) tbl LIMIT #{pageindex},#{pagesize}")
    List<ActBusinessStatus> queryMyApply(@Param("uniqueCode") String uniqueCode, @Param("pageindex") int pageindex, @Param("pagesize") int pagesize);
    @Select("SELECT COUNT(*) FROM act_business_status s WHERE s.createUserCode=#{uniqueCode}")
    Integer countMyApply(@Param("uniqueCode") String uniqueCode);
    /************************** 根据act_business_status，查询我的申请 End *************************************************/


    /************************** 根据act_business_status、act_task_assigne，查询我的已办 Start *************************************************/
    @Select("SELECT * FROM ( SELECT DISTINCT s.* FROM act_business_status s,act_task_assigne a WHERE s.processDefinitionId=a.processDefinitionId AND s.processInstanceId =a.processInstanceId AND (a.owner=#{uniqueCode} OR a.assignee=#{uniqueCode}) ORDER BY s.startTime DESC ) tbl LIMIT #{pageindex},#{pagesize}")
    List<ActBusinessStatus> queryMyJoin(@Param("uniqueCode") String uniqueCode, @Param("pageindex") int pageindex, @Param("pagesize") int pagesize);
    @Select("SELECT COUNT(DISTINCT s.id) FROM act_business_status s,act_task_assigne a WHERE s.processDefinitionId=a.processDefinitionId AND s.processInstanceId =a.processInstanceId AND (a.owner=#{uniqueCode} OR a.assignee=#{uniqueCode})")
    Integer countMyJoin(@Param("uniqueCode") String uniqueCode);
    /************************** 根据act_business_status、act_task_assigne，查询我的已办 Start *************************************************/

}
