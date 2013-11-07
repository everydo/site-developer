---
title: 简单单点登录
description: 利用易度的扩展软件包，实现和外部系统基于时间戳和密匙的简单单点登录
---

================
简单单点登录接口
================
.. contents:: 内容
.. sectnum::



以系统管理登录系统，进入应用市场，点击安装这个应用
--------------------------------------------------

.. image:: img/simple-sso1.PNG

- 然后部署这个软件包

.. image:: img/simple-sso2.png

- 部署完成，进入单点登录界面，会出现“密匙错误”，可在应用设置中设置下应用授权密码和超时失效时间。

.. image:: img/simple-sso3.png

URL接口
-------
- 通过一个带签名信息的URL链接来实现的登录，URL地址为:

-http://hoeron.easydo.cn/simple_sso/@@@zopen.simple_sso.sso?username=tt1024&sign=8509c9fb03492a6afea62c4820523b97×tamp=1268901715&came_from=http://OA_SERVER/xxxxx

url参数含义
------------
  a) @@@zopen.simple_sso.sso是单点登录入口
  b) username是登录的用户名
  c) sign是签名信息: md5签名信息，如果签名验证通过，就认证成功
  d) timestamp是时间戳:签名的时间，签名仅仅在生成之后一段时间范围内有效
  e) came_from是登录后跳转的URL: 认证成功后调整的目标URL，对于API访问，这个
