---
title: 人员和组织管理接口
description: 人员和组织的管理，方便其他系统自动导入现有人员数据进行初始化
---

========================
人员和组织管理接口
========================

个人相关
=================

/api_get_token_info
--------------------------------------
当前登录用户的基本信息

返回::
   
   {'app_id' : 'workonline',
    'account' : 'zopen',
    'vendor'  : 'standalone',
    'pid'    : 'users.test'
    }
   

/api_check_password
--------------------------------
检查密码

参数：
- pid
- password

返回::
  
  {'pid':'users.test',
    'status':True}

/api_reset_password
-----------------------------
重置密码

参数：
- password
- new_password

返回::

  {'pid':'users.test',
   'status': True}

/api_enable_dynamic_auth
---------------------------
开启动态认证

参数：
- secret_key
- code

返回::

   {'pid':'users.test', 
     'status':True}


/api_disable_dynamic_auth
------------------------------
关闭动态认证

参数：

- code

返回::

   {'pid':'users.test', 
     'status':True}

/api_is_dynamic_auth_enabled
----------------------------------
检查是否开启动态认证

返回::

   {'pid':'users.test', 
     'status':True}

用户管理
=======================
/api_remove_user
--------------------------
删除一个用户

- pid
- account

返回::

   {'pid':'users.test', 
     'status':True}
  

/api_get_principal_info
-------------------------------
得到某个用户的信息

- pid
- acount

返回::

    #得到实体的基本信息。实体：用户、组(部门、群组、角色)等。
    if member:
        return {'id':id, 'title': titlei, 'mobile': mobile, 'email': email, 'parent':parent_id}
    if ou or group or role:
        return {'id':id, 'title': title, 'parent':''}

/api_has_user
---------------------------
是否有某用户

- pid
- account

返回::

  {'pid':'users.test',
    'status':True}

/api_list_user_groups
-----------------------------
得到用户所属的组

- pid
- account

返回::

   {'ous':[ou_id, ...],
            'groups':[group_id, ...],
            'jobs':[job_id, ...],
            'roles':[role_id, ...],
            'licenses':[service_name, ...]
            }

/api_list_principals_info
-----------------------------
得到一组实体的基本信息。实体：用户、组(部门、群组、角色)等。

输入：

- pids
- account

输出：

- 人员信息：[{'id':id, 'title': titlei, 'mobile': mobile, 'email': email}]
- 组： [{'id':id, 'title': title}]

公司管理
=============

/api_sync
-----------------
同步用户信息

- ous: 部门信息
- groups：组
- users：人员
- send_mail：是否发送通知邮件
- account

返回::

   {'status':True}

/list_org_structure
---------------------------
得到整个组织结构

- account


/list_companies
-----------------------
得到全部外部公司

- account

/list_instances
-----------------------
- account

部门管理
===============

/api_remove_ous
--------------------
删除一组部门

- pids
- account
    
/get_ou_detail
------------------
得到部门详细信息

- pid
- include_disabled
- account

/has_ou
------------
是否存在某个部门

- pid
- account

组管理
==========

/list_group_members
------------------------
得到某个组的成员

- pid
- account


/remove_groups
------------------------
删除一组组

- pids
- account


/add_group_users
------------------------
- pid : 组
- pids : 组成员
- account

/remove_group_users
--------------------------
- pid
- pids
- account

LDAP认证
===============

/set_ldap_config
--------------------
- server_address
- enable
- account

/get_ldap_config
-----------------------
- account

授权设置
============
/set_allowed_services
-----------------------------
- pid
- app_name
- instance_name
- services
- account

/get_allowed_services
-----------------------------
- account
- username
- app_name
- instance_name
