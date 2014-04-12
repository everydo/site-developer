
---
title: 表单和流程
description: 表单和流程操作接口，包括表单自动生成
---

=================
流程
=================

可以定义一个简单的表单，也可以将这个表单附加一个操作流程。

.. Contents::
.. sectnum::

流程单任务
================
如果希望得到某个流程单的当前任务::

 IFlowTasksManager(sheet).listCurrentTasks

上面用到了IFlowTasksManager，接口说明如下:

- getLastTask(): 上一个完成的任务 
- listCurrentTasks(pid=None, state='flowtask.active'): 当前正在执行的任务对象
- clear(): 清除全部任务
- addTask(task): 添加任务
- getTask(name): 得到某个任务

数据管理器
=============
也就是流程单是数据的管理器，接口比较丰富，也是经常在个性化表单定制中需要使用。


详细API介绍如下:

流程和表单定义信息	
------------------------
- getFormDefinition()

  得到关联的表单定义	

- getFormDefinition().genTemplate(omitted_fields)

  生成个性化模版

- getWorkFlowDefinition

  得到关联的流程定义	

- getStagesDefinition

  得到关联的流程定义	

流程执行	
------------------
- ``excuteStepAction(task, action_name, request, as_principal=None, comment="")``

  as_principal参数，可以指定以某人的身份去执行这个流程(如:users.admin)。一旦设定，系统将不检查该用户是否有流程步骤的执行权限

流程多查看方式
-----------------------
只需使用特殊的python脚本命名前缀，就可实现流程单的多种查看方式。

对于表单的名字 foobar，命名方式为::

 view_foobar_xxx

其中xxx为真正的脚本名称。

如果需要改变默认的视图，只需要::

 IAppletData(flow_container).default_view = 'xxx_account.xxx_package.view_foobar_xxx'


数据项和可以和流程引擎结合:

  ITasksManager(item).listCurrentTasks


- getLastTask(): 上一个完成的任务 
- listCurrentTasks(pid=None, state='flowtask.active'): 当前正在执行的任务对象
- clear(): 清除全部任务
- addTask(task): 添加任务
- getTask(name): 得到某个任务

