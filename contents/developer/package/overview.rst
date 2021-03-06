---
title: 认识软件包
description: 系统架构、接口层次
---

=================
软件包概述
=================

软件包是直接可以在easydo后端部署的应用打包形式。软件包可以在易度平台上安装部署，利用软件包来进行企业应用的快速开发

Python语言
==================
易度软件包采用python语言开发。python语言是一门非常简洁的语言，详细的学习参看：

   `Python教程(廖雪峰) <http://www.liaoxuefeng.com/wiki/001374738125095c955c1e6d8bb493182103fac9270762a000>`__

对于易度来说，仅仅使用了python的基础语法，因此只需要学习前6章即可（Python简介、安装Python、第一个Python程序、Python基础、函数、高级特性）。

软件包
===========
软件包是扩展应用的代码载体，来组织自定义内容、代码逻辑和外部资源。软件包可以以独立文件的形式发布和交换。

一个软件包包括如下定制内容:

- 表单语义：包括数据项、数据容器、应用容器等的表单输入字段
- 属性集：额外的属性定义
- 阶段：
- 流程步骤
- 规则
- 皮肤
- 脚本
- 模板
- 资源文件

总体借用Python语言来定义表单和流程. 压缩包内的文件组织::

  scripts/  # 脚本
    __init__.py
    aaa.py
    setup.py
  templates/ # 模板
    sales_chance.pt
  stages/   # 阶段定义
    sales_chance.yaml
  metadata/ # 元数据定义
    sales_chance.yaml
    sales_chance_container.yaml
    appcontainers.yaml # 应用容器设置
  mdsets/  # 属性集定义
    archive.py
  workflows/  # 工作流
    sales_chance.py
  rules/ # 规则
    set_security_level.py
  skins/  # 皮肤
    template_a.py
  resources/   # 静态资源文件 css/js/img

软件包的任何一个资源，可以用下面的方法来标识::

  <软件包名>:<对象名>

比如: ``zopen.sales:chance`` . 同一个标识，可能是表单定义、流程，或者规则，具体是什么需要根据使用场景来判定。

系统自带一个内置的软件包，对于这个软件包里面的对象，可以直接用 ``<对象名>`` 来标识

管理软件包
=============
通过 ``root.packages`` 管理软件包。

查看已经安装的所有软件包::

  root.packages.keys()

创建一个软件包::

  package = root.packages.new('zopen.test', title, description, version, platform, tags)

设置软件包信息::

  root.packages.update('zopen.test', **kw)

得到一个软件包信息::

  packge = root.packages.get('zopen.test', detail=False)

在应用市场安装(升级)一个软件包::

  root.packages.install('zopen.test', upgrade=False)

卸载、删除一个安装的软件包::

  root.packages.remove('zopen.test')

应用开发平台
==================

EasyDo提供了企业应用的快速开发平台，分如下几层::


 |                           扩展应用(软件包)                              |
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
- 应用引擎层: 支撑企业应用逻辑
- 扩展应用层: 以软件包的形式组织企业定制逻辑

