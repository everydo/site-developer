.. Contents::
.. sectnum::

团队管理
=================


所谓团队,是非系统管理员管理的临时组，包括项目组、工作组、部门等. 

- 团队只能分2级

  - 总团队，比如一个项目的所有组员 groups.team.[intid]
  - 团队下面的一个子组，比如设计组：groups.team.[intid]-[team_name]
  - 一个人如果属于一个团队，将同时属于上述2个组

- 一旦项目、部门结束，团队解散：

  - 登录用户的reqeust.principal.groups中, 不再包含这些解散组队的信息
  - 针对这些组的授权也同时失效

ITeamManager(context):
---------------------------------

- listTeams(): 返回 [{id, title, description, members}]
- getTeam(id): 得到一个团队的信息 {id, title, description, members}
- getMemberTeams(member): 到人员所在的组id
- setTeamTitle(id, title, description): 设置组员标题和描述
- appendTeamMember(id, member): 添加一个组员
- removeTeamMember(id, member): 删除一个组员
- setTeamMembers(id, members): 设置组员
- addTeam(id, title, description, members): 添加一个团队
- removeTeam(id):删除一个团队
