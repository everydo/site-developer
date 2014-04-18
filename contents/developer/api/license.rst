---
title: 用户许可管理
description: 管理用户的许可
---

============
许可管理
============

/list_instances
-----------------------
得到全部的站点

- account

/set_license_policy
----------------------------
设置某个实例的许可策略：

- 手工控制: 没有分配许可的人，不得加入
- 自由加入: 初次访问可以自由加入，直至许可满员。这个策略可以减少许可管理的复杂度

/set_allowed_services
-----------------------------
- pid
- app_name
- instance_name
- services
- account

/get_allowed_services
-----------------------------
- account
- username
- app_name
- instance_name
- services:['docs', 'project']
- 'roles':[role_id, ...],
