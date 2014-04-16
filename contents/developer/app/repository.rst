---
title: 内容仓库
description: 系统首先是一个各种内容的存储仓库，都父子树状组织存放，有唯一的ID标识，支持版本，支持回收站
---

==================
内容仓库
==================

.. Contents::
.. sectnum::

系统可以存储各种文件、表单等内容，通过各种文件夹、栏目、表单管理器来组织管理。从根本上系统是存放各种内容的仓库。

根站点
=============

根站点下包括一组系统的自定义信息，比如表单、流程、规则等.

应用容器 AppContainer
-----------------------
只有在应用容器里面，才能部署其他的应用。网站根就是一个应用容器，下面的方法得到网站根::

  app_container = root = get_root()

应用容器里可以存放 表单容器、文件夹和子栏目::

  folder = app_container.deploy_folder(name, metadata={'title':'a folder',}, **mdsets)
  collection = app_container.deploy_data_container(name, metadata={'title':'a collcetion'}, **mdsets)
  sub_container = app_container.deploy_section(name, metadata={'title':'a sub container'}, **mdsets)

其中:

- metadata: 新部署应用的元数据
- mdsets: 新部署应用的一组属性集

文件夹 Folder
----------------
文件夹用来存放文件和文件的快捷方式，文件夹还能存放子文件夹::

  sub_folder = folder.add_folder(name, metadata={}, **mdsets)
  shortcut = folder.add_shortcut(obj, version_id='', metadata={}, **mdsets)
  new_file = folder.add_file(name, data='', content_type='', metadata={}, **mdsets)

文件 File
-------------
文件是最基础的内容形态，用于存放非结构化的数据，不能包含其他内容

快捷方式 ShortCut
---------------------
快捷方式可以指向其他的文件或者文件夹，不能包含其他内容::

  shortcut.get_orign()
  shortcut.reset_version(version_id)

数据集 DataContainer
-------------------------
用于存放表单数据项::

  item = collection.add_item(metadata, **mdsets)

数据项 DataItem
-------------------
数据项用来存放结构化的表单数据，是系统的基础内容，不能包含其他内容.


权限控制
================

系统中可以直接修改权限来进行权限管理，也可以通过修改角色来进行权限管理。

权限和角色的操作都通过IGrantManager接口进行。

授权
--------------
通过IGrantManager来管理角色

在obj对象上，授予用户某个角色::

  IGrantManager(obj).grantRole(role_id,user_id)

同上，禁止角色::

  IGrantManager(obj).denyRole(role_id,user_id)

同上，取消角色::

  IGrantManager(obj).unsetRole(role_id,user_id)

系统支持如下角色，角色ID为字符串类型，下文中角色ID将用role_id来代替。

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

检查权限
-------------
检查当前用户对某对象是否有某种权限，可使用checkPermission方法::

  checkPermission(permission_id, obj)

如果有该权限即返回True，反之返回False

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

读取权限
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



对象的状态
===========================
每一个对象存在一组状态，存放在对象的context.stati属性中

modify: 发布

- modify.default	草稿
- modify.pending	待审
- modify.archived	发布/存档 (只读)
- modify.history_default 普通历史版本
- modify.history_archived 发布的历史版本

visible: 保密

- visible.default	普通
- visible.private	保密

使用状态机IStateMachine，来控制对象状态的变化::

    # 不进行权限检查，直接发布某个文档
    IStateMachine(context).set_state('modify.archived', do_check=False)
    # 设置文件夹为受控
    IStateMachine(context).set_state('folder.control', do_check=False)

其包括的接口有：

- getAllStates()	得到对象的所有状态	
- getState(prefix) 得到某个的状态	
- setState(new_state, do_check=True) 设置状态	
- nextStates(self, prefix) 得到后续状态	

标签组
============

标签组实现了多维度、多层次、可管理的分类管理。

标签设置
---------------
另外，使用IFaceTagSetting可进行标签设置的管理：

- getFaceTagText(): 得到face tag 文字
- setFaceTagText(text): 
  设置face tag文字，会自动转换的, 典型如下::

   按产品
   -wps
   -游戏
   -天下
   -传奇
   -毒霸
   按部门
   -研发
   -市场

- getFaceTagSetting(): 得到全部的face tag setting::

   [(按产品, (wps, (游戏, (天下, 传奇)), 毒霸)),
    (按部门, (研发, 市场))]

- check_required(tags): 返回遗漏的标签分组list

标签维护
-------------
如果要添加一个标签:

ITagsManager(sheet).addTag('完成')

希望同时去除这个标签组中的所在维度其他的标签， 比如"处理中"这样的状态，因为二者不能同存:

ITagsanager(sheet).addTag('完成', exclude=True)

这里使用ITagManager进行标签管理。完整接口为

- listTags(): 得到全部Tags
- setTags(tags): 更新Tags
- addTag(tag, exclude=False):
  添加一个Tag, 如果exclude，则添加的时候， 把FaceTag的同一类的其他标签删除
- delTag(tag): 删除指定Tag
- canEdit(): 是否可以编辑

站点对象
==================

存放数据
--------------
::

 /  根站点
    _etc/
        rules/
        packages/
           zopen.default/
           zopen.sales/
              forms/
              mdsets/
              stages/
        skins/
           bootstrap

服务入口
---------
全部兑现唯一标示::

  root.get_intid_register() # 唯一标示注册表

表单定义::

  root.get_form_registry()
  root.get_settings_registry()
  root.get_mdset_registry()

规则定义::

  root.get_rule_registry()

皮肤定义::

  root.get_skin_registry()

流程定义::

  root.get_workflow_registry()

软件包管理::

  root.get_package_registry()


站点设置信息
----------------

得到某个运营选项参数::

    root.get_operation_option(option_name=None, default=None)

option_name可以是如下参数：

- sms: 短信数量
- apps_packages: 软件包数量
- flow_records: 数据库记录
- docsdue: 文档使用期限
- docs_quota: 文件存储限额(M)
- docs_users: 文档许可用户数
- docs_publish: 文档发布
- flow_customize: 流程定制
- apps_scripting: 允许开发软件包
