---
title: 表单和属性集
description: 自动生成表单、合法性校验，数据存储等
---

==================
表单处理
==================

.. Contents::
.. sectnum::

表单也用在如下地方：

- 流程容器设置 
- 流程表单
- 应用容器的设置
- 属性集

注册表单定义
================

可以将表单定义，注册保存到系统.

json定义表单
----------------
表单可以通过json来定义表单::

  form_def = {
    "name":'sales',
    "title":'',
    "description":'',
    "tag_groups":"", # 标签组设置
    'object_types':['DataItem'], # 语义定义用于的对象类型
    "on_update": "", # 保存的时候调用，用于校验等
    "template": "", # 生成表单的模板
     'related_workflow':'zopen.sales:sales',   # 关联的流程定义
     'realted_datacontainer':'zopen.sales:sales_container',  # 关联的容器设置
     'related_stage':'zopen.sales:sales', # 关联的阶段定义
    "fields" : [     # 所有的字段
            {"name":"title",
              "type":"TextLineField", 
              "title":'任务标题', },
            { "name":'description': 
               "type": "TextField",       
               "title":'任务说明',      
               "rows":3, },
            {"name": "start,
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
            } ],
    }


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
-----------------------------------
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

注册
-----
可以注册一个表单语义，用于数据项、数据容器，或者应用容器::

  root.packages.register_schema('zopen.sales', form_def)
  
也可以注册成一个属性集::

  root.packages.register_mdset('zopen.sales', 
        {name:
         title:, 
         description:, 
         fields:,
         on_update:,
         template:,
         obejct_types})

使用表单
==================

得到表单
----------------
数据容器的设置信息中，保存了使用的表单信息::

  schema = data_container.get_setting('item_schema')[0]

可以得到注册的表单对象::

  form = root.packages.get_schema_obj( schema[0] )

也可以得到一个属性集表单对象::

  form = root.packages.get_mdset_obj( mdset_name )

根据取出表单定义::

  form = Form().import(form_json)

生成表单html
------------------
::

  # 渲染表单
  html_form = form.render_html({'description':'请说清楚'}, fields.keys(), errors)

其中::

  render_html(storage, edit_fields, errors, **options)

生成表单函数

- storage 生成表单时需要运行某些表达式，而storage则是表达式运行的上下文, 这里可以存放初始值
- edit_fields 需要编辑的字段，如果不是编辑字段，则自动渲染为只读形式
- errors 表单提交错误

为了计算初始值，需要传入更多变量::

  html_form = form.render_html(template, {}, fields.keys(), errors,
                            request, context=context, container = container)

- request HTTP请求对象，同样作为表达式执行时的对象
- context
- container

保存结果
--------------
用于输入合法性校验，和更改时候的触发逻辑，数据存放到results中::

  results = {}
  errors = form.save(results, values=requrest.form)

完整定义::

  form.save(storage, values, fields=None, init=False, check_required=True, **options):``

- storage 数据会保存在这个dict接口对象中
- fileds 需要保存的字段，一个List
- init: 是否把各个字段初始化


软件包文件
====================
可以导出导入为一个python文件::

  root.packages.export_datatitem('zopen.sales:inquery')

示例如下::

    #-*-encoding=utf-8-*-
    title="销售机会"
    description="""这是销售机会的解释"""
    extend = 'zopen.sales:chance'  # 继承的表单定义
    displayed_columns=['responsibles', '_stage', 'client', 'start', 'lastlog']
    facetag = ""
    related_workflow = 'zopen.sales:sales'
    related_datacontainer = 'zopen.sales:sales'
    related_stage = 'zopen.sales:sales'

    fields = [ {"name":"title"
              "type":"TextLineField", 
            required=False,
            storage='field',
            validation_exp='',
            write_condition='',
            read_condition='',
            size=30,
            default_value_exp='""',
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
            description='一句话说明销售的内容',
            title='机会简述',
            name='title'
    ),

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

  root.packages.import_dataitem('zopen.sales:inquery', schema_file_conent)

