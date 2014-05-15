---
title: 认识软件包
description: 系统架构、接口层次
---

=================
软件包概述
=================

软件包是直接可以在easydo后端部署的应用打包形式。软件包可以在易度平台上安装部署，利用软件包来进行企业应用的快速开发

应用开发平台
==================

EasyDo提供了企业应用的快速开发平台，分如下几层::


 |                              扩展应用                                   |
 +-------------------------------------------------------------------------+
 |                             应用引擎                                    |
 |                                                                         |
 |       阶段、表单、流程、规则、皮肤、脚本、视图、软件包、外部服务访问    |
 +-------------------------------------------------------------------------+
 |                         内容协作（内容到人）                            |
 |                                                                         |
 |                评论、关注、收藏、通知、分享、团队、日志                 |
 +-------------------------------------------------------------------------+
 |                             内容仓库                                    |
 |                                                                         |
 |          应用容器、文件夹、文件、快捷方式、数据容器、数据项             |
 +-------------------------------------------------------------------------+
 |                             数据存储                                    |
 |                                                                         |
 |   容器、条目、属性、UID、关系、回收站、权限、状态、版本、标签组、索引   |
 +-------------------------------------------------------------------------+
 
从下往上，分别是：

- 数据存储层：负责系统数据的存取，可以基于多种方式来存储
- 内容仓库层：定义系统基础的内容类型，以及管理逻辑
- 内容协作层：将内容和人建立关系
- 应用逻辑层: 定制企业应用逻辑
- 扩展应用层

软件包
===========
软件包是扩展应用的代码载体，来组织自定义内容、代码逻辑和外部资源。软件包可以以独立文件的形式发布和交换。

总体借用Python语言来定义表单和流程. 压缩包内的文件组织::

  scripts/  # 脚本
    __init__.py
    aaa.py
  templates/ # 模板
    sales_chance.pt
  static/   # 静态文件
  stages/   # 阶段定义
    sales_chance.py
  dataitems/ # 表单数据
    sales_chance.py
    sales_chance.html
  datacontainers/  # 数据容器
    sales_chance.py
  appcontainers/  # 应用容器
    default.py
  mdsets/  # 属性集
    archive.py
  workflows/  # 工作流
    sales_chance.py
  rules/ # 规则
    set_security_level.py

python脚本
======================
python脚本可以直接通过浏览器调用

脚本采用python语言书写，存放在scripts中. 其中:

- setup: 用于部署
- upgrade(last_version='') : 用于升级

对于需要通过浏览器发起的请求，如下书写::

    @view_config(permission='zopen.Access', use_template='standard', icon=u'')
    def setup(redirect = True):
        """安装脚本

        初始化规则"""

        app = deployApplet('zopen.remind.workflows.remind', context, 'remind', '提醒')
        IObjectIndexer(app).index()
        #创建规则

如果仅仅是内部调用，则如下处理::

    def list_users():
        pass

模板
==============
采用Page Template格式，具体参照 “页面模板” 一章

管理软件包
=============
通过IPackages管理软件包。

查看已经安装的所有软件包::

  IPackages(root).keys()

创建一个软件包::

  package = IPacakges(root).new('zopen.test', title, description, version, platform, tags)

设置软件包信息::

  IPacakges(root).update('zopen.test', **kw)

得到一个软件包信息::

  packge = IPackages(root).get('zopen.test')

导入一个软件包::

  packge = IPackages(root).import('zopen.test', package_body)

导出一个软件包::

  packge = IPackages(root).export('zopen.test')


