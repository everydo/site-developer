---
title: 升级说明
description: 如果从老版本升级到新版本
---

============
升级说明
============

总体说明：

- 适配器都过时了
- 接口过时了
- utility转移到了root

安装和部署
===================
- deployApplet 改为 container.add_folder / add_appcontainer /add_datacontaner , 不会自动授权，不会自动添加到导航
- IAppletData -> context.navs

授权
=====================
所有的接口都过时了， ``Ixxx(context)`` 改为 context.xxx

- IGrantManger -> context.acl
- 角色 ``zopen.Reader1``  改为 ``Reader1``
- 权限 ``zopen.View`` 改为 ``View``
- ``checkPermission('zope.View', obj)`` 改为 ``obj.check_permission("View")``

所有的接口都过时了
=========================
- 不推荐使用 ``IFile`` ``IFolder`` 之类，使用 ``context.object_types`` 替换 
- ``IFile.providedBy(context)`` 改为 ``'File' in context.object_types``

搜索
=========
- 该搜索 ``object_types`` 而不是 ``object_provides``
- 工作项的搜索，应该使用独立的索引 ``QuerySet('workitems')``
- 搜索工作项的负责人，应该是 ``QuerySet('workitem').anyof(Response=['users.panjunyong'], parent='acl')``
- ``IObjectIndexer(context)`` 以及 ``indexObject()`` ``reindexObject`` 过时，改为 ``obj.index()`` ``obj.reindex()``
- 搜索表单容器应该根据 ``metadata`` 来搜索
- 搜索某种表单应该根据 ``item_metadata`` 了搜索

统一设置信息
====================
设置信息全部到 context.settings 里面，包括：

- 显示列
- 显示方式
- 关联流程定义、关联阶段、关联表单定义等

容器变化
===========
- 所有容器，默认都是可以排序的，前1000个对象是排序的
- ``len(container.values())`` 或 ``len(container.keys()`` 会报错，需要改为 ``len(container)``

表单
===========
- IFormEngine过时，直接用FieldsContainer
- IFieldStorage(context)过时 -> context.md
- IExtendedMetadata过时 -> context.mdset
- ISettings(container)过时，改为 container.md
- 表单分离校验和触发2个脚本
- related_edoclass过时，改为 container.settings['item_metadata']

团队
============
- 改为所有的appcontainer都支持团队
- ITeams(container) -> container.teams

流程
============
- related_workflow过时，改为 container.settings['item_workflow']
- 后续流程条件 ``nextsteps_conditions`` 过时，不推荐使用，改为后续步骤的进入条件
- 步骤支持auto_steps，自动步骤
- IFlowTasksManager(context) -> context.workitems
- 流程支持委托
