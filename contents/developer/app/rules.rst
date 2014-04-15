---
title: 规则
description: 如何在文件夹和流程中使用规则进行个性化定制
---

============
规则
============

通过规则，可以指定在某个位置，发生某种事件，满足一定条件时，执行一些特殊的操作。

在界面中使用
======================
分2步：

1. 系统管理员，在系统设置中定义规则

   规则定义了发生事件，满足的条件和执行的操作。

2. 文件夹、流程的管理员，使用规则

   将预先定义的规则，绑定到文件夹或者流程。

在规则中编写脚本
===========================

规则执行的操作，可以是脚本的形式，这里的脚本，可以使用的全局参数包括：

- context: 发生事件的对象
- container：规则绑定的容器
- event: 具体事件定义
- request：请求

其中事件event包括:

- modified
- created: 容器中添加新对象，包括移入和创建
- removed：容器中去除对象，包括删除和移出
- status-added
- status-removed
- moved
- clock
- login
- workflow-executed: 工作流执行某步骤

相关接口
==============
IRuleStorage
--------------

添加规则：

- addRule()::

    def addRule(self, title, event, description=u'', enabled=True, stop=False, name=False, aoth=True, features=[]):

IRuleAssignmentManager
-------------------------

用于分配规则

