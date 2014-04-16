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






::


API: api_has_user
===============================
参数:
{'account': 'zopen', 'user_id': 'users.admin'}
===============================
返回：
{u'pid': u'admin', u'status': True}


API: api_principal_info
===============================
参数:
{'account': 'zopen', 'pid': 'users.admin'}
===============================
返回：
{u'disable': False,
 u'email': u'test@zopen.cn',
 u'id': u'users.admin',
 u'mobile': u'',
 u'number': 9223372036854775807,
 u'parent': u'groups.tree.default',
 u'phone': u'123445566',
 u'title': u'admin',
 u'xmpp_username': u'admin#zopen@127.0.0.1'}


API: api_list_user_groups
===============================
参数:
{'account': 'zopen', 'user_id': 'users.admin'}
===============================
返回：
{u'groups': [],
 u'licenses': [u'groups.license.workonline-default-docs',
               u'groups.license.workonline-default-projects',
               u'groups.license.workonline-default-sms'],
 u'ous': [u'groups.tree.default'],
 u'roles': [u'groups.role.AccountAdmin']}


API: api_list_principals_info
===============================
参数:
{'account': 'zopen', 'users': "['users.admin']"}
===============================
返回：
[{u'disable': False,
  u'email': u'test@zopen.cn',
  u'id': u'users.admin',
  u'mobile': u'',
  u'number': 9223372036854775807,
  u'parent': u'groups.tree.default',
  u'phone': u'123445566',
  u'title': u'admin',
  u'xmpp_username': u'admin#zopen@127.0.0.1'}]


API: api_has_ou
===============================
参数:
{'account': 'zopen', 'ou_id': 'groups.tree.default'}
===============================
返回：
{u'status': True}


API: api_list_group_members
===============================
参数:
{'account': 'zopen', 'group_id': 'groups.tree.default'}
===============================
返回：
[u'users.admin']


API: api_get_ou_detail
===============================
参数:
{'account': 'zopen', 'include_disabled': True, 'ou_id': 'groups.tree.default'}
===============================
返回：
{u'groups': [],
 u'id': u'groups.tree.default',
 u'ous': [u'groups.tree.870705',
          u'groups.tree.341644',
          u'groups.tree.496030',
          u'groups.tree.984029',
          u'groups.tree.331002',
          u'groups.tree.673326',
          u'groups.tree.418133',
          u'groups.tree.447598'],
 u'parent': u'',
 u'title': u'\u6613\u5ea6\u516c\u53f8',
 u'users': [u'users.admin']}


