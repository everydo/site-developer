---
title: 索引和查询
description: 对象数据库和普通的关系数据库不一样，需要手工维护索引，目前还只支持内置的一组索引，不支持自定义索引。
---

=====================
对象索引和查询
=====================

.. contents::
.. sectnum::

对象的索引
============================================

有多少种索引，就能从多少角度搜索：

对象内置属性的索引
-------------------------------
系统自动维护的索引

可直接按照下列字段进行搜索

- stati: 状态
- path, 路径，值是父对象intid的集合
- parent: 上一级对象的intid
- size, 大小
- file_content:     全文 
- references:   关联的文件
- allowed_principals:     授权的人
- disallowed_principals:  禁止的人
- object_provides: 对象提供的接口

  - 文档：zopen.content.interfaces.IFile / zopen.content.interfaces.IImage / zopen.content.interfaces.IDocument
  - 快捷方式：zopen.shortcut.interfaces.IShortCut
  - 文件夹：zopen.content.interfaces.IFolder
  - 表单：zopen.model.interfaces.IDataItem, 每个具体类系的表单 dataitem.xxx.xxx.xxx （如：dataitem.zopen.archive.borrow）
  - 表单容器：zopen.model.interfaces.IDataManager, 具体的类型 datamanager.xxx.xxx.xxx
  - 容器: zopen.apps.interfaces.ISupperApplet, 具体的类型 package.xxx.xxx

对象的基础属性
-------------------------------

- title
- description
- created
- modified
- identifire
- subjects
- creators
- contributors
- "expires"
- "effective"

表单中的属性
-------------------------------
这个根据表单定义而定，所有字段都将进入索引，常见的有：

- 'responsibles':SetIndex,#负责人
- 'start':ValueIndex,# 开始时间
- 'end':ValueIndex, # 结束时间
- 'amount':ValueIndex, #总量
- 'reviewer':SetIndex, #检查人

4) 属性集中的元数据
-------------------------------
所有属性集里面的字段都将进入索引，比如：

- zopen.abc.aa.title

建立索引
===============

系统不会自动建立和更新索引。IObjectIndexer：对象索引接口，用于修改后重建索引

建立索引，recursive是否递归::

  IObjectIndexer(obj).index(recursive=False)
  IObjectIndexer(obj).unindex(recursive=False)

对fields字段更新索引,recursive是否递归::

  IObjectIndexer(obj).reindex(recursive=False, fields=[])

重建权限路径索引,recursive是否递归::

  IObjectIndexer(obj).reindex_security(recursive=True)
  IObjectIndexer(obj).reindex_path(recursive=True)

对一个文件对象或文件夹对象，经行全文索引，以便可以通过文件里面的文字，搜索出这个文件对象 例子::

  IObjectIndexer(obj).index_fulltext(recursive=False, include_history=False)

- recursive #如果obj是文件夹对象，则这个参数应该是True，让程序递归对文件夹对象下的文件对象做全文索引
- include_history #对文件对象的历史版本也做全文索引

搜索
============

搜索表达式
----------------------
搜索是对字段进行搜索，我们先看一个例子:::

  result = QuerySet(restricted=True).\ 
           anyof(path=[container]).\
           anyof(subjects=[‘aa’,’bb’]).
           range(created=[None, datetime.datetime.today()]).\
           parse(title='我爱北京').\
           sort(‘-created’).limit(5)

QuerySet常用操作：

- eq: 等于
- anyof: 满足任何一个
- allof: 满足全部
- range: 一个区间范围
- exclude: 等于
- exclude_anyof: 满足任何一个
- exclude_allof: 满足全部
- exclude_range: 一个区间范围
- parse #搜索某字段
- sum(field) #统计某一个字段的和
- limit(x) #限制返回结果数 
- sort(Field) #按字段排序， 可已"+" 或"-"开头 , 以"-"开头时倒序排列

- ``exclude(**expression)`` #排除条件符合条件的结果

合并搜索
-----------
另外，可以将2个QuerySet相加，进行搜索合并::

 result = Queryset().filter(...) | QuerySet().filter(...)

如果2个QeurySet都有排序和sum操作，以第一个为准.

搜索属性集中的属性
-------------------------
调用filter或parse方法时，上面的field试用于 内置属性、基础属性和表单属性。
对于属性集中的字段，则需要增加一个 ``collection`` 参数来指明属性集的名称。

下面的例子表示依据档案扩展属性中的档案编号进行检索::

           filter(number__anyof=['A101', 'C103'], collection="archive")

如果是应用自带的属性集，则需要通过 ``app`` 来指定应用的名字::

           filter(title__anyof=['A101', 'C103'], collection="prop1", app="zopen.abc")

嵌套字段
--------------------------------
表单和属性中，存在一种动态表格字段, 可以嵌套一个子表格, 系统也能够搜索子表格中的字段.

搜索表单中的动态表格reviewer_table中的dept字段::

           filter(dept__anyof=['A101', 'C103'], parent="review_table")

搜索自定义属性集archive中的动态表格reviewer_table的dept字段::

           filter(dept__anyof=['A101', 'C103'], parent="review_table", collection="archive")

搜索软件包zopen.abc中属性集archive_archive中的动态表格reviewer_table的dept字段::

           filter(dept__anyof=['A101', 'C103'], parent="review_table", collection="prop1" app="zopen.abc")

分用户存储的字段
------------------------------
有些数据，是分用户存储的，比如投票字段、评审意见字段等。

这种字段的数据搜索，也是采用类似表格字段, 内置 ``_user`` 和 ``_value`` 这2个子字段.

搜索表单中的reviewer_table字段::

           filter(_user__anyof=['users.pan', 'users.zhang'], parent="review_comment")
           parse(_value='同意', parent="review_comment")

搜索属性集archive中的reviewer_table字段::

           filter(_user__anyof=['A101', 'C103'], parent="review_comment", parent="review_comment", collection="archive")
           parse(_value='同意', parent="review_comment", collection="archive")

跨字段全文搜索
-----------------------

如果搜索所有字段，可简单搜索::


如果要搜索多个字段::

   .parse('我北京', fields=['title', 'description'])

如果字段在属性集里面::

   .parse('我北京', fields=['archive.title', 'archive.description'])

如果字段在嵌套字段里面::

   .parse('我北京', fields=['.table.title', '.table.description'])
   .parse('我北京', fields=['archive.table.title', 'archive.table.description'])

直接采用JSON格式查询
----------------------------
TODO

搜索结果和分页
-------------------------------
搜索结果是一个list，len(result)可得到结果的数量。遍历搜索结果:::

  for obj in result:
    do something

当你需要显示的东西（results） 太多了，一个页面放不下的时候，可以使用Batch.

下面例子，可以让results 每页只显示20个::

  # view.py
  batch = Batch(results, start=request.get(‘b_start’, 0), size=20)
  for obj in batch:
      ...

  batch_html = renderBatch(context, request, batch)

搜索结果集的slice操作注意
-----------------------------------
搜索结果results，如果直接使用slice操作，比如::

 results[:5]
 results[0]

需要判断每个对象是否为空, 因为有可能索引存在，但是对象不存在.

但是for 循环则不会有问题，因为内部已经过滤掉了
 
