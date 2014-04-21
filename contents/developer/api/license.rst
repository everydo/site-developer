---
title: 用户许可管理
description: 管理用户的许可
---

============
许可管理
============

许可策略
===================

/set_license_policy
----------------------------

- instance
- policy

设置某个实例的许可策略：

- 手工控制: 没有分配许可的人，不得加入
- 自由加入: 初次访问可以自由加入，直至许可满员。这个策略可以减少许可管理的复杂度

/get_instance_policy
-------------------------------
得到示例的许可策略


分配许可
==============

/list_allowed_instances
-----------------------------
得到分配的许可站点(oc的管理员可以查看所有的，每个人只能查看自己的)：

- account
- username

返回::

   [
     {  account:,
        application:,
        instance:
        role: []
   ]

如果是自由许可，而且购买无限制版本，则所有用户拥有该实例的许可

/set_allowed_instances
-----------------------------
- account
- app_name
- instance_name
- pids
- roles:   [role_id, ...],

/request_instance_licence
-----------------------------
如果站点设置自由加入，而且许可未满，则可以获得许可

- account
- app_name
- instance_name
- pids
- roles:   [role_id, ...],

