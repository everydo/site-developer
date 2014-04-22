---
title: 用户许可管理
description: 管理用户的许可
---

============
账户管理
============

账户管理包括：

/api/v1/account
==========================

  http://app.easydo.cn/api/v1/account/get_api_address?account=zopen&app=workonline&instance=default

实例管理
===================

/api/v1/instance/create_instance
-------------------------------------
创建站点

/api/v1/instance/list_instances
-------------------------------------
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


