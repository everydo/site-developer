---
title: 账户管理
description: 应用的创建、服务级别切换、缴费、管理员设置等
---

============
账户管理
============

账户管理包括应用的创建、服务级别切换、缴费、管理员设置等。

/api/v1/account/get_token_info
=========================================
当前登录用户的基本信息:

返回::
   
   {'app_id'  : 'workonline',
    'account' : 'zopen',
    'user_id': 'test'
    }

实例管理
===================

/api/v1/account/create_instance
-------------------------------------
创建应用实例

/api/v1/account/list_instances
-------------------------------------
得到账户所有的应用实例:

输入：

- account

返回各个应用的实例信息::

   [
     { application:,[{ id:, 
                       title
                       app_url:, 
                       api_url,},  ]}
   ]

/api/v1/account/get_api_address
--------------------------------------
得到某个应用实例的api访问入口::

  http://app.easydo.cn/api/v1/account/get_api_address?account=zopen&app=workonline&instance=default

