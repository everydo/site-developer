---
title: 认证模块
description: 易度自带认证模块，也可以通过CAS单点登陆，或者简单单点登录，或LDAP认证
---

====================
认证模块
====================

系统提供多种认证源，在登陆窗口，用户可以任意选择:

- 内置的直接
- 单点登录


直接认证集成
====================
直接认证:

- 提供基于用户名、密码的认证
- 支持动态密码
- 用户认证信息可和LDAP集成，如果和LDAP集成，可以采用LDAP密码登陆

/api/v1/auth/set_ldap_config
------------------------------------
设置LDAP认证服务器信息

- account
- server: ldap服务器，形式为 ``address:port``
- enabled : 激活与否

/api/v1/auth/get_ldap_config
----------------------------------
- account

返回::

  {server: , enabled: }

/api/v1/auth/check_password
----------------------------------
检查内置密码

参数：
- pid
- password

返回::
  
  {'pid':'users.test',
   'status':True}

/api/v1/auth/reset_password
----------------------------------
重置密码

参数：

- password
- new_password

返回::

  {'pid':'users.test',
   'status': True}

/api/v1/auth/enable_dynamic_auth
--------------------------------------
开启动态认证

参数：
- secret_key
- code

返回::

   {'pid':'users.test', 
     'status':True}

/api/v1/disable_dynamic_auth
----------------------------------
关闭动态认证

参数：

- code

返回::

   {'pid':'users.test', 
     'status':True}

/api/v1/is_dynamic_auth_enabled
---------------------------------------
检查是否开启动态认证

返回::

   {'pid':'users.test', 
     'status':True}

单点登陆认证
===================
单点登录认证支持CAS、SAML和签名URL几种方式。

如果现有系统不支持LDAP直接登录认证，可通过编写单点登录适配模块来实现集成，如CAS/SAML。

CAS认证
------------
CAS激活/禁用
..................
TODO

CAS参数设置
.................
TODO

签名URL认证
----------------------
签名认证可以通过对访问系统的URL进行签名，来实现免登录认证。

可在账户管理设置中，设置应用授权密码和超时失效时间。

.. image:: img/simple-sso3.png

也可以通过API来设置::

  TODO

通过一个带签名信息的URL链接来实现的登录，URL地址为::

 http://hoeron.easydo.cn/simple_sso/@@@zopen.simple_sso.sso?username=tt1024&
  sign=8509c9fb03492a6afea62c4820523b97×tamp=1268901715&
  came_from=http://OA_SERVER/xxxxx

url参数含义:

a) @@@zopen.simple_sso.sso是单点登录入口
b) username是登录的用户名
c) sign是签名信息: md5签名信息，如果签名验证通过，就认证成功
d) timestamp是时间戳:签名的时间，签名仅仅在生成之后一段时间范围内有效
e) came_from是登录后跳转的URL: 认证成功后调整的目标URL，对于API访问，这个字段可忽略
  
- 登录签名生成的算法是： MD5(填写的用户名+应用授权密码+时间戳)
- 如果应用授权密码为123456，sign=MD5('tt10241234561268901715')

验证原理:

a) 应用授权密码，用于双方系统之间的验证，由系统两方协定，并不是用户的登录密码。
   点击右上 “应用设置”，可以设置 授权密码和超时失效时间；

b) 双放系统的用户名相同 ，可将用户导入即可
c) 如果是C/S,返回sessonid.
d) 关于安全性方面，由于验证签名是经过md5摘要的，比较难逆向出原文，并
   且添加了时间戳作为生成sign验证码条件，使得每次生成的验证码均不同，
   无法伪造登录信息。

SAML认证
--------------
TODO

