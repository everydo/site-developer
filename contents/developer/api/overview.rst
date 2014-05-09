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
- 工作平台: 应用名字为 workonline
- 文件服务: 应用名字为 file(viewer), 包括上传、下载、转换等
- 短信发送：应用名字为 sms

其中账户管理、组织架构有唯一的服务API入口地址，其他服务的API入口地址，需要通过account提供的接口来查询::

  http://app.easydo.cn/api/v1/account/get_api_address?account=zopen&app=workonline&instance=default

返回值和错误信息
=========================

模块清单
=====================
账户管理：

- account：实例管理、续费、服务地址查询、认证配置




