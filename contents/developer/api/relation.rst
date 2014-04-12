---
title: 关系 
description: 对象之间可以建立关系
---

================
关系
================

每一个对象都可以和其他的对象建立各种关系。

系统内置关系类型
-----------------------

- children:比如任务的分解，计划的分解
- attachment：这个主要用于文件的附件
- related :一般关联，比如工作日志和任务之间的关联，文件关联等
- comment_attachment：评注中的附件，和被评注对象之间的关联
- favorit:内容与收藏之间的关联
- "shortcut" 快捷方式

接口API：IRelations
-----------------------------------

- add(type, obj， metadata={})

  添加对obj的type类型关系 

  -   type:关系类型 
  -   obj：被关联对象
  -   metadata：这条关系的元数据
 
- remove(type, obj):删除对obj的type类型关系

  -   type:关系类型 
  -   obj：被关联对象

- set_target_metadata(type, obj, metadata):设置某条关系的元数据

- get_target_metadata(type, obj, metadata):得到某条关系的元数据
 
- list_sources(type):列出所有该类型的被关联对象
     type:关系类型 

- has_target(type):是否有该类型的关联对象

- has_source(type): 是否有该类型的被关联对象

- list_targets(type):列出所有该类型的关联对象
     type:关系类型 
 
- set_targets(type, target_list):

- clean():清除该对象的所有关系


使用示例
----------------------
将doc2设置为doc1的附件（doc1指向doc2的附件关系） ::
  
  IRelation(doc1).add('attachment', doc2) 

删除上面设置的那条关系::

  IRelation(doc1).remove('attachment', doc2) 

设置关系的元数据（关系不存在不会建立该关系）::

  IRelations(doc1).set_target_metadata('attachment', doc2, {'number':01, 'size':23}) 

得到关系的元数据（关系不存在返回None）::

  IRelations(doc1).get_target_metadata('attachment', doc2) 

