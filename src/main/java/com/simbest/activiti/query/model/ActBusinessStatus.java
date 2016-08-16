/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.query.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.simbest.cores.model.GenericModel;
import org.activiti.engine.task.DelegationState;

import javax.persistence.*;
import java.util.Date;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-11  21:45
 */
@Entity
@Table(name = "act_business_status", uniqueConstraints={@UniqueConstraint(columnNames={"processDefinitionId", "processInstanceId"})})
public class ActBusinessStatus extends GenericModel<ActBusinessStatus> {

    private static final long serialVersionUID = -261318095183008548L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "TINYINT default 0")
    private Boolean iscg;

    private String code;

    private String title;

    private String businessKey; //业务流程主键

    private String processDefinitionName; //流程定义中文名称

    private String processDefinitionKey; //流程定义英文名称

    private String processDefinitionId; //流程定义Id

    private String processInstanceId;

    private String executionId;

    private String taskId;

    private String taskKey;

    private String taskName;

    private String taskOwner;

    private String taskAssignee;

//    private String candidateUsers;
//
//    private String candidateGroups;

    @Temporal(TemporalType.TIMESTAMP)
    private Date taskStartTime;

    @Enumerated(EnumType.STRING)
    private DelegationState delegationState;

    private Integer createUserId;

    private String createUserCode;

    private String createUserName;

    private Integer createOrgId;

    private String createOrgName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    private Long duration;

    private String startActivityId;

    private String startActivityName;

    private String endActivityId;

    private String endActivityName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIscg() {
        return iscg;
    }

    public void setIscg(Boolean iscg) {
        this.iscg = iscg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskOwner() {
        return taskOwner;
    }

    public void setTaskOwner(String taskOwner) {
        this.taskOwner = taskOwner;
    }

    public String getTaskAssignee() {
        return taskAssignee;
    }

    public void setTaskAssignee(String taskAssignee) {
        this.taskAssignee = taskAssignee;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserCode() {
        return createUserCode;
    }

    public void setCreateUserCode(String createUserCode) {
        this.createUserCode = createUserCode;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(Date taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public Integer getCreateOrgId() {
        return createOrgId;
    }

    public void setCreateOrgId(Integer createOrgId) {
        this.createOrgId = createOrgId;
    }

    public String getCreateOrgName() {
        return createOrgName;
    }

    public void setCreateOrgName(String createOrgName) {
        this.createOrgName = createOrgName;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getStartActivityId() {
        return startActivityId;
    }

    public void setStartActivityId(String startActivityId) {
        this.startActivityId = startActivityId;
    }

    public String getStartActivityName() {
        return startActivityName;
    }

    public void setStartActivityName(String startActivityName) {
        this.startActivityName = startActivityName;
    }

    public String getEndActivityId() {
        return endActivityId;
    }

    public void setEndActivityId(String endActivityId) {
        this.endActivityId = endActivityId;
    }

    public String getEndActivityName() {
        return endActivityName;
    }

    public void setEndActivityName(String endActivityName) {
        this.endActivityName = endActivityName;
    }

    public DelegationState getDelegationState() {
        return delegationState;
    }

    public void setDelegationState(DelegationState delegationState) {
        this.delegationState = delegationState;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }
}
