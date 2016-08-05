/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.identity;

import com.simbest.cores.admin.authority.model.SysGroup;
import com.simbest.cores.admin.authority.service.ISysGroupAdvanceService;
import com.simbest.cores.admin.authority.service.ISysUserAdvanceService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-05  13:50
 */
//@Service
public class CustomGroupEntityManager extends GroupEntityManager {

    @Autowired
    private ISysUserAdvanceService sysUserAdvanceService;

    @Autowired
    private ISysGroupAdvanceService sysGroupAdvanceService;

    @Override
    public void insertGroup(Group group) {
        SysGroup sysGroup = new SysGroup();
        sysGroup.setId_(group.getId());
        sysGroup.setName_(group.getName());
        sysGroup.setType_(group.getType());
        sysGroup.setRev_(1);
        sysGroupAdvanceService.create(sysGroup);
    }

    @Override
    public void updateGroup(Group updatedGroup) {
        sysGroupAdvanceService.update(IdentityUtils.toSysGroup(updatedGroup));
    }

    @Override
    public void deleteGroup(String groupId) {
        sysGroupAdvanceService.delete(groupId);
    }

    @Override
    public List<Group> findGroupsByUser(final String loginName) {
        if (loginName == null)
            return null;
        List<SysGroup> groupList = sysUserAdvanceService.getByUnique(loginName).getGroupList();

        List<Group> gs = new ArrayList<Group>();
        GroupEntity g;
        for (SysGroup bGroup : groupList) {
            g = new GroupEntity();
            g.setRevision(1);
            g.setType("assignment");

            g.setId(bGroup.getId_());
            g.setName(bGroup.getName_());
            gs.add(g);
        }
        return gs;
    }
}
