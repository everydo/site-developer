---
title: 阶段
description: 用户自定义的状态
---

================
阶段
================

.. contents::

阶段是用户自定义的状态，可以给表单定义不同的阶段.

阶段是对状态机的描述
===============================
典型的状态机::

   阶段A ----> 阶段B

定义阶段
============
采用json格式来描述阶段::

  stages = {"valid": {"title":"需求确认",
                      "description": "分配的新单",
                      "color":"1221", # 文字颜色
                      "bgcolor":"2222", # 背景的颜色
                      "on_enter": "" # 进入阶段的触发脚本
                     }
            "initial": {"title":"初始"}
           }

注册一个阶段定义::

   IStages(root).register('query', package="zopen.sales", stages)

调整阶段
=========================
使用IStateMachine来设置某个阶段::

   IStateMachine(item).set_state('stage.valid', do_check=False)

