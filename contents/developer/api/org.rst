---
title: 人员和组织管理接口
description: 人员和组织的管理，方便其他系统自动导入现有人员数据进行初始化
---

========================
人员和组织管理接口
========================

系统自带组织结构，也可以由第三方来提供。

用户和组织结构读取
=======================

如果其他用户和组织结构读取接口，系统可以显示人员和组织结构。

/api_get_principal_info[废弃]
--------------------------------------
得到某个用户的信息

- pid
- acount


返回::

    #得到实体的基本信息。实体：用户、组(部门、群组、角色)等。
    if member:
        return {'id':id, 'title': titlei, 'mobile': mobile, 'email': email, 'parent':parent_id}
    if ou or group or role:
        return {'id':id, 'title': title, 'parent':''}

/api_list_principals_info[保留]
------------------------------------------------------
得到一组实体的基本信息。实体：用户、组(部门、群组、角色)等。

输入：

- pids
- account

输出：

- 人员信息：[{'id':id, 'title': title, 'mobile': mobile, 'email': email}]
- 组： [{'id':id, 'title': title}]

/api_list_user_groups
--------------------------------
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

/list_group_members[api_search替换]
----------------------------------------------
得到某个组的成员

- pid
- account

/list_companies[api_search替换]
--------------------------------------------
得到全部外部公司

- account

/get_ou_detail[废弃]
----------------------------
得到部门详细信息

- pid
- include_disabled
- account


用户和组织结构维护
==============================
如果使用系统自带的人员组织结构模块，也提供了用户和组织结构管理接口，可以实现增删改。

/api_remove_user
--------------------------

删除一个用户

- pid
- account

返回::

   {'pid':'users.test', 
     'status':True}

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

/api_remove_ous
--------------------
删除一组部门

- pids
- account
    
/remove_groups
------------------------
删除一组组

- pids
- account

/remove_group_users
--------------------------
- pid
- pids
- account


/add_group_users
------------------------
- pid : 组
- pids : 组成员
- account

/api_search
------------------------

- parent
- keywork
- type
- start
- limit
- length

返回::

  [{'id':id, 'title': titlei, 'mobile': mobile, 'email': email, 'parent':parent_id},
  {'id':id, 'title': titlei, 'mobile': mobile, 'email': email, 'parent':parent_id},
   {'id':id, 'title': titlei, 'mobile': mobile, 'email': email, 'parent':parent_id}]
   


