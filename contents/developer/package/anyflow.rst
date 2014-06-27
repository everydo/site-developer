---
title: 自定义流程
description: 如果定制一个可高度自定义的流程
---

============================
用户自定义流程
============================
软件包里面定义的流程，有最高的灵活性，但是普通用户不方便自定义。

可以借助流程设置，让用户自己类方便定义某些特殊流程，比如 `文档审批流程 <http://www.edodocs.com/tour/doc-flow.rst>`__

系统能够对自定义流程自动绘制流程图，并提供一组api来简化自定义流程的设计，可参照下面的方法定义。

表单字段的约定
====================
如果希望流程支持手工指定审核人，可在表单中增加字段：

- reviewers: 手工指定的审核人, 编辑条件是context.get('_next_step') and context['_next_step']['can_assign']
- reviewers_optional: 可跳过的手工指定的审核人, 编辑条件是context.get('_next_step') and context['_next_step']['can_assign_optional'] 

上述2个字段均为动态表格，包括2个子字段：

- step_name: 步骤名
- reviewer: 审核人 

示例如下::

    GrowingTableField(
        description=u'文档的手工指定(必填)审核人表',
        title=u'审核人',
        required=False,
        storage=u'field',
        addrow_condition=u"False",
        delrow_condition=u"False",
        read_condition=u"False",
        write_condition=u"context['_next_step'] and context['_next_step']['can_assign']",
        default_value_exp=u"",
        name=u'reviewers',
        fields=[
            TextLineField(
                title=u'步骤名称',
                required=False,
                storage=u'field',
                size=20,
                write_condition=u"False",
                default_value_exp=u'""',
                name=u'step_name'
        ),
            PersonSelectField(
                title=u'审核人',
                required=False,
                storage=u'field',
                selectable_object=u'persononly',
                multiple_selection=True,
                default_value_exp=u'PersistentList([])',
                name=u'reviewer'
        ),
        ]
    ),
    GrowingTableField(
        description=u'文档的手工指定(可选)审核人表',
        title=u'可选审核人',
        required=False,
        storage=u'field',
        addrow_condition=u"False",
        delrow_condition=u"False",
        read_condition=u"False",
        write_condition=u"context['_next_step'] and context['_next_step']['can_assign_optional']",
        default_value_exp=u"''",
        name=u'reviewers_optional',
        fields=[
            TextLineField(
                title=u'步骤名称',
                required=False,
                storage=u'field',
                size=20,
                write_condition=u"False",
                default_value_exp=u'""',
                name=u'step_name'
        ),
            PersonSelectField(
                title=u'审核人',
                required=False,
                storage=u'field',
                selectable_object=u'persononly',
                multiple_selection=True,
                default_value_exp=u'PersistentList([])',
                name=u'reviewer'
        ),
        ]
    ),

流程设置的约定
=========================
设置需要包括2个字段

自定义步骤: steps
----------------------
这是一个动态表格

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


具体::

    GrowingTableField(
        title=u'审批步骤',
        description=u'依次填写全部审批步骤，各步骤的审批人，多个审批人并审时审批通过的条件，以及审批人审批时对文档拥有的权限',
        required=False,
        storage=u'field',
        default_value_exp=u'PersistentList([])',
        name=u'steps' ,

        fields=[
        TextLineField(
            title=u'步骤名称',
            required=False,
            storage=u'field',
            size=20,
            default_value_exp=u'""',
            name=u'title'
            ),
        SingleSelectField(
            title=u'步骤类型',
            required=False,
            storage=u'field',
            vocabulary_exp=u"[('review', '审批'), ('inform', '知会'), ('assign_reviewer', '指定审核人')]",
            limit=1,
            default_value_exp=u'"review"',
            name=u'step_type'
            ),
        SingleSelectField(
            title=u'审批人',
            required=False,
            storage=u'field',
            vocabulary_exp=u"[('review_table', '查审批人表'), ('creators', '提交人'), ('handwork', '手工指定(必填)'), ('handwork_optional', '手工指定(可选)')]",
            limit=4,
            default_value_exp=u'"review_table"',
            name=u'responsible'
            ),
        SingleSelectField(
            title=u'有多个审批人?',
            required=False,
            storage=u'field',
            vocabulary_exp=u"[('any', '一个通过就行'), ('all', '必须全部通过')]",
            limit=4,
            default_value_exp=u'"all"',
            name=u'condition'
            ), ]
    ) ,

