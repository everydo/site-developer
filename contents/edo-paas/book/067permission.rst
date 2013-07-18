.. contents::

登录和权限
============================

**经常需要和其他异种系统实现单点登录：**

- "loginAs":登录
- "ssoURL": 单点登录
- ‘user_tracker’: 用户使用记录


权限与角色管理
==========================

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
- 'zopen.Add'：添加
- 'zopen.AddFolder'添加文件夹
- 'zopen.sso.logined': 是否登录

'zopen.Access'和'zope.View'的区别，存在这个情况，需要进入文件夹，但是不希望查看文件夹包含的文档。

通常文件夹或容器，都是要'zopen.Access'控制，对容器包含的内容，都采用'zopen.View'权限控制。 
原因是：zope.View的授权会继承下去，导致子文件夹的内容都是可以查看的了。
特别是项目、部门、工作组、客户，这些需要多层分区控制权限的，在根容器里面一定不能用zope.View的权限来控制，而是应该是zopen.Access。

(由于系统搜索会只搜索到有权限查看的内容，因此zopen.Access通常是安全的)。典型的情况：

- 希望使用项目，但不希望看到所有项目的数据
- 希望试用流程，但不希望看到别人提及的流程数据
- 因为权限会继承，如果使用'zope.View'权限，会导致过渡授权的情况。

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

得到拥有某权限的所有角色

IGrantManager(obj).getRolesForPermission(permission_id)

得到上层以及全局的授权信息

IGrantManager(obj).getInheritedRolesForPermission(permission_id)
