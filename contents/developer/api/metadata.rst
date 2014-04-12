---
title: 属性/元数据
description: 对象可以自定义一组属性
---

==================
属性/元数据
==================

.. Contents::
.. sectnum::

对象属性/元数据
==============================================

IMetadata用来得到对象的各种属性，如基础标题、描述、分类，表单字段，以及扩展属性集等。

IMetadata统一和取代了已经过时的IExtendedMetatada, IFieldStorage和ISettings接口，使用更加简单.

基础属性
--------------------------------------

系统的所有对象，都包括一组标准的元数据，也就是所谓的都柏林核心元数据（这是一个图书馆元数据国际标准）::

  IMetadata(obj)['title'] 对象的标题
  IMetadata(obj)['description'] 对象的描述信息
  IMetadata(obj)['subjects'] 关键字，分类
  IMetadata(obj)['identifier'] 这个也就是文件的编号
  IMetadata(obj)['creators'] 对象的创建人 注意，这是个list类型的对象
  IMetadata(obj)['contributors'] 参与人，贡献人
  IMetadata(obj)['created'] 对象的创建时间
  IMetadata(obj)['modified'] 对象的修改时间
  IMetadata(obj)['expires'] 对象的失效时间
  IMetadata(obj)['effective'] 对象的生效时间

表单定义属性
------------------
基础元数据无需定义表单，系统自动维护。也可用通过表单定义，来增加对象属性.

对于需要在日历上显示的对象，有如下表单字段::

  IMetadata(obj)['responsibles'] 负责人
  IMetadata(obj)['start'] 开始时间 
  IMetadata(obj)['end'] 结束时间

对于联系人类型的对象，通常可以有如下表单字段::

  IMetadata(obj)['email'] 邮件
  IMetadata(obj)['mobile'] 手机

经费相关的字段::

  IMetadata(obj)['amount'] 

数量相关的字段::

  IMetadata(obj)['quantity']

对于地理位置对象，通常有如下字段::

  IMetadata(obj)['longitude'] #经度
  IMetadata(obj)['latitude'] # 纬度


属性集
---------------

为了避免命名冲突，可以定义属性集(mdset: metadata set)，来扩展一组属性。

使用星号，可以直接读取一组属性集，下面返回用户自定义的档案管理archive属性集的所有内容（一个字典）::

  IMetadata(obj).new_mdset('archive')
  IMetadata(obj).get_mdset('archive')
  IMetadata(obj).set_mdset('archive', {})
  IMetadata(obj).remove_mdset('archive')
  IMetadata(obj).list_mdsets()  # 返回： [archive, ]

得到其中的一个字段值::

  IMetadata(obj).get_mdset('archive')['archive_number']

属性的快捷访问
---------------------------
如果obj表单，那更简单的写法是::

    obj['title']

生成表单
=========================
为了方便用户录入数据，需要为上述属性制作维护表单

定义表单
---------------
可以通过代码来定义一个表单::

  # 定义字段
  form_def = FormDefinition(
    TextLineField(name='title', title=u'任务标题',),
    TextField(name='description', title=u'任务说明', rows=3),
    DateField(name="start", title=u'开始时间',),
    DateField(name="end", title=u'结束时间',),
    IntegerField(name="level", title=u'任务等级', size=18),
    PersonSelectField(name="responsible", title=u'负责人人', 
                validation_exp=u"not value and '需要一名检查人'",),
    PersonSelectField(name="reviewer", title=u'检查人', 
                validation_exp=u"not value and '需要一名检查人'",),
  )

表单由各种字段组成:

- IntegerField : 整数
- TextField : 文本框
- TextLineField : 单行文本框
- FixedPointField : 小数
- PasswordField : 密码
- ReferenceField : 文件选择
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

也可以在如下途径来定义表单::

  # 软件包中的表单定义
  form_def = root.get_form_definition('inquery')
  form_def = root.get_form_definition('zopen.sales:inquery')
  # 软件包中的表单定义设置
  form_def = root.get_setting_definition('inquery')
  form_def = root.get_setting_definition('zopen.sales:inquery')
  # 软件包中的属性定义
  form_def = root.get_metadata_definition('default')
  form_def = root.get_metadata_definition('zopen.sales:default')

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

表单管理器
=========================
易度的表单管理器，是一个定制的容器对象，可以做到完全傻瓜化的表单数据管理::

   IMetadata(collection).get_mdset('_settings')

这里存放如下信息：

- form: 表单定义(list)
- form_mdsets: 表单属性集(list)
- table_columns: 显示哪些列(list)
- setting: 设置项表单(list)
- container_mdsets: 容器的扩展属性(list)

我们先看看一个个性化定制表单的使用示例。对于易度外网中的一个客户调查信息表，在完成表单和流程定制部署后，可创建如下的Python脚本，部署到外网用于收集客户资料::


  form_name = IMetadata(container).get_mdset('_settings')['form']
  form_def = root.get_form_definition(form_name)

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

