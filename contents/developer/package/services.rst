---
title: 服务访问
description: 访问其他的服务
---

================
服务访问
================

系统包括多个服务，可以使用当前用户身份去访问这些服务::

  client = root.get_client(applicaiton, instance)

其中 application 可以为:

- viewer: 云查看
- oc: 运营中心
- org: 账户组织结构

访问云查看
=================

得到云查看客户端::

  viewer = root.get_client('viewer', 'default')

得到云查看访问密匙::

  secret = viewer.get_secret()

得到云查看的访问策略等信息::

  secret = viewer.info()

设置云查看的访问策略为公开::

  secret = viewer.set_access_policy('public')

