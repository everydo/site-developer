---
title: 团队管理
description: 团队是类似项目、工作组等临时组成的团队
---

==============
团队管理
==============

.. contents::
.. sectnum::

所谓团队,是非系统管理员管理的临时组，包括项目组、工作组、部门等. 

- 团队只能分2级

  - 总团队，比如一个项目的所有组员 groups.team.[intid]
  - 团队下面的一个子组，比如设计组：groups.team.[intid]-[team_name]
  - 一个人如果属于一个团队，将同时属于上述2个组

- 一旦项目、部门结束，团队解散：

  - 登录用户的reqeust.principal.groups中, 不再包含这些解散组队的信息
  - 针对这些组的授权也同时失效

ITeams(context):
==========================

- list_teams(): 返回 [{id, title, description, members}]
- get_team(id): 得到一个团队的信息 {id, title, description, members}
- get_member_teams(member): 到人员所在的组id
- update_team(id, title, description): 设置组员标题和描述
- append_team_member(id, member): 添加一个组员
- remove_team_member(id, member): 删除一个组员
- set_team_members(id, members): 设置组员
- add_team(id, title, description, members): 添加一个团队
- remove_team(id):删除一个团队

