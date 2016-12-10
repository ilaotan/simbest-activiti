package com.simbest.activiti.listener.schedule.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.simbest.cores.app.model.ProcessTaskCallbackLog;
import com.simbest.cores.model.GenericModel;

import javax.persistence.*;
import java.util.Date;

/**
 * 业务代办回调日志记录
 * 
 * @author lishuyi
 *
 */
@Entity
@Table(name = "act_task_callback_log")
public class TaskCallbackLog extends GenericModel<TaskCallbackLog> {
    private static final long serialVersionUID = -1467229183614614810L;

    @Id
    @Column(name = "id")
    @SequenceGenerator(name="act_task_callback_log_seq", sequenceName="act_task_callback_log_seq")
    @GeneratedValue(strategy=GenerationType.AUTO, generator="act_task_callback_log_seq")
    private Long id;

    private Long actBusinessStatusId;

    @Column(name = "callbackType", nullable = false)
	private String callbackType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "callbackStartDate", nullable = false)
	private Date callbackStartDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "callbackEndDate", nullable = false)
    private Date callbackEndDate;

    @Column(name = "callbackDuration", nullable = false)
    private Long callbackDuration;

    @Column(name = "callbackResult", nullable = false, columnDefinition = "int default 1")
    protected Boolean callbackResult;

    @Column(name = "callbackError", nullable = true, length = 2000)
    private String callbackError;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActBusinessStatusId() {
        return actBusinessStatusId;
    }

    public void setActBusinessStatusId(Long actBusinessStatusId) {
        this.actBusinessStatusId = actBusinessStatusId;
    }

    public String getCallbackType() {
        return callbackType;
    }

    public void setCallbackType(String callbackType) {
        this.callbackType = callbackType;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    public Date getCallbackStartDate() {
        return callbackStartDate;
    }

    public void setCallbackStartDate(Date callbackStartDate) {
        this.callbackStartDate = callbackStartDate;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    public Date getCallbackEndDate() {
        return callbackEndDate;
    }

    public void setCallbackEndDate(Date callbackEndDate) {
        this.callbackEndDate = callbackEndDate;
    }

    public Long getCallbackDuration() {
        return callbackDuration;
    }

    public void setCallbackDuration(Long callbackDuration) {
        this.callbackDuration = callbackDuration;
    }

    public Boolean getCallbackResult() {
        return callbackResult;
    }

    public void setCallbackResult(Boolean callbackResult) {
        this.callbackResult = callbackResult;
    }

    public String getCallbackError() {
        return callbackError;
    }

    public void setCallbackError(String callbackError) {
        this.callbackError = callbackError;
    }
}