---
title: 表单
description: 对象可以自定义一组属性，通过表单来更新属性
---

==================
表单
==================

.. Contents::
.. sectnum::

生成表单
=========================
为了方便用户录入数据，通过定义表单来录入数据。

定义表单
---------------
可以通过json来定义一个表单::

  # 定义字段
  schema = [ {"name":"title"
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
             "validation_expi":"not value and '需要一名检查人'",
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

可以将表单定义，注册保存到系统::

  # 注册为一个语义
  ISchemas(root).register('zopen.sales:inquery', schema, title, layout, table_columns, on_update)
  ISchemas(root).get('zopen.salesinquery')

on_update 表单保存触发
--------------------------------
用于输入合法性校验，和更改时候的触发逻辑

参数:

- context: 是当前操作的对象
- container: 是当前对象context所在的容器对象，比如文件夹或者数据管理器。
- old_storage: 这保存了表单提交直接存储的数据

返回值:

如果表单提交数据校验正常，不返回任何值; 
如果表单字段校验有问题，可返回错误字段的错误信息，比如::

  {'title':'can not be empty',
   'age':'must greater than '
  }

注意，仅仅这些表单是可输入项的时候，这些错误信息才能显示。如果错误信息和输入项无关，可这样返回::

  {'':'something wrong！'}

上述错误信息会在表单头部显示

生成表单html
------------------------
::

  # 生成默认模板, 可传入表单布局 div/table
  template = schema.gen_template('table')

  # 渲染表单
  html_form = schema.render_html(template, {'description':'请说清楚'}, fields.keys(), errors)

其中::

  render_html(form_template, storage, edit_fields, errors, **options)

生成表单函数

- form_template 生成表单的模板
- storage 生成表单时需要运行某些表达式，而storage则是表达式运行的上下文, 这里可以存放初始值
- request HTTP请求对象，同样作为表达式执行时的对象
- edit_fields 需要编辑的字段，如果不是编辑字段，则自动渲染为只读形式
- errors 表单提交错误
- options 为执行表达式时提供额外的变量

为了计算初始值，需要传入更多变量::

  html_form = schema.render_html(template, {}, fields.keys(), errors,
                            request, context=context, container = container)

在浏览器上渲染表单
--------------------------
现在时兴web app，可以分别传回表单的模板和数据，供渲染.

gen_template生成的模板为handlerbar格式的模板。

提交表单
-------------------
提交表单还需要对表单值进行校验::

  # 保存表单
  results, errors = form.submit(request)

返回表单数据，和errors信息. 完整定义::

  submit(request, fields=None, init=False, check_required=True, **options):``

- storage 数据会保存在这个dict接口对象中
- request 执行统一校验的request变量
- fileds 需要保存的字段，一个List
- init: 是否把各个字段初始化

可以将results直接保存到主属性中::

  IMedata(obj).update(result)

或者保存到mdset中::

  IMedata(obj).set_mdset('lala', results)

