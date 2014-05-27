---
title: 团队管理
description: 团队是类似项目、工作组等临时组成的团队
---

==============
团队管理
==============

.. contents::
.. sectnum::

团队和容器
================
所谓团队,是非账户管理员管理的组，他和系统的某个应用容器直接关联。

典型的团队包括：

- 站点人员: 系统自带的一个team，根据购买的许可限制站点人数；可设置策略，自动加入
- 项目团队: 项目的临时团队
- 工作组: 一个协作区域
- 部门: 部门工作区域

使用 ``appcontainer.teams`` 来访问团队的api。

激活应用容器的团队功能
==========================
默认应用容器的团队功能是关闭的。可以激活:: 

   appcontainer.set_state('team.active')

可以查看容器的团队功能是否激活::

   'team.active' in appcontainer.stati

团队关联的容器有如下状态::

- team.active : 活动
- team.onhold : 暂停，这种情况下团队也是没有关闭的
- team.closed ：关闭

一旦项目、工作组结束，团队自动解散, 可以再次关闭团队::

   appcontainer.set_state('team.close')

这时候针对团队的授权自动失效。

团队和子组
=================
团队包括多个子组，可以选择 某个子组，或者选择整个团队：

- 整个团队，比如一个项目的所有组员 
- 团队下面的一个子组，比如设计组：

查看team所有的组::

   app_container.teams.list_groups()

返回::

   [{id, title, description, white_list, black_list}]

也可以得到一个组的信息::

    app_container.teams.get_group(name): 

同样返回::

    {id, title, description, white_list, black_list}

team组管理其他的接口还包括：

- add_group(name, title, description, white_list, black_list): 添加一个团队
- update_group(name, title, description, white_list, black_list): 设置组员标题和描述
- remove_group(name): 删除一个团队

团队人员限制
===================
team可能有人员限制，比如根节点的人员限制，就是站点购买的许可数；

不同级别，允许的team人数也是不同的, 查看 人员人数限制::

  appcontainer.teams.get_member_limit():

得到team去重复之后的人员总人数::

  get_members_count()

组成员管理接口

- list_member_groups(member): 到人员所在的组pid
- list_group_members(name): 得到组包括的人员pid
- add_team_members(name, members): 添加多个组员
- remove_group_members(name, members): 删除多个组员
- set_group_members(name, members): 设置组员

人员自动加入
===================
当team人数多的时候，维护team人员非常繁琐。因此可以设置自动加入策略，满足条件的用户，自动加入到团队的某个组中。

将某个人加入子组，也同时加入了团队

每个组通过white_list和black_list是组的自动人员添加策略:

- white_list: 白名单，允许自动加入的组的清单, 比如允许公司所有人员加入::

   ['groups.tree.default']

- black_list: 黑名单，禁止自动加入的组或者人员的清单, 比如禁止销售部门人员和张三进入::

   ['groups.tree.sales', 'users.zhangsan']

根据策略定义自动加入到组::

   appcontainer.teams.join(pid)

策略是：检查所有的组，如果pid属于某个组的黑名单，则禁止；如果属于白名单，则自动加入；

返回：加入的组的pid列表, 比如加入战点的users组::

   ['groups.team.0', 'groups.team.0-users']

如果人员满或者没有匹配的组，则抛出异常. 

也可以对某个组，根据白名单、黑名单，自动加入所有人员, 直至许可用尽::

   appcontainer.teams.join_all(group_name): 

team和组的标识ID
=================
- get_team_pid()

  内部标识pid为： groups.team.[uid], uid是团队关联容器的uid

  对于站点人员，uid固定为0. 因此判断某个人是否有站点许可::

      'groups.team.0' in request.principal.groups

- get_group_pid(name): 得到组的pid::

    groups.team.[uid]-[team_name]

