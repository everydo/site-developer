---
title: 根站点
decription:
---

=============
根站点
=============

根站点下包括一组系统的自定义信息，比如表单、流程、规则等.

功能入口
==================
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


存放数据
===============
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


站点设置信息
===================

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
