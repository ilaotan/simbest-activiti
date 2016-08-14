/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.query.model;

import com.simbest.cores.model.LogicModel;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BusinessModel<T> extends LogicModel<T> {

    private static final long serialVersionUID = -8887330587069785067L;

    @Column(columnDefinition = "TINYINT default 0")
    private Boolean iscg;

    private String code;

    private String title;

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
}