API: api_list_org_structure
===============================
参数:
{}
===============================
返回：
{u'groups': [],
 u'id': u'groups.tree.default',
 u'jobs': [],
 u'ous': [{u'groups': [],
           u'id': u'groups.tree.870705',
           u'jobs': [{u'id': u'groups.jobs.414859',
                      u'title': u'\u603b\u88c1'},
                     {u'id': u'groups.jobs.596287',
                      u'title': u'\u526f\u603b\u88c1'},
                     {u'id': u'groups.jobs.310415',
                      u'title': u'\u603b\u7ecf\u7406'},
                     {u'id': u'groups.jobs.480109',
                      u'title': u'\u603b\u7ecf\u7406\u52a9\u7406'}],
           u'ous': [],
           u'title': u'\u603b\u88c1\u529e',
           u'type': u'department'},
          {u'groups': [],
           u'id': u'groups.tree.341644',
           u'jobs': [{u'id': u'groups.jobs.954491',
                      u'title': u'\u884c\u653f\u7ecf\u7406'},
                     {u'id': u'groups.jobs.388651',
                      u'title': u'\u884c\u653f\u6587\u5458'},
                     {u'id': u'groups.jobs.971457',
                      u'title': u'\u524d\u53f0'}],
           u'ous': [],
           u'title': u'\u884c\u653f\u90e8',
           u'type': u'department'},
          {u'groups': [],
           u'id': u'groups.tree.496030',
           u'jobs': [{u'id': u'groups.jobs.516268',
                      u'title': u'\u8d22\u52a1\u603b\u76d1'},
                     {u'id': u'groups.jobs.525827',
                      u'title': u'\u4f1a\u8ba1/\u51fa\u7eb3'},
                     {u'id': u'groups.jobs.835508',
                      u'title': u'\u8d22\u52a1\u52a9\u7406'}],
           u'ous': [],
           u'title': u'\u8d22\u52a1\u90e8',
           u'type': u'department'},
          {u'groups': [],
           u'id': u'groups.tree.984029',
           u'jobs': [{u'id': u'groups.jobs.473068',
                      u'title': u'\u4eba\u529b\u8d44\u6e90\u7ecf\u7406'},
                     {u'id': u'groups.jobs.432317',
                      u'title': u'\u4eba\u529b\u8d44\u6e90\u52a9\u7406'},
                     {u'id': u'groups.jobs.988590',
                      u'title': u'\u4eba\u529b\u8d44\u6e90\u4e13\u5458'}],
           u'ous': [],
           u'title': u'\u4eba\u529b\u8d44\u6e90\u90e8',
           u'type': u'department'},
          {u'groups': [],
           u'id': u'groups.tree.331002',
           u'jobs': [{u'id': u'groups.jobs.662923',
                      u'title': u'\u57f9\u8bad\u7ecf\u7406'},
                     {u'id': u'groups.jobs.569267',
                      u'title': u'\u7ba1\u7406\u54a8\u8be2\u5e08'},
                     {u'id': u'groups.jobs.878178',
                      u'title': u'\u57f9\u8bad\u4e3b\u7ba1'},
                     {u'id': u'groups.jobs.503610',
                      u'title': u'\u54a8\u8be2\u987e\u95ee'}],
           u'ous': [],
           u'title': u'\u57f9\u8bad\u7ba1\u7406\u4e2d\u5fc3',
           u'type': u'department'},
          {u'groups': [],
           u'id': u'groups.tree.673326',
           u'jobs': [{u'id': u'groups.jobs.113369',
                      u'title': u'\u5ba2\u670d\u7ecf\u7406'},
                     {u'id': u'groups.jobs.657567',
                      u'title': u'\u5927\u5ba2\u6237\u4e3b\u7ba1'},
                     {u'id': u'groups.jobs.227303',
                      u'title': u'\u5ba2\u670d\u6587\u5458'}],
           u'ous': [],
           u'title': u'\u5ba2\u6237\u670d\u52a1\u90e8',
           u'type': u'department'},
          {u'groups': [],
           u'id': u'groups.tree.418133',
           u'jobs': [{u'id': u'groups.jobs.798079',
                      u'title': u'\u5e02\u573a\u603b\u76d1'},
                     {u'id': u'groups.jobs.162954',
                      u'title': u'\u5e02\u573a\u7ecf\u7406'},
                     {u'id': u'groups.jobs.872210',
                      u'title': u'\u5e02\u573a\u52a9\u7406'},
                     {u'id': u'groups.jobs.407001',
                      u'title': u'\u8425\u9500\u4ee3\u8868'},
                     {u'id': u'groups.jobs.960814',
                      u'title': u'\u62d3\u5c55\u4e3b\u7ba1'},
                     {u'id': u'groups.jobs.948531',
                      u'title': u'\u62d3\u5c55\u4e13\u5458'}],
           u'ous': [],
           u'title': u'\u5e02\u573a\u5f00\u53d1\u90e8',
           u'type': u'department'},
          {u'groups': [],
           u'id': u'groups.tree.447598',
           u'jobs': [{u'id': u'groups.jobs.947686',
                      u'title': u'\u516c\u5171\u5173\u7cfb\u7ecf\u7406'},
                     {u'id': u'groups.jobs.658513',
                      u'title': u'\u516c\u5173\u52a9\u7406'},
                     {u'id': u'groups.jobs.698404',
                      u'title': u'\u5a92\u4ecb\u6267\u884c'}],
           u'ous': [],
           u'title': u'\u5a92\u4ecb/\u516c\u5173\u90e8',
           u'type': u'department'}],
 u'title': u'\u6613\u5ea6\u516c\u53f8',
 u'type': u'company'}


API: api_list_companies
===============================
参数:
{}
===============================
返回：
[]


API: api_list_instances
===============================
参数:
{}
===============================
返回：
{u'workonline': {u'default': {u'operator_name': u'0',
                              u'title': u'\u4e3b\u7ad9',
                              u'url': u'http://192.168.1.115:60200/++skin++EDOWorkonlineSkin/wo/default.zopen.standalone'}}}



