package com.simbest.activiti.query.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.simbest.cores.model.GenericModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "act_task_assigne")
public class ActTaskAssigne extends GenericModel<ActTaskAssigne> {

    private static final long serialVersionUID = -7956922435328251210L;

    @Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "processDefinitionId", nullable = true, length=255)
	private String processDefinitionId;//流程定义Id

	@Column(name = "processInstanceId", nullable = true, length=255)
	private String processInstanceId;//流程实例Id

	@Column(name = "executionId", nullable = true, length=255)
	private String executionId;//流程执行Id

	@Column(name = "taskId", nullable = true, length=255)
	private String taskId;//任务Id

	@Column(name = "owner", nullable = true, length=255)
	private String owner;//任务所属人

	@Column(name = "assignee", nullable = true, length=255)
	private String assignee;//任务办理人

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "handleTime", nullable = true)
	private Date handleTime;//办理时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }
}