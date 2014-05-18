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

  stages = {'name':'sales',
            'title':'Sales',
            'description':'Description',
            'stages': [{"name":"valid",
                        "title":"需求确认",
                        "description": "分配的新单",
                        "color":"1221", # 文字颜色
                        "bgcolor":"2222", # 背景的颜色
                        "on_enter": "" # 进入阶段的触发脚本
                       },
                       { "name":"initial",
                        "title":"初始"}
                      ]
           }

注册一个阶段定义::

   IPackages(root).register_stage('zopen.sales', stages)

导入导出
============
可以导出一个阶段定义到python文件中::

   IPackages(root).export('zopen.sales:query')

导出结果::

    class valid:
          """需求确认

          分配的新单"""

          # 进入阶段的触发脚本
          def __init__(context):
              pass

    class initial:
          """初始

          确认客户信息有效"""

    class planing:
          """准备方案

          确认客户信息有效"""
          
    class plan_accept:
          """准备合同

          客户已接受方案，进入合同谈判阶段"""

也可以导入::

   IPackages(root).import_stage('zopen.sales:query', body)

调整阶段
=========================
使用IStateMachine来设置某个阶段::

   IStateMachine(item).set_state('stage.valid', do_check=False)

