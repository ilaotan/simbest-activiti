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
public interface MyTaskMapper {

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
    List<ActBusinessStatus> queryTaskCandidateOrAssigned(@Param("uniqueCode") String uniqueCode, @Param("pageindex") int pageindex, @Param("pagesize") int pagesize);

    @Select(taskCandidateOrAssignedSQLCountSQL)
    Integer countTaskCandidateOrAssigned(@Param("uniqueCode") String uniqueCode);
    /************************** 根据ACT_RU_TASK，查询我的待办 End *************************************************/



    /*******************************************根据ACT_RU_TASK、ACT_RU_IDENTITYLINK，查询我的参与 Start*************************************************/
    //SELECT DISTINCT RES.* FROM ACT_RU_TASK RES WHERE ( EXISTS(SELECT LINK.USER_ID_ FROM ACT_RU_IDENTITYLINK LINK WHERE USER_ID_ = ? AND LINK.TASK_ID_ = RES.ID_) OR RES.ASSIGNEE_ = ? OR RES.OWNER_ = ? ) ORDER BY RES.ID_ ASC LIMIT ? OFFSET ?
    /*******************************************根据ACT_RU_TASK、ACT_RU_IDENTITYLINK，查询我的参与 End*************************************************/



    /************************** 根据act_business_status，查询我的申请 Start *************************************************/
    @Select("SELECT * FROM ( SELECT * FROM act_business_status s WHERE s.createUserCode=#{uniqueCode} ORDER BY S.startTime DESC ) tbl LIMIT #{pageindex},#{pagesize}")
    List<ActBusinessStatus> queryMyApply(@Param("uniqueCode") String uniqueCode, @Param("pageindex") int pageindex, @Param("pagesize") int pagesize);

    @Select("SELECT COUNT(*) FROM act_business_status s WHERE s.createUserCode=#{uniqueCode}")
    Integer countMyApply(@Param("uniqueCode") String uniqueCode);
    /************************** 根据act_business_status，查询我的申请 End *************************************************/
}
