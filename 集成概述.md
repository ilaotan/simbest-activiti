通过
```
<property name="dbIdentityUsed" value="false"/>
```
禁用Activiti默认的用户管理，即不使用文章[同步或者重构Activiti Identify用户数据的多种方案比较](http://www.kafeitu.me/activiti/2012/04/23/synchronize-or-redesign-user-and-role-for-activiti.html)所介绍方法的任意一种方案。原因如下：

方案一：
对现有系统的用户权限管理侵入性太强，强烈不建议使用，并且需要维护人员和组织信息，代价太大；

方案二：
自定义Activiti的GroupEntityManager和UserEntityManager也没有办法完全解决用户和组织的增删查改操作，详细可参考[Activiti需要用户信息吗？](http://www.chanjar.me/activiti/activiti-xu-yao-yong-hu-xin-xi-ma.html),也就是说查找用户任务时，Activiti始终还在使用自身内部的表。

方案三：
全视图方式如何做到CUD？

综上所述，直接禁用掉Activiti的ACT_ID_*所有表，并且通过自定义CustomSQL的方式查询用户任务更为有效！