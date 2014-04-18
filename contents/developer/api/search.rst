---
title: 内容搜索
description: 搜索内容
---

======================
内容搜索
======================

发起搜索: /api/search
=============================
使用 ``POST`` ``/api/search`` 来进行搜索，只能搜索到有权限查看的内容，在body中填写查询条件::

  'query':[ # 类似ES
               ],
  'sort':{},
  'aggs':{},
      'limit':1
  'size':20
  'from':1

搜索条件
================
基础条件
-------------
- stati: 状态
- parent: 上一级对象的intid
- size: 大小
- content_type:   全文的内容类型，对于无法知道的内容类型，以 ``application/x-ext-{ext}`` 来替代
- schemas : 基础属性的语义定义
- mdsets: 属性集
- object_type: 是什么类型的对象

  - 文件： File 
  - 快捷方式：ShortCut 
  - 文件夹：Folder
  - 表单：Item
  - 表单容器：Collection
  - 容器: Container
  - 流程任务: Task

- path: 路径，值是所有父对象intid的集合
- file_content:     文件内包含的文本文件，用于全文搜索 
- references:   关联的文件

自定义属性(元数据)
---------------------------

搜索结果控制
====================
排序::

  sort=title
  sort=start_date
  sort=zopen.sales.sales.number_date

分页控制，减少返回结果数量::

  size=10
  start=1

限制结果数量，加快搜索速度::

  limit=200

搜索结果
===================
::

  {count:10,
   results: [ { ''  },
            ]
  }

