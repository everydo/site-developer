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
  form_def = [ {"name":"title"
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

  # 注册为一个表单
  form_reg = root.get_form_registry()
  form_reg.register(name, package, form_def, title, layout, table_columns, on_update)
  form_def = form_reg.get('inquery')
  form_def = form_reg.get('inquery', package="zopen.sales")

  # 注册为表单设置项
  settings_reg = root.get_settings_registry()
  settings_reg.register(name, package, form_def, title, layout, on_update)
  form_def = settings_reg.get('inquery')
  form_def = settings_reg.get('inquery', package='zopen.sales')

  # 注册为软件包的一个属性集
  mdset_reg = root.get_mdset_registry()
  mdset_reg.register_mdset(name, package, form_def, title, layout, on_update)
  form_def = mdset_reg.get_mdset('default')
  form_def = mdset_reg.get_mdset('default', package='zopen.sales')

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
  template = form_def.gen_template('table')

  # 渲染表单
  html_form = form_def.html(template, {'description':'请说清楚'}, fields.keys(), errors)

其中::

  html(form_template, storage, edit_fields, errors, **options)

生成表单函数

- form_template 生成表单的模板
- storage 生成表单时需要运行某些表达式，而storage则是表达式运行的上下文, 这里可以存放初始值
- request HTTP请求对象，同样作为表达式执行时的对象
- edit_fields 需要编辑的字段，如果不是编辑字段，则自动渲染为只读形式
- errors 表单提交错误
- options 为执行表达式时提供额外的变量

为了计算初始值，需要传入更多变量::

  html_form = form_def.html(template, {}, fields.keys(), errors,
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

表单管理器 datacontainer
=========================
易度的表单管理器，是一个定制的容器对象，可以做到完全傻瓜化的表单数据管理，有如下设置信息::

   datacontainer.schemas = ('zopen.sales.query_container',)  # 容器自身的设置信息
   IMetadata(datacontainer).set_setting('item_schemas', ('zopen.sales.query',))   # 包含条目的表单定义
   IMetadata(datacontainer).set_setting('table_columns', ('title', 'description')) : 显示哪些列(list)

   IMetadata(datacontainer).set_setting('item_mdsets', ('archive_archive', 'zopen.contract.contract')) : 表单属性集(list)
   IMetadata(datacontainer).set_setting('item_stages', ('zopen.sales.query',)): 容器的阶段定义(list)
   IMetadata(datacontainer).set_setting('item_workflows', ('zopen.sales.query',)): 容器的工作流定义(list)

我们先看看一个个性化定制表单的使用示例。对于易度外网中的一个客户调查信息表，在完成表单和流程定制部署后，可创建如下的Python脚本，部署到外网用于收集客户资料::

  form_names = IMetadata(container).get_setting('item_schemas')
  form_def = root.get_form_definition(form_names)

  template = form_def.gen_template('div')

  form_html = """
      <h1>易度客户调查表</h1>
      <p>您好！感谢您填写此调查表，请务必真实的告知贵公司的需求，以便我们为您提供一个适合您的方案。</p>
      <form method="post">
      %s
      <input type="hidden" name="form.submitted" value="1" />
      """ 

  if not request.has_key('form.submitted'):
      return form_html % form_def.html(template, context=context, container=container)
  else:
      result, errors = form_def.submit(request, context=context, container=container)
      if errors:
          return form_html % form_def.html(template, request, result, errors, context=context, container=container)
      else:
          IMetadata(context).update(result)
          return "谢谢！"

