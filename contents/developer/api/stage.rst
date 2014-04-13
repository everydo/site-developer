---
title: 阶段
description: 用户自定义的状态
---

================
阶段
================

.. contents::

阶段是用户自定义的状态，可以给表单定义不同的阶段.

定义阶段
============
采用json格式来描述阶段::

  stages = {"valid": {"title":"需求确认",
                      "description": "分配的新单",
                      "on_enter": "" # 进入阶段的触发脚本
                     }
            "initial": {"title":"初始"}
           }

注册一个阶段定义::

   IStageDefinition(root).register('query', package="zopen.sales")

将表单和阶段关联
=========================
在容器里面设置阶段定义::

   IMetadata(collection).set_etc('childern_stage', ('zopen.sales:query',))

调整阶段
=========================
使用IStateMachine来设置某个阶段::

   IStateMachine(item).set_state('stage.valid', do_check=False)