审核人表：reviewers
---------------------------
::

    GrowingTableField(
        title=u'审批人表',
        description=u'根据提交人所在的范围，确定步骤的审批人。同一步骤，可设置多行审批人: 不同审批人，负责审批不同的部门',
        required=False,
        storage=u'field',
        default_value_exp=u'PersistentList([])',
        name=u'reviewers' ,
        fields=[
            TextLineField(
                title=u'步骤名称',
                required=False,
                storage=u'field',
                size=20,
                default_value_exp=u'""',
                name=u'step'
        ),
            PersonSelectField(
                title=u'审批人',
                required=False,
                storage=u'field',
                selectable_object=u'persononly',
                multiple_selection=True,
                default_value_exp=u'PersistentList([])',
                name=u'reviewer'
        ),
            PersonSelectField(
                description=u'',
                title=u'审批范围',
                required=False,
                storage=u'field',
                validation_exp=u'',
                write_condition=u'',
                selectable_object=u'persongroup',
                read_condition=u'',
                multiple_selection=True,
                default_value_exp=u'PersistentList([])',
                name=u'members'
        ) ]),

流程步骤定义的约定
============================
对步骤的基本定义要求：

自定义审核步骤的前一步
----------------------------
触发操作脚本, 计算下一步信息(存放在context['_next_step']中)::

     next_step = IUserDefinedSteps(container).calc_next_step(context)
     if next_step is None:
        pass 
        # TODO: 步骤完成的处理


典型的示例::

    FlowStep(
        name=u'submit',
        title=u'申请审批',
        fields=[u'doc', u'description'],
        invisible_fields=[u'review_comment', u'step', u'folder', u'tags', u'current_review_comment', u'reviewers', u'reviewers_optional'],
        responsibles=u"[request.principal.id] or context['creators']",
    actions=[
        FlowStepAction(
            name=u'提交',
            title=u'提交',
            nextsteps=[u'review'],
            finish_condition=u'',
            stage=u'pending',
            trigger=ur"""
            next_step = IUserDefinedSteps(container).calc_next_step(context,
                    get_responsible_script='zopen.review.get_responsible')
            if next_step is None:
                pass # TODO
            """
            )
    ])
    ,

自定义审核过程
---------------------
步骤名必须为review

- 步骤进入条件: ``context.get('_next_step')``

- 进入步骤触发脚本，设置当前任务的名称: ``task.title = context['_next_step']['title']``

- 执行人: ``context['_next_step']['responsibles']``

- 审核通过操作, 通过条件: ``IUserDefinedSteps(container).finish_condition(context, task, u'通过')``

- 操作触发脚本::

     if 'flowtask.finished' in task.stati:
        next_step = IUserDefinedSteps(container).calc_next_step(context,
            get_responsible_script='zopen.review.get_responsible')
        if next_step is None:
            pass
            # TODO 流程结束处理

示例::

    FlowStep(
        name=u'review',
        title=u'审批',
        condition="context.get('_next_step')",
        fields=[u'description', u'review_comment', u'reviewers', u'reviewers_optional', u'tags', u'folder'],
        invisible_fields=[u'current_review_comment'],
        responsibles=u"context['_next_step']['responsibles']",
        trigger=ur"""
        task.title = context['_next_step']['title']
        """,
    actions=[

        FlowStepAction(
            name=u'passed',
            title=u'通过',
            condition=u"context['_next_step'].get('step_type', 'review') in ('review', 'assign_reviewer')",
            nextsteps=[u'review'],
            finish_condition=u"IUserDefinedSteps(container).finish_condition(context, task, u'通过')",
            trigger=ur"""
            if error: return error
            if 'flowtask.finished' in task.stati:
                root.call_script('zopen.review:finish_step', task=task, context=context, request=request, container=container)
                next_step = IUserDefinedSteps(container).calc_next_step(context,
                    get_responsible_script='zopen.review.get_responsible')
                if next_step is None:
                    root.call_script('zopen.review:archive', context=context, request=request, container=container)
            """,
        ),

        FlowStepAction(
            name=u'rejected',
            title=u'打回',
            condition=u"context['_next_step'].get('step_type', 'review') in ('review', 'assign_reviewer')",
            nextsteps=[],
            finish_condition=u'',
            stage=u'rejected'
       ),

        # 如果步骤类型只是知会，则只能确认不能打回
        FlowStepAction(
            name=u'confirm',
            title=u'确认',
            condition=u"context['_next_step'].get('step_type') == 'inform'",
            finish_condition=u"IUserDefinedSteps(container).finish_condition(context, task, u'通过')",
            nextsteps=[u'review'],
            trigger=ur"""
                error = root.call_script('zopen.review:reviewing_passed', context=context, request=request, container=container)
                if error: return error
                if 'flowtask.finished' in task.stati:
                    root.call_script('zopen.review.finish_step', task=task, context=context, request=request, container=container)
                    next_step = IUserDefinedSteps(container).calc_next_step(context,
                        get_responsible_script='zopen.review.get_responsible')
                    if next_step is None:
                        root.call_scirpt('zopen.review.archive', context=context, request=request, container=container)
                """,
        )
    ])
    ,
    ]

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

