---
title: 表单处理
description: 自动生成表单、合法性校验，数据存储等
---

==================
表单处理
==================

.. Contents::
.. sectnum::

表单也用在如下地方：

- 流程设置
- 流程表单
- 应用容器的设置
- 属性集

表单如下信息构成::

  form = Form(fields=, # 字段
                on_update:'', # 更新触发脚本
                template =, # 字段的布局模板
                grid_columns, # 表格显示列
                factags # 标签组设置
                )

注册语义
====================
可以将表单定义，注册保存到系统::

  # 注册为一个语义
  ISchemas(root).register('zopen.sales:inquery', form)
  form = ISchemas(root).get('zopen.sales:inquery')

表单字段
---------------
可以通过json来定义表单字段::

  # 定义字段
  fields = [ {"name":"title"
                "type":"TextLineField", 
                "title":'任务标题', },
            { "name":'description': 
               "type": "TextField",       
               "title":'任务说明',      
               "rows":3, },
            {"name": "start": 
              "type": "DateField",
              "title": '开始时间',},
            { "name":"end",
              "type": "DateField",
              "title": '结束时间',},
           { "name":"level",
              "type": "IntegerField",
              "title": '任务等级',
              "size":18,},
           {"name":"responsibles",
             "type":"PersonSelectField",
             "title":'负责人人', 
             "validation_expr":"not value and '需要一名检查人'",
           } ]

表单由各种字段组成:

- IntegerField : 整数
- TextField : 文本框
- TextLineField : 单行文本框
- FixedPointField : 小数
- PasswordField : 密码
- ReferenceField : 文件选择

  如果初始值设置为 ``get_references()`` 就可以正确关联

- FileField  : 文件上传
- SingleSelectField : 单选
- MultipleSelectField : 多选
- BooleanField : bool字段
- LinesField : 多行字段, list
- DateField : 日期字段
- GrowingTableField: 动态表格字段，有几个特殊参数可以利用:

  - row_index : 当前是第几行
  - this_row : 当前行的数据，是一个dict ，{field_name:value}

- PersonSelectField : 人员选择
- FolderSelectField : 文件夹选择

- IntegerComputedField : 公式字段(整数)
- FloatComputedField : 公式字段(浮点)
- ListComputedField : 公式字段(多值)
- TextComputedField : 公式字段(文本)
- ReferenceComputedField : 公式字段(链接)

on_update脚本: 表单保存触发
--------------------------------
用于输入合法性校验，和更改时候的触发逻辑::

  form = ISchemas(root).get('zopen.sales:inquery')
  errors = form.save(IMedata(context), values={})
  errors = form.save(IMedata(context).get_mdset('archive'), values={})

调用save的时候，会自动调用on_update::

    def on_update(storage, values, **options)

- storage: 存储对象，可查看之前的旧的数据
- values: 新的数据
- options: 其他的参数，包括

  - context: 是当前操作的对象
  - container: 是当前对象context所在的容器对象，比如文件夹或者数据管理器。

返回值:

如果表单提交数据校验正常，不返回任何值; 
如果表单字段校验有问题，可返回错误字段的错误信息，比如::

  {'title':'can not be empty',
   'age':'must greater than '
  }

注意，仅仅这些表单是可输入项的时候，这些错误信息才能显示。如果错误信息和输入项无关，可这样返回::

  {'':'something wrong！'}

数据容器 DataContainer
===========================
数据容器是最简单的表单使用方法，只需要设置好数据容器对应的表单语义即可::

  IMetadata(data_container).set_setting('item_schemas', ('zopen.sales:query',))

这样就可以在系统后台进行任意的表单增删改操作。

表单使用
==================
生成表单html
------------------------
::

  # 渲染表单
  html_form = form.render_html(template='', {'description':'请说清楚'}, fields.keys(), errors)

其中::

  render_html(template, storage, edit_fields, errors, **options)

生成表单函数

- template: 生成表单的模板
- storage 生成表单时需要运行某些表达式，而storage则是表达式运行的上下文, 这里可以存放初始值
- request HTTP请求对象，同样作为表达式执行时的对象
- edit_fields 需要编辑的字段，如果不是编辑字段，则自动渲染为只读形式
- errors 表单提交错误
- options 为执行表达式时提供额外的变量

为了计算初始值，需要传入更多变量::

  html_form = form.render_html(template, {}, fields.keys(), errors,
                            request, context=context, container = container)

在浏览器上渲染表单
--------------------------
现在时兴web app，可以分别传回表单的模板和数据，供渲染.

gen_template生成的模板为handlerbar格式的模板。

提交表单
-------------------
提交表单还需要对表单值进行校验::

  # 保存表单
  errors = form.save(IMedata(context), values=requrest.form)

返回表单数据，和errors信息. 完整定义::

  form.save(storage, values, fields=None, init=False, check_required=True, **options):``

