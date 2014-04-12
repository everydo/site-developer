---
title: 自定义流程
description: 如果定制一个可高度自定义的流程
---

============================
用户自定义流程
============================
软件包里面定义的流程，有最高的灵活性，但是普通用户不方便自定义。

可以借助流程设置，让用户自己类方便定义某些特殊流程，比如文档审批流程：

  http://www.edodocs.com/tour/doc-flow.rst

系统能够对自定义流程自动绘制流程图，并提供一组api来简化自定义流程的设计，可参照下面的方法定义。

表单字段的约定
--------------
如果希望流程支持手工指定审核人，可在表单中增加字段：

- reviewers: 手工指定的审核人, 编辑条件是context.get('_next_step') and context['_next_step']['can_assign']
- reviewers_optional: 可跳过的手工指定的审核人, 编辑条件是context.get('_next_step') and context['_next_step']['can_assign_optional'] 

上述2个字段均为动态表格，包括2个子字段：

- step_name: 步骤名
- reviewer: 审核人 

流程设置的约定
-------------------
- 自定义步骤的设置项为 steps (动态表格)

  - title: 步骤名
  - step_type: 类型

    - 审核
    - 指定审核人

  - responsible: 审核人查找方式

    - review_table: 查审核人表
    - creators: 创建人审核
    - handwork: 手工指定人，必填
    - handwork_optional: 手工指定人，可选。如果没有指定跳过该步骤

  - 通过条件 condition

- 审核人表为 reviewers

流程步骤定义的约定
--------------------------
对步骤的基本定义要求：

- 自定义审核过程的步骤名必须为review
- review步骤的前一步骤

  - 触发操作脚本

    计算下一步信息(存放在context['_next_step']中)::
    
     next_step = IUserDefinedSteps(container).calc_next_step(context,
            get_responsible_script='zopen.review.get_responsible')
     if next_step is None:
        pass 
        # TODO: 步骤完成的处理

  - 后续步骤条件::

      {"审核": not context.get('_next_step')}

- reviewer步骤

  - 触发脚本

    设置当前任务的名称::

        task.title = context['_next_step']['title']

  - 执行人::

        context['_next_step']['responsibles']

- reviewer步骤 -> 审核通过操作 

  - 通过条件::
 
        IUserDefinedSteps(container).finish_condition(context, task, u'通过')

  - 触发脚本::

     if 'flowtask.finished' in task.stati:
        next_step = IUserDefinedSteps(container).calc_next_step(context,
            get_responsible_script='zopen.review.get_responsible')
        if next_step is None:
            pass
            # TODO 流程结束处理

  - 后续步骤表达式::

     {'审批': not context.get('_next_step')}

IUserDefinedSteps接口说明
---------------------------
::

 IUserDefinedSteps(datamanager):
    """用户自定义工作流 """

    def verify():
        """ 检查是否是自定义流程 """

    def calc_next_step(dataitem, get_responsible_script=''): 
        """ 计算下一步的步骤信息, 包括审核人、是否需要指定后续审核人，并计入到dataitem['_next_step']中，详细见下节


        返回值：

            - 如果有下一步，就是 dataitem['_next_step']

            - 如果没有下一步流程，则返回None

        输入值：

            – Dataitem: 当前流程单
            – get_responsible_script: 如果默认的负责人查找方式找不到下一步的负责人会调用该脚本。

              脚本接受一个参数:查找方式,如’doc_reviewer’, ‘admin’
        """

    def finish_condition(dataitem, task, action_title) 
        """ 判断当前是否结束了 """

context['_next_step']的信息
--------------------------------------
calc_next_step方法会在context['_next_step']中存放下一步的步骤信息，包括：

- 流程设置中，步骤所在行的全部信息
- responsibles: 流程负责人
- can_assign：是否可指定必填的审核人
- can_assign_optional: 是否可指定可选的审核人

