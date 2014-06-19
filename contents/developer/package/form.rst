---
title: 表单处理
description: 自动生成表单、合法性校验，数据存储等
---

==================
表单处理
==================

.. Contents::
.. sectnum::


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
    "template": "", # 生成表单的模板
    'on_validation': "", # 字段正确性整体校验脚本
    "fields" : [     # 所有的字段
            {"name":"title",
              "type":"TextLine", 
              "title":'任务标题', },
            { "name":'description': 
               "type": "Text",       
               "title":'任务说明',      
               "rows":3, },
            {"name": "start,
              "type": "Date",
              "title": '开始时间',},
            { "name":"end",
              "type": "Date",
              "title": '结束时间',},
            { "name":"level",
              "type": "Integer",
              "title": '任务等级',
              "size":18,},
            {"name":"responsibles",
             "type":"PersonSelect",
             "title":'负责人', 
             "validation_expr":"not value and '需要一名检查人'",
            } 
            {"name":"reviewers",
             "type":"GrowingTable",
             "title":'审核人', 
             "fields": [
                {"name":"title",
                 "type":"TextLine"
                 "title""标题",
                },
                {"name":"description",
                 "type":"Text"
                 "title""描述",
                },
             ]
            } 
        ],
    }

表单由各种字段组成:

- Integer: 整数
- Text: 文本框
- TextLine: 单行文本框
- FixedPoint: 小数
- Password: 密码
- FileSelect: 文件选择

  如果初始值设置为 ``get_references()`` 就可以正确关联

- Select: 选择框，如果multiple=True多选，否则单选
- Boolean: bool字段
- Lines: 多行字段, list
- Date: 日期字段
- GrowingTable: 动态表格字段，有几个特殊参数可以利用:

  - row_index : 当前是第几行
  - this_row : 当前行的数据，是一个dict ，{field_name:value}

- PersonSelect: 人员选择
- LocationSelect: 位置选择(可选择站点任何一个地方)
- FolderSelect: 文件夹选择
- DataItemSelect: 表单选择
- DataContainerSelect: 表单容器选择 
- TagSelect: 标签组选择
- Computed: 公式字段

字段属性
-----------------
所有字段都公有的属性：

- name: 字段名
- title: 字段名称
- description: 说明帮助信息
- storage: user: 分用户存储；field: 普通存储
- default_value_exp: 默认值, 表达式
- read_condition: 查看条件, 表达式
- write_condition: 修改条件, 表达式
- required: 是否必填

一些特有：

- multiple: 是否允许多值
- vocabulary_exp
- size
- precision
- showtime
- minutestep
- rows
- cols
- rich_text
- upload: 是否允许上传
- container_exp
- is_global
- search_subtree
- addrow_condition
- delrow_condition
- mutiple
- selectable_object: persononly, persongroup, grouponly
- metadata
- show_info

on_validation脚本
----------------------------
用于校验表单提交值是否合法, 用于多个输入项联合校验::

   on_validation(fields, values, **options)

- fields: 本次提交可输入的字段
- values: 实际得到的值

其他的参数(options)，通常包括

- context: 是当前操作的对象
- container: 是当前对象context所在的容器对象，比如文件夹或者数据管理器。
- request: 请求对象

返回值:

- 如果表单提交数据校验正常，不返回任何值; 
- 如果表单字段校验有问题，可返回错误字段的错误信息，比如::

      {'title':'can not be empty',
       'age':'must greater than '
      }

- 注意，仅仅这些表单是可输入项的时候，这些错误信息才能显示。如果错误信息和输入项无关，可这样返回::

      {'':'something wrong！'}

注册
-----
可以注册一个表单语义，用于数据项、数据容器，或者应用容器::

  root.packages.register_metadata('zopen.sales', form_def)
  

使用表单
==================

得到表单
----------------
如果有需要使用数据容器的关联的表单定义::

  metadata = data_container.settings['item_metadata'][0]
  form_json = root.packages.get_metadata( metadata)

表单生成和处理
------------------
最简单的渲染表单方法::

  form = ui.form(action='', title='', description='')\
                .fields(form_json['fields'])\
                .action('form.save', '保存')\
                .kss('@zopen.sales:test')
  html = form.html()

用户提交表单，这时候可以对提交表单数据处理（原始数据放在 ``request_form`` 中)::

  errors, results = form.submit(request_form=request.form)

如果正确提交，errors为空，可以得到提交的结果数据存放在results。

如果发现错误, 需要提示用户重新提交::

  form = ui.form(action='', title='', description='')\
                .fields(form_json['fields'], reqeust.form, errors=errors)\
                .action('form.save', '保存')\
                .kss('@zopen.sales:test')
  html = form.html()

``ui.form.fields`` 完整API::

    ui.form().fields(fields_json, data={}, template=None, edit_fields=None, omit_fields=(), errors={}, **options):

- data: 存放各字段初始值
- edit_fields 需要编辑的字段，如果不是编辑字段，则自动渲染为只读形式
- omit_fields 表单中需要忽略的字段
- errors 各字段的错误信息
- template: 个性化的模板
- options: 动态计算需要的额外参数

``ui.form.submit`` 完整API::

    errors, result = form.submit(request_form, fields=None, check_required=True, pid=None, **options)

- fields: 仅仅处理那几个字段
- check_required: 是否需要判断必填条件
- pid：如果有需要分用户存储字段，这个是当前用户id
- options: 动态计算需要的额外参数

这个result可以加入到一个数据容器中::

    datacontainer.add_dataitem(result, name='', request=request):

获取表单的默认值
-----------------------
得到表单的初始值::

  results = form.get_defaults()

完整API::

  form.get_defaults(fields=None, **options)

- fields: 需要计算初始值的字段
- options：计算初始值需要的额外参数

平台相关的表单
=================
系统的表单在如下地方：

- 数据容器的设置 
- 数据容器的表单
- 应用容器的设置

由于使用场景的特殊性，有一些额外的属性：

- object_types':['DataItem'], # 语义定义用于的对象类型
- tag_groups: 所在容器的标签组设置
- on_save": 表单保存的时候, 会触发调用on_update, 这个方法和on_validatation脚本类似。但是调用这个参数的时候，对象数据保存了。
- related_workflow':'zopen.sales:sales' 这个表单关联的流程定义
- realted_datacontainer':'zopen.sales:sales_container',  关联的容器设置
- related_stage':'zopen.sales:sales', 关联的阶段定义

软件包文件
====================
可以导出导入为一个python文件::

  root.packages.export_metadata('zopen.sales:inquery')

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

  root.packages.import_metadata('zopen.sales:inquery', metadata_file_conent)