- storage 数据会保存在这个dict接口对象中
- fileds 需要保存的字段，一个List
- init: 是否把各个字段初始化

文件格式
---------------
可以导出导入为一个python文件::

  ISchemas(root).export('zopen.sales:inquery')


示例如下::

    #-*-encoding=utf-8-*-
    title="销售机会"
    description="""这是销售机会的解释"""
    extend = 'zopen.sales:chance'  # 继承的表单定义
    displayed_columns=['responsibles', '_stage', 'client', 'start', 'lastlog']
    facetag = ""

    fields=(
        TextLineField(
            description='一句话说明销售的内容',
            title='机会简述',
            required=False,
            storage='field',
            validation_exp='',
            write_condition='',
            read_condition='',
            size=30,
            default_value_exp='""',
            name='title'
    ),
        DataItemSelectField(
            container_exp='getParent(container)["orgs"]',
            multiple=True,
            description='',
            edoclass='zopen.contacts.org',
            title='客户',
            required=True,
            storage='field',
            validation_exp='',
            write_condition='',
            read_condition='',
            show_info='title',
            default_value_exp='PersistentList([])',
            name='client'
    ),
        ComputedField(
            description='',
            title='客户信息',
            required=False,
            storage='field',
            validation_exp='',
            write_condition='',
            read_condition='',
            default_value_exp="callScript(context,request, 'zopen.sales.client_info')",
            name='client_info'
    ),
        PersonSelectField(
            description='该销售机会的销售员',
            title='销售员',
            required=True,
            storage='field',
            validation_exp='',
            write_condition='',
            selectable_object='persononly',
            read_condition='',
            multiple_selection=False,
            default_value_exp='PersistentList([])',
            name='responsibles'
    ),
        TagSelectField(
            container_exp='container',
            description='',
            title='分类信息',
            required=False,
            storage='field',
            validation_exp='',
            write_condition='',
            read_condition='',
            default_value_exp='PersistentList([])',
            name='subjects'
    ),
        TextField(
            rows=5,
            description='',
            title='销售机会详情',
            required=False,
            storage='field',
            cols=10,
            validation_exp='',
            write_condition='',
            read_condition='',
            default_value_exp="ISettings(container)['template']",
            rich_text=False,
            name='case_info'
    ),
        TextField(
            rows=5,
            description='',
            title='报价方案',
            required=False,
            storage='field',
            cols=10,
            validation_exp='',
            write_condition='',
            read_condition='',
            default_value_exp='',
            rich_text=False,
            name='plan_info'
    ),
        ReferenceField(
            container_exp="context['folder'] is not None and intids.getObject(context['folder'])",
            is_global=False,
            multiple=True,
            description='',
            title='相关文档',
            required=False,
            storage='field',
            upload=True,
            validation_exp='',
            write_condition='',
            search_subtree=True,
            read_condition='',
            default_value_exp='PersistentList([])',
            name='files'
    ),
        FolderSelectField(
            is_global=True,
            description='',
            title='文件存放区',
            required=False,
            storage='field',
            validation_exp='',
            write_condition='',
            read_condition='',
            default_value_exp='ISettings(container).get("folder","")',
            name='folder'
    ),
        TextField(
            rows=5,
            description='',
            title='上次跟进',
            required=False,
            storage='field',
            cols=10,
            validation_exp='',
            write_condition='',
            read_condition='',
            default_value_exp='',
            rich_text=False,
            name='lastlog'
    ),
        TextField(
            rows=5,
            description='',
            title='跟进记录',
            required=False,
            storage='field',
            cols=10,
            validation_exp='',
            write_condition='',
            read_condition='',
            default_value_exp='',
            rich_text=False,
            name='log'
    ),
        DateField(
            minutestep=60,
            description='',
            title='下次跟进时间',
            showtime=True,
            required=True,
            storage='field',
            validation_exp='',
            write_condition='',
            read_condition='',
            default_value_exp='datetime.datetime(*(datetime.datetime.now() + datetime.timedelta(1)).timetuple()[:4])',
            name='start'
    ),)

    def on_update(context, container, old_context):
        # 如果有根据记录，做记录循环，并保存为评论
        log = (context['log'] or '').strip()
        if log:
            context['lastlog'] = log
            context['log'] = ''
            ICommentManager(context).addComment(log)

        if old_storage:
            for user_id in old_storage['responsibles']:
                IGrantManager(context).unsetRole('zopen.Editor',user_id)

        for user_id in context['responsibles']:
            IGrantManager(context).grantRole(r'zopen.Editor', user_id)

        # 如果下次跟进时间，小于当前时间，则将下次跟进时间改为当前时间+2天
        if context['start'] <= datetime.datetime.now():
            context['start']=datetime.datetime(*(datetime.datetime.now() + datetime.timedelta(2)).timetuple()[:4])

同样可以导入这样一个文件::

  ISchemas(root).import('zopen.sales:inquery', schema_file_conent)


