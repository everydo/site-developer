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

所有节点，有唯一不重复的id. 节点包括三种：

组织单元: ou
-----------------------------------------
组织单元的对象类型(object_type)是 ``ou`` : Organization Unit.
如果公司、部门、科室、分公司等；可包含子组织单元、组、人员

- 'id': 组织的唯一ID，内部公司的根节点必须是 ``default``
- 'object_type': 对象类型必须是 'ou',
- 'parent': 所在上级ou的ID，根节点的parent为空
- 'title': ou的名字，如: '易度公司'

组: group
----------------
组的对象类型(object_type)是 ``group`` .
组不直接包含其他内容，但是可以组可以和人员建立关联关系

- 'id': 唯一的ID，如：'admin'
- 'object_type': 对象类型必须是'person'
- 'parent': 所在ou的ID：'default'
- 'title': 姓名，如 '张三'

人员: person
----------------
人员的对象类型(object_type)是 ``person`` .
如果人员信息，包括：

- 'id': 唯一的ID，如：'admin'
- 'object_type': 对象类型必须是'person'
- 'parent': 所在ou的ID：'default'
- 'title': 姓名，如 '张三'
- 'disable': 是否被禁用，比如:false
- 'email': 'test@zopen.cn'
- 'mobile': None
- 'number': 9223372036854775807
- 'phone': '123445566'
- 'xmpp_username': 'admin#zopen@127.0.0.1'

用户和组织结构读取
=======================

如果其他用户和组织结构读取接口，系统可以显示人员和组织结构。

/api/v1/org/get_objects_info
----------------------------------
得到一组对象的详细信息

参数 :

- ``account`` : 账户名，如： ``zopen``
- ``objects`` : 带类型的对象id清单，用逗号分隔，如： ``person:admin,ou:default``


返回对象信息列表::

  [{'id': 'admin',
    'object_type': 'person',
    'disable': False,
    'email': 'test@zopen.cn',
    'mobile': None,
    'number': 9223372036854775807,
    'parent': 'default',
    'phone': '123445566',
    'title': 'admin',
    'xmpp_username': 'admin#zopen@127.0.0.1'},

   {'id': 'default',
    'object_type': 'ou',
    'parent': '',
    'title': '易度公司'}]

/api/v1/org/list_groups_members
----------------------------------
得到组的成员，可以传入一组组，得到各组的成员信息

- ``account`` : 账户名，如： ``zopen``
- groups: 需要查询的组清单，使用逗号分隔多个，如：13123,123123,123

返回各组组成员ID清单 ::

  {'13123':['panjy','zhangsan'], 
   '123123':['lisi', 'wangwu'], 
   '123':['lisi']
   }

/api/v1/org/get_ou_detail
----------------------------------
得到部门下的组、子部门、部门成员信息

- ``account`` : 账户名，如： ``zopen``
- ou_id: 部门ID, 如：13123
- include_diabled，成员中是否包含被禁用人员

返回子部门、组、成员的ID清单 ::

  {
  'id':'13123',
  'title': "XX部",
  'users':['panjy','zhangsan'], 
  'groups':['group1', 'group2'], 
  'ous':['ou1']
  }

/api/v1/org/list_relations
-----------------------------------
查找某个人的上下级同事关系

参数 :

- account: 如 ``zopen``
- person: 人员的ID，如 ``zhangsan``
- relation: 可以是下列中的一个

  - superior : 上级
  - subordinate : 下级
  - colleague : 同级同事

返回:

范围关系和人员的清单::

  {"superior":['admin'],
   "subordinate":['zhangsan', 'lisi'],
   "colleague" : [],
  }

/api/v1/org/search
------------------------
搜索某个部门之下的某种对象

参数 :

- account: 如 ``zopen``
- ou: 搜索的部门ID，如 ``default``
- scope: 可以取值 单层 ``onelevel`` ，或者整个子树 ``subtree``
- object_type: 一个或者多个对象类型，比如： ``ou,group,person``
- include_diabled: 是否包含禁用的对象，默认 ``false``
- q: 搜索词，采用类似全文搜索的方式

返回::
   
   {'count': 10,
    'result': [{'id': 'admin',
                'object_type': 'person',
                'parent': 'default',
                'title': 'admin',
                'disable': false,
                'email': 'test@zopen.cn',
                'mobile': None,
                'number': 9223372036854775807,
                'phone': '123445566',
                'xmpp_username': 'admin#zopen@127.0.0.1'}
              ]
   }


/api/v1/org/list_person_ougroups
--------------------------------
得到人员所属的全部部门和组，包括所有的上级部门

参数 :

- account: 如 ``zopen``
- person: 人员的ID，如 ``zhangsan``

返回::

  { 'ous': ['458996', '789189', '593469', ], 
    'groups': ['524263', '580381', '952627', '343263'], }

用户和组织结构维护
==============================
如果使用系统自带的人员组织结构模块，也提供了用户和组织结构管理接口，可以实现增删改。

/api/v1/org/sync
------------------------
同步组织结构信息，支持新增和修改，不支持删除, 这个全球必须采用 POST 方式发送

- account
- send_mail：新增的用户是否发送通知邮件
- objects_detail: 新增对象的详细信息列表, 参考 ``get_objects_info`` 接口返回的结果

objects_detail事例 ::

   [{'id': 'admin', # 对象, 必填
    'object_type': 'person',#对象类型， 必填
    'disable': False, # 是否禁用用户， 需要修改才填写
    'email': 'test@zopen.cn', # 邮件地址, 需要修改才填写
    'mobile': '', # 需要修改才填写
    'number': 9223372036854775807,# 需要修改才填写
    'parent': 'default',# 所属部门 需要修改才填写
    'phone': '123445566',# 需要修改才填写
    'title': 'admin',# 需要修改才填写
    'xmpp_username': 'admin#zopen@127.0.0.1'# 需要修改才填写
    },

   {'id': 'default', # 必填
    'object_type': 'ou', # 必填
    'title': '易度公司'# 需要修改才填写}]

返回::

   {'status':true}

/api/v1/org/remove_objects
--------------------------
删除一个对象，可以是ou/person/group

- account
- objects: 带对象类型的对象id清单，比如： ``person:zhangsan,ou:1212,group:32112``

返回::

   { status:true }

/api/v1/org/remove_group_members
---------------------------------------
删除组的成员：

- account
- group: 组ID
- persons: 组成员的ID清单


/api/v1/org/add_group_members
--------------------------------------
组添加成员：

- account
- group : 组ID
- persons: 组成员的ID清单

/api/v1/org/remove_relation
-------------------------------
去除人的关系:

参数 :

- account: 如 ``zopen``
- person: 人员的ID，如 ``zhangsan``
- superior: 上级
- colleague: 同事, 多个用逗号分隔
- subordinate: 下级, 多个用逗号分隔

/api/v1/org/add_relation
--------------------------------
更改人的关系:

参数 :

- account: 如 ``zopen``
- person: 人员的ID，如 ``zhangsan``
- superior: 上级
- colleague: 同事, 多个用逗号分隔
- subordinate: 下级, 多个用逗号分隔
