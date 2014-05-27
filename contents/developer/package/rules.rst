---
title: 规则
description: 如何在文件夹和流程中使用规则进行个性化定制
---

============
规则
============

通过规则，可以指定在某个位置，某个对象发生某种事件，满足一定条件时，执行一些操作。

在界面中使用
======================
分2步：

1. 系统管理员，在系统设置中定义规则

   规则定义了发生事件，满足的条件和执行的操作。

2. 文件夹、流程的管理员，使用规则

   将预先定义的规则，绑定到文件夹或者流程。

创建规则
==================
创建一个规则::

   root.packages.add_rule('zopen.ocr', 
                   {'name':'ocr',
                    'title':'中文OCR', 
                    'description':'对移入文件夹的文档进行ocr', 
                    'event':'movein', 
                    'object_types':['File'],
                    'extensions':"jpg, tiff, png, pdf, gif, jpeg, tif",
                    'script':'root.call_code()'
                    })

event
---------
其中事件event包括:

- created: 容器中添加新对象，包括移入和创建
- removed：容器中去除对象，包括删除和移出
- modified: 对象被修改
- status-added: 对象新增某个状态
- status-removed: 对象去除某个状态
- movein: 这个是移入的事件
- moveout: 这个是移出的事件
- clock: 站点定时时间
- login: 登陆站点
- workflow-executed: 工作流执行某步骤
- file-downloaded: 下载

条件
-------------------
现在仅仅支持2种内置的条件判断：

- object_types: 事件关联对象的类型
- extensions：限于文件对象，且文件扩展名为指定的之一

script
----------------
运行的脚本，这个脚本可以使用的参数包括:

规则执行的操作，可以是脚本的形式，这里的脚本，可以使用的全局参数包括：

- root: 站点对象
- context: 发生事件的对象
- container：规则绑定的容器
- event: 具体事件定义
- request：请求

规则的分配
==============

查看已经分配的规则::

   context.rules.list_assignments()

分配规则::

   context.rules.assign(rule_ids, enabled=True, bubbles=False)

变更规则::

   context.rules.update(rule_id, enabled=True, bubbles=False)

去除一个柜子::

   context.rules.remove(rule_id)

