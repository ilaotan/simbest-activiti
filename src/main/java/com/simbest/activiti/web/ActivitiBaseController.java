/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.web;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.simbest.cores.utils.configs.CoreConfig;
import com.simbest.cores.utils.pages.PageSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-05  9:07
 */
public class ActivitiBaseController {
    @Autowired
    protected CoreConfig config;

    protected Map<String, Object> wrapQueryResult(List<?> list) {
        Map<String, Object> dataMap = Maps.newHashMap();
        PageInfo info = new PageInfo(list);
        if (config.getValue("js.framework").equals("zjs")) {
            //前端没有传递pageindex和pagesize参数，GenericMapperService和GenericSQLService没有按照分页模式查询，因此info.getList()返回空值
            if (info.getList() == null) {
                dataMap.put("TotalPages", ((int) list.size() / 20) + 1);
                dataMap.put("TotalRows", list.size());
                dataMap.put("Datas", list);
            } else {
                dataMap.put("TotalPages", info.getPages());
                dataMap.put("TotalRows", info.getTotal());
                dataMap.put("Datas", info.getList());
            }
        }
        return dataMap;
    }

    protected Map<String, Object> wrapQueryResult(PageSupport<?> ps) {
        Map<String, Object> dataMap = Maps.newHashMap();
        if (config.getValue("js.framework").equals("zjs")) {
            dataMap.put("TotalPages", ps.getTotalPages());
            dataMap.put("TotalRows", ps.getTotalRecords());
            dataMap.put("Datas", ps.getItems());
        }
        return dataMap;
    }
}
