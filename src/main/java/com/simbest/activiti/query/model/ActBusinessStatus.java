/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.query.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.simbest.cores.model.GenericModel;
import com.simbest.cores.utils.annotations.ProcessProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;

import org.activiti.engine.task.DelegationState;

import javax.persistence.*;

import java.util.Date;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-11  21:45
 */
@Entity
@Table(name = "act_business_status")
public class ActBusinessStatus extends GenericModel<ActBusinessStatus> {

    private static final long serialVersionUID = -261318095183008548L;

    @Id
    @Column(name = "id")
    @SequenceGenerator(name="act_business_status_seq", sequenceName="act_business_status_seq")
    @GeneratedValue(strategy=GenerationType.AUTO, generator="act_business_status_seq")
    private Long id;
    
	@Column(name = "enabled", nullable = false, columnDefinition = "int default 1")
	@ProcessProperty
    @ApiModelProperty(value="是否可用")
	protected Boolean enabled;
	
	@Column(name = "removed", nullable = false, columnDefinition = "int default 0")
	@ProcessProperty
    @ApiModelProperty(value="是否逻辑删除")
	protected Boolean removed;

    @Column(columnDefinition = "int default 0")
    private Boolean iscg; //是否草稿

    private String code; //单据编码

    private String title; //单据标题

    private Long businessKey; //业务流程主键

    private String processDefinitionName; //流程定义中文名称

    private String processDefinitionKey; //流程定义英文名称

    private String processDefinitionId; //流程定义Id

    private String processInstanceId; //流程实例Id

    private String executionId; //流程执行Id

    private String taskId; //任务Id

    private String taskKey; //任务Key值

    private String taskName; //任务名称

    private String taskOwner; //任务拥有者

    private String taskAssignee; //任务办理人

//    private String candidateUsers;
//
//    private String candidateGroups;

    @Temporal(TemporalType.TIMESTAMP)
    private Date taskStartTime; //任务开始时间

    @Enumerated(EnumType.STRING)
    private DelegationState delegationState; //任务状态

    private Integer createUserId; //业务创建人Id

    private String createUserCode; //业务创建人编码

    private String createUserName; //业务创建人名称

    private Integer createOrgId;  //业务创建组织Id
    
    private Integer demandOrgId;//需求提出部门
    
    private Integer demandUserId;//需求提出人

    private String createOrgName; //业务创建组织名称
    
    private Long act_parentId; //主工单id

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime; //数据创建时间

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime; //数据更新时间

    private Integer previousAssignee; //SysUser主键Id

    private String previousAssigneeUniqueCode; //编码

    private String previousAssigneeName; //中文名称

    private Date previousAssigneeDate;//上一个处理时间

    private Integer previousAssigneeOrgId;

    private String previousAssigneeOrgName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime; //流程启动时间

    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime; //流程结束时间

    private Long duration; //流程持续时间

    private String startActivityId; //流程启动活动Id

    private String startActivityName; //流程启动活动名称

    private String endActivityId; //流程结束Id

    private String endActivityName; //流程结束名称

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

    public Long getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(Long businessKey) {
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

    public Integer getPreviousAssignee() {
        return previousAssignee;
    }

    public void setPreviousAssignee(Integer previousAssignee) {
        this.previousAssignee = previousAssignee;
    }

    public String getPreviousAssigneeUniqueCode() {
        return previousAssigneeUniqueCode;
    }

    public void setPreviousAssigneeUniqueCode(String previousAssigneeUniqueCode) {
        this.previousAssigneeUniqueCode = previousAssigneeUniqueCode;
    }

    public String getPreviousAssigneeName() {
        return previousAssigneeName;
    }

    public void setPreviousAssigneeName(String previousAssigneeName) {
        this.previousAssigneeName = previousAssigneeName;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getPreviousAssigneeDate() {
        return previousAssigneeDate;
    }

    public void setPreviousAssigneeDate(Date previousAssigneeDate) {
        this.previousAssigneeDate = previousAssigneeDate;
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

    public Integer getPreviousAssigneeOrgId() {
        return previousAssigneeOrgId;
    }

    public void setPreviousAssigneeOrgId(Integer previousAssigneeOrgId) {
        this.previousAssigneeOrgId = previousAssigneeOrgId;
    }

    public String getPreviousAssigneeOrgName() {
        return previousAssigneeOrgName;
    }

    public void setPreviousAssigneeOrgName(String previousAssigneeOrgName) {
        this.previousAssigneeOrgName = previousAssigneeOrgName;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public Integer getDemandOrgId() {
		return demandOrgId;
	}

	public void setDemandOrgId(Integer demandOrgId) {
		this.demandOrgId = demandOrgId;
	}

	public Integer getDemandUserId() {
		return demandUserId;
	}

	public void setDemandUserId(Integer demandUserId) {
		this.demandUserId = demandUserId;
	}

	public Long getAct_parentId() {
		return act_parentId;
	}

	public void setAct_parentId(Long act_parentId) {
		this.act_parentId = act_parentId;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getRemoved() {
		return removed;
	}

	public void setRemoved(Boolean removed) {
		this.removed = removed;
	}
	
	
    
}
