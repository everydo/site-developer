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

应用容器 AppContainer
=============================
只有在应用容器里面，才能部署其他的应用，网站根就是一个应用容器。
应用容器里可以存放 表单容器、文件夹和子栏目::

  folder = app_container.add_folder(name)
  collection = app_container.add_datacontainer(name)
  sub_container = app_container.add_appcontainer(name)

其中:

- metadata: 新部署应用的元数据
- mdsets: 新部署应用的一组属性集

文档管理
================
包括转换、预览、全文索引、管控等等。

文件夹 Folder
----------------
文件夹用来存放文件和文件的快捷方式，文件夹还能存放子文件夹::

  sub_folder = folder.add_folder(name)
  new_file = folder.add_file(name, data='', content_type='')
  shortcut = folder.add_shortcut(obj, version_id='')

文件 File
-------------
文件是最基础的内容形态，用于存放非结构化的数据，不能包含其他内容::

  new_file.set_data('这是文件内容')

快捷方式 ShortCut
---------------------
快捷方式可以指向其他的文件或者文件夹，不能包含其他内容::

  shortcut.get_orign()
  shortcut.reset_version(version_id)

数据管理
==================

数据容器 DataContainer
-------------------------
用于存放表单数据项::

  item = collection.add_item(metadata, **mdsets)

数据项 DataItem
-------------------
数据项用来存放结构化的表单数据，是系统的基础内容，不能包含其他内容.

根站点对象
==================
根站点下包括一组系统的自定义信息，比如表单、流程、规则等.

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
