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

1） 系统自动维护的索引
-------------------------------

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

2）对象的基础属性
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

3) 表单中的属性
-------------------------------

- 'responsibles':SetIndex,#负责人
- 'start':ValueIndex,# 开始时间
- 'end':ValueIndex, # 结束时间
- 'amount':ValueIndex, #总量
- 'reviewer':SetIndex, #检查人

4) 属性集中的元数据
-------------------------------
比如：

- zopen.abc.aa.title

建立索引
===============

系统不会自动建立和更新索引。IObjectIndexer：对象索引接口，用于修改后重建索引

建立索引，recursive是否递归::

  IObjectIndexer(obj).index(recursive=False)
  IObjectIndexer(obj).unindex(recursive=False)

对fields字段重建索引,recursive是否递归::

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

搜索条件和排序
----------------------
搜索是对字段进行搜索，我们先看一个例子:::

  result = QuerySet(restricted=True).\ 
           filter(path__anyof=[container]).\
           filter(subjects__anyof=[‘aa’,’bb’]).
           exclude(created__range=[None, datetime.datetime.today()]).
           sort(‘-created’).limit(5)

QuerySet常用操作：

- limit(x) #限制返回结果数 
- sort(Field) #按字段排序， 可已"+" 或"-"开头 , 以"-"开头时倒序排列
- ``exclude(**expression)`` #排除条件符合条件的结果
- parse(text,Fields) #跨字段搜索函数
- ``filter(exclude=False, **expression)``
- sum(field) #统计某一个字段的和

对于这个结果：

result是一个list，len(result)可得到结果的数量。遍历搜索结果:::

  for obj in result:
    do something

如果需要对扩展属性中的字段进行搜索，可以在调用filter或parse方法时传入"md"参数。下面的例子表示依据档案扩展属性中的档案编号进行检索:::

  result = QuerySet(restricted=True).\
           filter(md="archive_archive", archive_number__anyof=['A101', 'C103'])
  result = QuerySet(restricted=True).\
           filter(md="zopen.abc,prop1", title__anyof=['A101', 'C103'])


结果分页
-------------------------------

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
 
