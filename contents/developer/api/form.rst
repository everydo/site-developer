---
title: 表单和流程
description: 表单和流程操作接口，包括表单自动生成
---

=================
表单和流程
=================

可以定义一个简单的表单，也可以将这个表单附加一个操作流程。

.. Contents::
.. sectnum::

表单的定义
================
表单包括一组字段。

IntegerField,
------------------------
整数

TextField,
------------------
文本框

TextLineField,
----------------------
单行文本框

FixedPointField
-------------------------------
小数

PasswordField
-----------------------------
密码

'FileSelectField': ReferenceField,
-----------------------------------------------
文件选择

'UploadField': FileField,
---------------------------------------
文件上传

SingleSelectField
-------------------------------------------
单选

MultipleSelectField
-----------------------------
多选

ComputedField
--------------------------
公式字段

BooleanField
-------------------------
bool字段

LinesField
----------------------
多行字段

DateField
---------------------
日期字段

GrowingTableField
---------------------------
动态表格字段，有几个特殊参数可以利用:

- row_index : 当前是第几行
- this_row : 当前行的数据，是一个dict ，{field_name:value}

PersonSelectField
---------------------------
人员选择

FolderSelectField
----------------------------
文件夹选择

流程定义
====================
每个流程包括一组设置，流程的步骤定义，以及阶段的定义。

流程关联
-----------------
如果发起关联流程，request里面会传入参数'__reference'，使用下面的api可以得到关联的一组对象::

  get_references()

表单保存触发
--------------
返回值
.............
如果表单提交数据校验正常，不返回任何值

如果表单字段校验有问题，可返回错误字段的错误信息，比如::

  {'title':'can not be empty',
   'age':'must greater than '
  }

注意，仅仅这些表单是可输入项的时候，这些错误信息才能显示。如果错误信息和输入项无关，可这样返回::

  {'':'something wrong！'}

上述错误信息会在表单头部显示

old_storage
.................
这保存了表单提交直接存储的数据, 用途：

- 比较数据变化差异，可以记录到日志里面去::

   if old_storage['description'] != context['description']:
      log('description changed')

- 可以判断是否是初始提交，这时候old_storage里面应该是空的::

   if not old_strage: 
      xxx

- 比如可以判断，任务负责人有没有更改，如果更改，需要发送通知邮件


数据管理器
=============
也就是流程单是数据的管理器，接口比较丰富，也是经常在个性化表单定制中需要使用。

我们先看看一个个性化定制表单的使用示例。对于易度外网中的一个客户调查信息表，在完成表单和流程定制部署后，可创建如下的Python脚本，部署到外网用于收集客户资料::

 def renderForm(form, actions):
    return """
    <h1>易度客户调查表</h1>
    <p>您好！感谢您填写此调查表，请务必真实的告知贵公司的需求，以便我们为您提供一个适合您的方案。</p>
    <form method="post">
    %s %s
    <input type="hidden" name="form.submitted" value="1" />
    """ % (form, actions)

 template = context.getRelatedEdoClass().genTemplate(['salesman'])

 if not request.has_key('form.submitted'):
   form,actions = context.renderAddForm(request, {}, template)
   return renderForm(form, actions)
 else:
   errors, sheet = context.submitAddForm(request)
   if errors:
       form,actions = context.renderAddForm(request, errors,template )
       return renderForm(form, actions)
   else:
       return IFieldStorage(context)['finishtext']

详细API介绍如下:

部署表单和流程
--------------------------
``deployApplet(pkg_name, container, name=u'', title='', description='', permissions={})``
  - pkg_name: 分三种：

    - default.xxx : 内置的应用
    - xxx.xxx.workflows.xxx: 流程
    - xxx.xxx.objects.xxx: 表单

  - permissions: 部署后的初始授权 {role: pids}

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

表单处理
--------------
- renderAddForm(request, errors, template='') 	

  返回 form , actions

- submitAddForm(reqeust)	errors, item

- renderEditForm(item, request, errors, template='')	

  返回： form, ations

