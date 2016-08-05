/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2016。保留一切权利!
 */
package com.simbest.activiti.identity;

import com.simbest.cores.admin.authority.model.SysGroup;
import com.simbest.cores.admin.authority.model.SysUser;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 用途： 
 * 作者: lishuyi 
 * 时间: 2016-08-05  15:25 
 */
public class IdentityUtils {
    public static UserEntity toActivitiUser(SysUser sysUser){
        UserEntity userEntity = new UserEntity();
        userEntity.setId(sysUser.getLoginName());
        userEntity.setFirstName(sysUser.getUserCode());
        userEntity.setLastName(sysUser.getUsername());
        userEntity.setPassword(sysUser.getPassword());
        userEntity.setEmail(sysUser.getEmail());
        userEntity.setRevision(1);
        return userEntity;
    }

    public static GroupEntity toActivitiGroup(SysGroup sysGroup){
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setRevision(1);
        groupEntity.setType("assignment");
        groupEntity.setId(sysGroup.getId_());
        groupEntity.setName(sysGroup.getName_());
        return groupEntity;
    }

    public static List<Group> toActivitiGroups(List<SysGroup> sysGroups){
        List<org.activiti.engine.identity.Group> groupEntitys = new ArrayList<Group>();
        for (SysGroup sysGroup : sysGroups) {
            GroupEntity groupEntity = toActivitiGroup(sysGroup);
            groupEntitys.add(groupEntity);
        }
        return groupEntitys;
    }

    /**=================================================================================**/

    public static SysUser toSysUser(User user){
        SysUser sysUser = new SysUser();
        sysUser.setLoginName(user.getId());
        sysUser.setUserCode(user.getFirstName());
        sysUser.setUsername(user.getLastName());
        sysUser.setPassword(user.getPassword());
        sysUser.setEmail(user.getEmail());
        return sysUser;
    }

    public static SysGroup toSysGroup(Group group){
        SysGroup sysGroup = new SysGroup();
        sysGroup.setName_(group.getName());
        sysGroup.setType_(group.getType());
        sysGroup.setId_(group.getId());
        return sysGroup;
    }
}
