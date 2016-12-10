package com.simbest.activiti.listener.schedule.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.simbest.cores.model.GenericModel;
import com.simbest.cores.utils.annotations.NotNullColumn;

import javax.persistence.*;
import java.util.Date;

/**
 * 业务代办回调任务
 * 
 * @author lishuyi
 *
 */
@Entity
@Table(name = "act_task_callback_retry")
public class TaskCallbackRetry extends GenericModel<TaskCallbackRetry>{
    private static final long serialVersionUID = -7007861362041015756L;

    @Id
    @Column(name = "id")
    @SequenceGenerator(name="act_task_callback_retry_seq", sequenceName="act_task_callback_retry_seq")
    @GeneratedValue(strategy=GenerationType.AUTO, generator="act_task_callback_retry_seq")
    private Long id;

    @Column(name = "taskJobClass", nullable = false)
	private String taskJobClass;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastExecuteDate", nullable = false)
    private Date lastExecuteDate;

    @Column(name = "executeTimes", nullable = false)
    private Integer executeTimes;

    @Column(name = "callbackType", nullable = false)
    private String callbackType;

    private Long actBusinessStatusId;

    private String uniqueCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getTaskJobClass() {
        return taskJobClass;
    }

    public void setTaskJobClass(String taskJobClass) {
        this.taskJobClass = taskJobClass;
    }

    public Long getActBusinessStatusId() {
        return actBusinessStatusId;
    }

    public void setActBusinessStatusId(Long actBusinessStatusId) {
        this.actBusinessStatusId = actBusinessStatusId;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    public Date getLastExecuteDate() {
        return lastExecuteDate;
    }

    public void setLastExecuteDate(Date lastExecuteDate) {
        this.lastExecuteDate = lastExecuteDate;
    }

    public Integer getExecuteTimes() {
        return executeTimes;
    }

    public void setExecuteTimes(Integer executeTimes) {
        this.executeTimes = executeTimes;
    }

    public String getCallbackType() {
        return callbackType;
    }

    public void setCallbackType(String callbackType) {
        this.callbackType = callbackType;
    }
}