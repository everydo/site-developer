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

有多少种索引，就能从多少角度搜索。
对象的所有属性和属性集都进入索引，另外还包括一组内置的、自动维护的属性:

- stati: 状态
- path, 路径，值是父对象intid的集合
- parent: 上一级对象的intid
- size, 大小
- file_content:     全文 
- references:   关联的文件
- allowed_principals:     授权的人
- disallowed_principals:  禁止的人
- object_provides: 内容提供的接口

其中object_provides表示对象是什么，取值为:

- 文件： File 

  如果是文件，文件的小写后缀也会进入这个索引，如：doc, docx, txt, json, png

- 快捷方式：ShortCut 
- 文件夹：Folder
- 表单：Item
- 表单容器：Collection
- 容器: Container
- 流程任务: Task


索引维护
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

 result = Queryset().anyof(...) | QuerySet().allof(...).exclude(...)

如果2个QeurySet都有排序和sum操作，以第一个为准.

搜索属性集中的属性
-------------------------
调用filter或parse方法时，上面的field试用于 内置属性、基础属性和表单属性。
对于属性集中的字段，则需要增加一个 ``namespace`` 参数来指明属性集的名称。

下面的例子表示依据档案扩展属性中的档案编号进行检索::

   .anyof(number=['A101', 'C103'], mdset="archive")
   .anyof(number=['A101', 'C103'], mdset="archive")

多行表格字段
--------------------------------
多行表格值 ``review_table`` 类似如下::

    [{'title':'aa', 'dept':['groups.121', 'groups.32']}, 
     {'title':'bb', 'dept':['groups.3212', 'groups.3212']}]

搜索表单中的动态表格reviewer_table中的dept字段::

   anyof(dept=['groups.1213', ], parent="review_table", )

搜索自定义属性集archive中的动态表格reviewer_table的dept字段::

   anyof(dept=['groups.1213', ], parent="review_table", mdset="archive")

dict字段
------------------------------
存储(dict)示例如下::

    {'panjy':'good', 'li':'well', 'dd':'asdfa'}

这种字段的数据搜索，也是采用类似表格字段, 内置 ``key`` 和 ``value`` 这2个子字段::

   [{'key':'panjy', 'value':'good'},
    {'key':'li', 'value':'well'},
    {'key':'dd', 'value':'asdfa'}]

搜索表单中的reviewer_reviewcomment字段::

   anyof(key=['users.pan', 'users.zhang'], parent="review_comment")
   parse(key='同意', parent="review_comment")

搜索属性集archive中的reviewer_comment字段::

   anyof(key=['A101', 'C103'], parent="review_comment", mdset="archive")
   parse(value='同意', parent="review_comment", mdset="archive")

全文搜索parse
------------------
默认所有字符串类型的字段，都支持全文搜索。

但是多值类型(list/tuple)中的字符串，不支持全文搜索，只能完全匹配:: 

   ('asd asd', 'fas', 'ssas')

如果搜索所有字段，可简单搜索::

   .parse('我北京')

如果要搜索多个字段::

   .parse('我北京', fields=['title', 'description'])

如果字段在属性集里面::

   .parse('我北京', fields=[{'archive.title', 'archive.description'])

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

