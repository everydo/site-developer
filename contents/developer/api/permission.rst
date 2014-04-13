---
title: 权限和授权
description: 检查权限、分配权限
---

================
权限和授权
================

.. contents::

系统中可以直接修改权限来进行权限管理，也可以通过修改角色来进行权限管理。

角色是一组权限的集合，方便成批授权，角色只能使用系统中定义的角色，不可自定义。

权限和角色的操作都通过IGrantManager接口进行。


权限
-----------------------

系统中常用权限，权限ID为字符串类型，下文中权限ID将用permisson_id来代替。

- 'zope.Public'：公开，任何人都可以访问
- 'zope.ManageContent'：管理
- 'zope.View'：查看的权限
- 'zopen.Access'：容器/栏目访问的权限
- 'zopen.Edit'：编辑的权限
- 'zopen.Add'：添加文件、流程单
- 'zopen.AddFolder': 添加文件夹
- 'zopen.AddContainer': 添加容器(子栏目)
- 'zopen.Logined': 是否登录

'zopen.Access'和'zope.View'的区别，需要进入文件夹(zopen.Access)，但是不希望查看文件夹包含的文档(zope.View)。

检查权限
-------------
检查当前用户对某对象是否有某种权限，可使用checkPermission方法::

  checkPermission(permission_id, obj)

如果有该权限即返回True，反之返回False

角色
----------------------

系统支持如下角色，角色ID为字符串类型，下文中角色ID将用role_id来代替。

TODO 应对角色定义做更详细的说明

- 'zopen.PrivateReader' 保密查看人
- 'zopen.Manager' 管理员
- 'zopen.Editor' 编辑人
- 'zopen.Owner' 拥有者
- 'zopen.Collaborator' 添加人
- 'zopen.Creator': 文件夹创建人
- 'zopen.ContainerCreator': 子栏目/容器创建人
- 'zopen.Responsible' 负责人
- 'zopen.Subscriber' 订阅人
- 'zopen.PrivateReader' 超级查看人
- 'zopen.PrivateReader4' 仅仅文件授权的时候用，不随保密变化
- 'zopen.PrivateReader3' 仅仅文件授权的时候用，不随保密变化
- 'zopen.PrivateReader2' 仅仅文件授权的时候用，不随保密变化
- 'zopen.PrivateReader1' 仅仅文件授权的时候用，不随保密变化
- 'zopen.Reader5'
- 'zopen.Reader4'
- 'zopen.Reader3'
- 'zopen.Reader2'
- 'zopen.Reader1'
- 'zopen.Accessor' 访问者

授权
--------------
通过IGrantManager来管理角色

在obj对象上，授予用户某个角色::

  IGrantManager(obj).grantRole(role_id,user_id)

同上，禁止角色::

  IGrantManager(obj).denyRole(role_id,user_id)

同上，取消角色::

  IGrantManager(obj).unsetRole(role_id,user_id)

权限检查
------------
根据角色来获取obj对象上拥有该角色的用户ID::

  IGrantManager(obj).getContextPrincipalsForRole(role_id)

得到上层以及全局的授权信息::

  IGrantManager(obj).getInheritedPrincipalsForRole(role_id)

得到最近一组拥有角色的用户ID::

  IGrantManager(obj).getNearestPrincipalsForRole(role_id)

得到某个用户在obj上的所有角色::

  IGrantManager(obj).getContextRolesForPrincipal(user_id)

得到某个用户在上层继承的角色::

  IGrantManager(obj).getInheritedRolesForPrincipal(user_id)

权限与角色的转换
------------------------------

得到拥有某权限的所有角色::

  IGrantManager(obj).getRolesForPermission(permission_id)

得到上层以及全局的授权信息::

  IGrantManager(obj).getInheritedRolesForPermission(permission_id)

