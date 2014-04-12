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

为了避免命名冲突，可以定义属性集，来扩展一组属性。

使用星号，可以直接读取一组属性集，下面返回用户自定义的档案管理archive属性集的所有内容（一个字典）::

  IMetadata(obj).new_collection('archive')
  IMetadata(obj).get_collection('archive')
  IMetadata(obj).remove_collection('archive')
  IMetadata(obj).list_collections()  # 返回： [archive, ]

得到其中的一个字段值::

  IMetadata(obj).get_collection('archive')['archive_number']

属性的快捷访问
---------------------------
如果obj表单，那更简单的写法是::

    obj['title']

