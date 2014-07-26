---
title: 账户管理
description: 应用的创建、服务级别切换、缴费、管理员设置等
---


============
账户管理
============

账户管理包括应用的创建、服务级别切换、缴费、管理员设置等。

.. contents::

账户角色管理
================

/api/v1/account/get_user_roles
--------------------------------------
得到用户在账户管理中的角色:

输入：

- account
- user

返回::
   
    {'account' :
     'user' :
     'roles'   : ['AccountAdmin'], }

这里的roles，是用户在账户管理中的全局组, 比如 ``AccountAdmin`` 就是账户管理员.

/api/v1/account/get_role_users
--------------------------------------
得到拥有某个角色的用户:

输入：

- account
- role

返回::
   
    {'account' : 'zopen',
     'role' : 'AccountAdmin',
     'users'   : ['zhangsan', 'lisi'], }

实例管理
===================

/api/v1/account/get_instance
--------------------------------------
得到某个应用实例的api访问入口

输入:

- account
- application
- instance

输出::

   {'application':, 
    'instance': 
    'operator':
    'title':, 
    'app_url':, 
    'api_url': 
   }

/api/v1/account/list_instances
-------------------------------------
得到账户，某个应用的所有实例:

输入：

- account
- application

返回各个应用的实例信息::

     { application:,[{ application:, 
                       instance:
                       operator:
                       title:,
                       app_url:, 
                       api_url:,},  
                    ]},

/api/v1/account/create_instance
-------------------------------------
创建一个应用实例:

输入：

- account
- application
- instance 
- title
- operator


返回创建的实例的信息::


   {'application':, 
    'instance': 
    'operator':
    'title':, 
    'app_url':, 
    'api_url': 
   }
   
/api/v1/account/destory_instance
-------------------------------------
删除一个应用实例:

输入：

- account
- application
- instance


返回是否删除成功::

   {'status':True}

/api/v1/account/update_instance
-------------------------------------
更新应用实例的名称:

输入：

- account
- application
- instance
- title


返回实例的信息::


   {'application':, 
    'instance': 
    'operator':
    'title':, 
    'app_url':, 
    'api_url': 
   }

切换运营参数
====================

1. 注册系统，默认会启动一套初始的服务，并赠送试用票，以便试用
2. 之后可以调整服务的参数。不同参数，功能不同，单价也不同。
3. 一旦调整参数，会将单价、数量、功能参数记录到票上，并通知具体服务
4. 一旦缴费，账户余额增加；一旦购买，余额减少，购买服务数量参数增加。

/api/v1/account/get_remaining
--------------------------------
查询账户余额，通过系统在线支付渠道，可以充值

输入：

- account

/api/v1/account/get_ticket
--------------------------------------
得到服务凭证信息

输入：

- account
- application
- instance : 
- ticket: 服务凭证名, 如：

  - due : 租用服务的服务期限
  - sms : 短信服务的服务量

输出：

- name : ticket的id, 目前是due/sms
- title: ticket的名字
- levels : 文档、项目等的服务级别
- quotas: 配额参数，如用户数、容量等
- price ：单价
- options : 详细的功能参数
- quantity：数量

/api/v1/account/list_tickets
--------------------------------------
得到实例的全部的ticket

输入：

- account
- application
- instance : 

输出： get_ticket的列表

/api/v1/account/update_ticket
-----------------------------------------------
调整凭证的服务级别和配额

输入:

- account
- application
- instance : 实例名
- ticket: due / sms
- levels : 服务级别, 一个dict，比如 {'docs':'standard', 'team':'free'}

  - docs : 文档管理级别
  - team: 团队协作级别

- quotas:

  - storage_size : 文档存储容量
  - item_count : 每月新增条目数，包括文档/表单
  - user_count : 使用用户数量

/api/v1/account/pay_ticket
-----------------------------------------------
凭证支付

输入：

- account
- application
- instance : 
- ticket: due / sms
- amount : 支付的余额

/api/v1/account/list_application_options
-------------------------------------------
应用的全套运营参数信息。

输入:

- application: 应用的id

输出::

  [{'sms': {'title':'短信数量', 'type':'number'), 
   {'rules': {'title':'是否支持规则引擎', 'type':'bool'),
   {'metadata': {'title':'是否支持元数据', 'type':'bool'),
   ...
  ]

其中type可以是：

- time: 时间、期限
- count：数量
- size: 存储容量
- amount: 金额
- bool: 逻辑

/api/v1/account/list_service_levels
-----------------------------------------
查询账户余额，通过系统在线支付渠道，可以充值

输入：

- service

输出::

 [ {'name': 'free',  # 级别名
    'title': '免费版',  # 级别标题
    'description': '5人以下免费使用', # 备注
    'options':{
       'docs_peruser_fee':8, 'pergiga_fee':10, 'perkrecords_fee':10,
       'docs_ctrfolder':False, 'docs_extedit':True, 'docs_facetag':True,
       'docs_repository':True, 'docs_finepermissions':True, 'docs_templates':True,
       'docs_personal':True, 'docs_publish':False,
       'rules':False, 'metadata':False,
       'apps_packages':0, 'apps_scripting':False,
       'flow_customize':False,
      }},
    {'name':'standard', 
     'title':'企业版',  
     'comment':'每月: 10元/人,10元/G,10元/千记录',
     'options':{
       'docs_peruser_fee':10, 'pergiga_fee':10, 'perkrecords_fee':10,
       'docs_ctrfolder':True, 'docs_extedit':True, 'docs_facetag':True,
       'docs_repository':True, 'docs_finepermissions':True, 'docs_templates':True,
       'docs_personal':True, 'docs_publish':True,
       'rules':False, 'metadata':False,
       'apps_packages':20, 'apps_scripting':False,
       'flow_customize':False,
       }},
 ]


