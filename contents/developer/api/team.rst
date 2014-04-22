---
title: 团队管理
description: 工作平台提供团队管理的API
---

===============
团队管理
===============

团队是位于工作平台某个位置的临时组。

list_my_teams
---------------------------
输入:

- pid

到人员所在的组id::

 [team1_id, team2_id]

/is_obj_teamable
------------------------
对象是否支持团队管理

/set_obj_teamable
------------------------
设置对象是否支持团队管理

- enabled

/list_obj_teams
--------------------
输入：

- obj_id:
- obj_path: 和obj_id二选一

得到所有的team信息::

  [{id, title, description, members}]

new_obj_team
--------------------
新建一个team

- obj_id:
- obj_path: 和obj_id二选一
- team_id
- title
- description
- pids

remove_obj_teams
-----------------------
删除一个团队

- obj_id:
- obj_path: 和obj_id二选一
- team_ids

/get_obj_team
-----------------
输入： 

- obj_id:
- obj_path: 和obj_id二选一
- team_id

得到一个团队的信息::

   {id, title, description, members}

update_obj_team
------------------------
更改team的标题描述

输入:

- obj_id:
- obj_path: 和obj_id二选一
- team_id
- title
- description

append_obj_team_members
---------------------------
添加一批组员

输入:

- obj_id:
- obj_path: 和obj_id二选一
- team_id
- pids

remove_obj_team_members
----------------------------
删除一批组员

输入:

- obj_id:
- obj_path: 和obj_id二选一
- team_id
- pid

set_obj_team_members
-----------------------------
设置一批组员

输入:

- obj_id:
- obj_path: 和obj_id二选一
- team_id
- pids

