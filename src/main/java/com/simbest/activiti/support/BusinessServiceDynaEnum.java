package com.simbest.activiti.support;

import com.simbest.cores.utils.enums.PropertiesDynaEnum;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BusinessServiceDynaEnum extends PropertiesDynaEnum {

	@PostConstruct
	public void init() {
		init(BusinessServiceDynaEnum.class);
	}

	public BusinessServiceDynaEnum() {
		super();
	}

    public BusinessServiceDynaEnum(String name, String meaning, int ordinal) {
		super(name, meaning, ordinal);
	}
}
