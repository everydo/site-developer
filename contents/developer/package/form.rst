---
title: 表单
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
  

使用表单
==================

得到表单
----------------
直接通过json得到表单定义::

  form = init_form(form_json)

如果有需要使用数据容器的关联的表单定义::

  schema = data_container.get_setting('item_schema')[0]
  form = root.packages.get_schema_obj( schema )

表单生成和处理
------------------
最简单的渲染表单方法::

  html_form = form.render()

用户提交表单，这时候可以对提交表单数据处理（原始数据放在 ``request_form`` 中)::

  errors, results = form.submit(request_form=request.form)

如果正确提交，errors为空，可以得到提交的结果数据存放在results。

如果发现错误, 需要提示用户重新提交::

  html_form = form.render(request.form, errors=errors)

``form.render`` 完整API::

    form.render(data={}, template=None, edit_fields=None, errors={}, **options):

- data: 存放各字段初始值
- edit_fields 需要编辑的字段，如果不是编辑字段，则自动渲染为只读形式
- errors 各字段的错误信息
- template: 个性化的模板
- options: 动态计算需要的额外参数

``form.submit`` 完整API::

    errors, result = form.submit(request_form, fields=None, check_required=True, pid=None, **options)

- fields: 仅仅处理那几个字段
- check_required: 是否需要判断必填条件
- pid：如果有需要分用户存储字段，这个是当前用户id
- options: 动态计算需要的额外参数

获取表单的默认值
-----------------------
得到表单的初始值::

  results = form.get_defaults()

完整API::

  form.get_defaults(fields=None, **options)

- fields: 需要计算初始值的字段
- options：计算初始值需要的额外参数

软件包文件
====================
可以导出导入为一个python文件::

  root.packages.export_schema('zopen.sales:inquery')

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

  root.packages.import_schema('zopen.sales:inquery', schema_file_conent)

