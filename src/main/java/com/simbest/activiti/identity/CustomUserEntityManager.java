/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.identity;

import com.simbest.cores.admin.authority.model.SysGroup;
import com.simbest.cores.admin.authority.model.SysUser;
import com.simbest.cores.admin.authority.service.ISysUserAdvanceService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2016-08-05  13:47
 */
//@Service
public class CustomUserEntityManager extends UserEntityManager {

    @Autowired
    private ISysUserAdvanceService sysUserAdvanceService;

    @Override
    public UserEntity findUserById(final String loginName) {
        if (loginName == null)
            return null;
        try {
            SysUser sysUser = sysUserAdvanceService.getByUnique(loginName);
            return IdentityUtils.toActivitiUser(sysUser);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Group> findGroupsByUser(final String loginName) {
        if (loginName == null)
            return null;
        List<SysGroup> groupList = sysUserAdvanceService.getByUnique(loginName).getGroupList();
        return IdentityUtils.toActivitiGroups(groupList);

    }

    public void insertUser(User user) {
        throw new RuntimeException("Not implement method!");
    }

    public void updateUser(User user) {
        SysUser sysUser = IdentityUtils.toSysUser(user);
        sysUserAdvanceService.updateViaAdmin(sysUser);
    }

    public void deleteUser(String loginName) {
        SysUser sysUser = sysUserAdvanceService.loadByUnique(loginName);
        sysUserAdvanceService.delete(sysUser.getId());
    }


}
