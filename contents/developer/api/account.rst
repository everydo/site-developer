---
title: 账户管理
description: 应用的创建、服务级别切换、缴费、管理员设置等
---

============
账户管理
============

账户管理包括应用的创建、服务级别切换、缴费、管理员设置等。

/api/v1/account/get_user_roles
=========================================
得到用户在账户管理中的角色:

输入：

- account
- user_id

返回::
   
    {'account' :
     'user_id' :
     'roles'   : ['AccountAdmin'], }

这里的roles，是用户在账户管理中的全局组, 比如 ``AccountAdmin`` 就是账户管理员.

实例管理
===================

/api/v1/account/list_instances
-------------------------------------
得到账户所有的应用实例:

输入：

- account

返回各个应用的实例信息::

   [
     { application:,[{ id:, 
                       title:,
                       app_url:, 
                       api_url:,},  
                    ]},
   ]

/api/v1/account/get_instance_url
--------------------------------------
得到某个应用实例的api访问入口

输入::

- account
- app
- instance

输出::

   {'app_url':,
    'api_url':
   }

切换运营参数
====================

/api/v1/account/switch_operation_options
-----------------------------------------------
调整运营参数

输入:

- account
- instance : 实例名
- product : docsdue / sms
- options :

  - docs_level
  - projects_level
  - quota
  - docs_users
  - records

/api/v1/account/buy
-----------------------------------------------
购买支付

输入：

- account
- instance : 
- product : ducsdue / sms
- amount

/api/v1/account/get_ticket
--------------------------------

输入：

- account
- instance : 
- product : ducsdue / sms

输出：

- options
- unit_price
- amount

/api/v1/account/get_remaining
--------------------------------
查询账户余额

输入：

- account