- ``renderDisplayForm(item, request, template='')``
- submitEditForm(item, request)

  返回： errors

- ``addDataItem(request, name='', **kw)``

  添加一个数据，kw是表单中的实际数据

流程执行	
------------------
- ``excuteStepAction(task, action_name, request, as_principal=None, comment="")``

  as_principal参数，可以指定以某人的身份去执行这个流程(如:users.admin)。一旦设定，系统将不检查该用户是否有流程步骤的执行权限

数据操作
-------------------------------
（这套api有问题，有新的！！！）

- def addRow(data = None):

  #data是对应的dict,如果不指定data，则创建空的dataitem

- ``def queryRows( as_storage=False, **filter):``
- ``def queryOneRow(**filter):``
- ``def updateRows( up_data, **filter):``
- ``def delRows( **filter):``

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

流程单多查看方式
===========================
只需使用特殊的python脚本命名前缀，就可实现流程单的多种查看方式。

对于表单的名字 foobar，命名方式为::

 view_foobar_xxx

其中xxx为真正的脚本名称。

如果需要改变默认的视图，只需要::

 IAppletData(flow_container).default_view = 'xxx_account.xxx_package.view_foobar_xxx'

表单生成
=========================
::

  # 定义字段
  fields = FieldsContainer(
    TextLineField(title=u'任务标题',),
    TextField(title=u'任务说明', rows=3),
    DateField(title=u'开始时间',),
    DateField(title=u'结束时间',),
    IntegerField(title=u'任务等级',size=18),
    PersonSelectField(title=u'检查人', validation_exp=u"not value and '需要一名检查人'",),
    MultipleSelectField(title=u'月',
                                        vocabulary_exp=u"[(str(i),str(i) + ' 号') for i in range(1,32)]",
                                        limit=40,
                                       )
  )

  # 生成默认模板
  template = fields.genTemplate() # 可传入表单样式 div/table

  # 渲染表单
  form =  IFormEngine(fields).genForm(template, {}, request, fields.keys(), errors,
                            context=context, container = container)

  # 保存表单
  results = {}
  errors = IFormEngine(fields).saveForm(fields.keys(), results, request, context=context, container=container)

其中:

``genForm(formtemplate, storage, request, edit_fields, errors, **options)``
        生成表单函数

        - formtemplate 生成表单的模板
        - storage 生成表单时需要运行某些表达式，而storage则是表达式运行的上下文, 这里可以存放初始值
        - request HTTP请求对象，同样作为表达式执行时的对象
        - edit_fields 需要编辑的字段，如果不是编辑字段，则自动渲染为只读形式
        - errors 表单提交错误
        - options 为执行表达式时提供额外的变量

``saveForm(fields, storage, request,  init=False, check_required=True, **options):``
        保存表单数据，返回errors信息

        - fileds 需要保存的字段，一个List
        - storage 数据会保存在这个dict接口对象中
        - request 执行统一校验的request变量
        - init: 是否把各个字段初始化

用户自定义流程
============================
软件包里面定义的流程，有最高的灵活性，但是普通用户不方便自定义。

可以借助流程设置，让用户自己类方便定义某些特殊流程，比如文档审批流程：

  http://www.edodocs.com/tour/doc-flow.rst

系统能够对自定义流程自动绘制流程图，并提供一组api来简化自定义流程的设计，可参照下面的方法定义。

表单字段的约定
--------------
如果希望流程支持手工指定审核人，可在表单中增加字段：

- reviewers: 手工指定的审核人
- reviewers_optional: 可跳过的手工指定的审核人 

上述2个字段均为动态表格，包括2个子字段：

- step_name: 步骤名
- reviewer: 审核人 

这2个字段允许编辑的条件是::

        context['_next_step']['step_type'] in ['', '']

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
        """ 计算下一步的步骤信息, 并计入到dataitem['_next_step']中，
            初始化表单中需要手工指定的审核人字段。内容：

            step_info信息是流程设置step信息，并增加了负责人repsonsibles

            如果找不到审核人，则自动跳过

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

