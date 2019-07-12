/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.query.model;

import com.simbest.cores.model.LogicModel;
import com.simbest.cores.utils.annotations.ProcessProperty;

import javax.persistence.*;

@MappedSuperclass
public class BusinessModel<T> extends LogicModel<T> {

    private static final long serialVersionUID = -8887330587069785067L;

    //所有业务单据的主键必须统一类型，否则无法写入待办和审批记录
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    protected String processDefinitionKey; //流程定义英文名称

    @Column(columnDefinition = "int default 0")
    protected Boolean iscg;

    protected String code;

    protected String title;
    
    private Integer demandOrgId;//需求提出部门
    
    private Integer demandUserId;//需求提出人

    @Transient
    private Integer createOrgId;

    @Transient
    private String createOrgName;

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

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
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
	
	
    
}
