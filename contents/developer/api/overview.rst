---
title: 开放API概览
description: 总体说明系统的开放API架构
---

======================
开放API概览
======================

系统的开放API，建立在http协议上，依托在oauth2.0协议之上的，返回结果的形式json。

请求的基本形式是::

  http://service-domain-name/api/v1/module_name/api_name?arg1=value1&arg2=value2

服务地址
====================
系统服务组成包括:

- 账户管理: account
- 组织架构: org
- 云查看: viewer
- 工作平台: workonline

其中账户管理account是有唯一的服务API入口地址，其他服务的API入口地址，需要通过account提供的接口来查询::

  http://app.easydo.cn/api/v1/account/get_api_address?account=zopen&app=workonline&instance=default

返回值和错误信息
=========================

模块清单
=====================
账户管理：

- account：实例管理、续费、服务地址查询、认证配置

