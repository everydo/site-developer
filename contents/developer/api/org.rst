---
title: 人员和组织管理接口
description: 人员和组织的管理，方便其他系统自动导入现有人员数据进行初始化
---

========================
人员和组织管理接口
========================

系统自带组织结构，也可以由第三方来提供。

组织结构模型
===========================

示例::

   default/ : 本公司

     ou_a/ : 组织单元a

         ou_a_a/ : 组织单元a-a
         ou_a_b/ : 组织单元a-a

         group_a : 组a
         group_b : 组b

         user_c 人员C
         user_d 人员D

     ou_b/ : 组织单元b

     group_a : 组a
     group_b : 组b

     user_a 人员A
     user_b 人员A

   xxx/ : 某外部公司

说明：

- 节点包括:

  - 组织单元(ou, Organization Unit): 如果公司、部门、科室、分公司等；可包含子组织单元、组、人员
  - 组(Group): 组不直接包含其他内容，但是可以组可以和人员建立关联关系
  - 人员(User): 不可包含其他内容

- 所有节点，有唯一不重复的id


用户和组织结构读取
=======================

如果其他用户和组织结构读取接口，系统可以显示人员和组织结构。

/api/get_principals_info
----------------------------------
得到一组对象的详细信息

参数 ::
{'account': 'zopen', 'pid': ['person:admin', 'ou:default']}


返回::

[{u'disable': False,^M
  u'email': u'test@zopen.cn',^M
  u'id': u'admin',^M
  u'mobile': None,^M
  u'number': 9223372036854775807,^M
  u'object_type': u'person',^M
  u'parent': u'default',^M
  u'phone': u'123445566',^M
  u'title': u'admin',^M
  u'xmpp_username': u'admin#zopen@127.0.0.1'},^M
 {u'id': u'default',^M
  u'object_type': u'ou',^M
  u'parent': u'',^M
  u'title': u'\u6613\u5ea6\u516c\u53f8'}]^M



/api/list_group_users
----------------------------------
得到组的用户，可以传入一组组，得到全部用户

- groups
- account

返回去重之后的用户ID清单 ::

  ['admin', 'tests', 'cha']

/api/search
------------------------
搜索某个部门之下的某种对象

- account:
- ou: 
- scope: base/onelevel/subtree
- object_type:  ou/group/person
- term: 搜索词

返回结果::

  {count: 60,
   results: [{'id':,'title':, 'object_type':},...],
  },

/api/list_user_groups
--------------------------------
得到用户所属的组，包括所有的上级部门

- pid
- account

返回::

  {'ous':[ou_id, ...],  # 部门
   'groups':[group_id, ...],  # 组
  }

用户和组织结构维护
==============================
如果使用系统自带的人员组织结构模块，也提供了用户和组织结构管理接口，可以实现增删改。

/api/remove_user
--------------------------

删除一个用户

- pid
- account

返回::

   {'pid':'users.test', 
     'status':True}

/api_sync[分拆]
------------------------

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

