---
title: 内容仓库
description: 系统首先是一个各种内容的存储仓库，都父子树状组织存放，有唯一的ID标识，支持版本，支持回收站
---

==================
内容仓库
==================

.. Contents::
.. sectnum::

系统可以存储各种文件、表单等内容，通过各种文件夹、容器来组织管理。

内容仓库结构
==================
所有内容在系统中按照树状的层次结构存储，如下::

    +- 站点根/
    |
    |----+- 容器/
    |    |---+- 文件夹1/
    |    |   |- 文件1
    |    |   |- 快捷方式
    |    |   |- 子文件2/
    |    |
    |    |---+- 文件夹2/
    | 	 |   |- ….
    |
    |----+- 表单容器/
    |    |- 表单1
    |    |- 表单2

系统只存放6种内容: 容器、文件夹/文件/快捷方式、数据集/数据项。

容器 Container
--------------------
只有在容器里面，才能部署其他的应用。网站根就是一个容器，下面的方法得到网站根::

  root = get_root()

容器里可以存放 表单容器、文件夹和子容器::

  folder = container.deploy_folder(name, metadata={'title':'a folder',}, **mdsets)
  collection = container.deploy_collection(name, metadata={'title':'a collcetion'}, **mdsets)
  sub_container = container.deploy_container(name, metadata={'title':'a sub container'}, **mdsets)

其中:

- metadata: 新部署应用的元数据
- mdsets: 新部署应用的一组属性集

文件夹 Folder
----------------
文件夹用来存放文件和文件的快捷方式，文件夹还能存放子文件夹::

  sub_folder = folder.add_folder(name, metadata={}, **mdsets)
  shortcut = folder.add_shortcut(obj, version_id='', metadata={}, **mdsets)
  new_file = folder.add_file(name, data='', content_type='', metadata={}, **mdsets)

文件 File
-------------
文件是最基础的内容形态，用于存放非结构化的数据，不能包含其他内容::

  my_file.get_data()
  my_file.set_data(data)
  my_file.get_content_type()

快捷方式 ShortCut
---------------------
快捷方式可以指向其他的文件或者文件夹，不能包含其他内容::

  shortcut.get_orign()
  shortcut.reset_version(version_id)

数据集 Collection
-------------------------
用于存放表单数据项::

  item = collection.add_item(metadata, **mdsets)

数据项 Item
--------------
数据项用来存放结构化的表单数据，是系统的基础内容，不能包含其他内容.

容器关系
===============
容器类对象，是可以存放子内容的容器, 包括文件夹、数据集、容器这四种。

任何对象在容器中有唯一的名字::

  folder.name
  contaiener.name
  my_file.name
  collection.name
  ...

任何对象可得到其所在的容器::

  obj.parent

可得到容器包含的全部对象(注意，如果包含内容数量多，可能存在性能问题)::

  container.values()

或者全部包含对象的名字::

  container.keys()

得到容器包含的某个名字的内容::

  container [name]

删除某个包含的内容::

  del contaner[name]

容器类对象都支持对包含内容进行排序(注意：如果容器包含的内容数量大，为提高性能，可对部分内容进行排序)::

  container.set_order(keys)
  container.ordered_keys()

可以使用"IObjectMover"接口，对内容进行移动、改名或者复制::

    IObjectMover(context).move_to(parent, new_name)
    IObjectMover(context).copy_to(parent, new_name)

标识和定位对象
======================================
路径定位
-----------------
可叠加内容的名字、以及包含该内容的所有容器的名字，形成对象路径，用于定位一个内容::

   root.object_path(file_c) # 返回: '/files/folder_a/folder_b/file_c'
   root.object_by_path('/files/folder_a/folder_b/file_c')

数据库里面的对象，一旦发生移动或者改名，对象的路径就发生变化。这样用路径就不能来永久标识对象。

唯一标识定位
----------------
系统的所有对象，创建后均会注册一个永久的整数，无论以后对象是否移动或者改名，都不会改变::

  intids = root.get_intid_register() # 唯一标示注册表
  int_id = intids.get_id(obj)
  obj = intids.get_object(int_id)  # 通过int_id找到对象

对象属性
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

设置属性集
-----------------
设置信息是一个名字叫 ``_etc`` 特殊的属性集，存放一些杂碎的设置信息. 由于使用频繁，提供专门的操作接口::

   IMetadata(collection).etc_get('children_workflow')
   IMetadata(collection).etc_set('children_workflow', ('zopen.sales:query', ))

属性的快捷访问
---------------------------
如果obj表单，那更简单的写法是::

    obj['title']

属性值
------------------
基础的属性值类型包括:

- 字符串: 全文索引
- 整数: 数值索引
- 浮点数: 数值索引
- 日期：日期索引

系统自动根据属性值的类型，来做索引.

- 多值类型(list/tuple/set): 

  根据包含值的类型做索引。如果是字符串，则做全匹配索引, 非全文索引

- 分用户存储(dict)

- 嵌套表( [{'':, '':}] )

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

站点设置信息
=============

得到某个运营选项参数::

    root.get_operation_option(option_name=None, default=None)

option_name可以是如下参数：

- sms: 短信数量
- apps_packages: 软件包数量
- flow_records: 数据库记录
- docsdue: 文档使用期限
- docs_quota: 文件存储限额(M)
- docs_users: 文档许可用户数
- docs_publish: 文档发布
- flow_customize: 流程定制
- apps_scripting: 允许开发软件包

版本管理
==================

文件File、数据项Item支持版本管理，可以保存多个版本::

   rev_man = IRevisionManager(obj)
   rev_man.save(comment='', metadata={}) #存为一个新版本
   rev_man.retrieve(selector=None, preserve=()): 获得某一个版本
   rev_man.get_history(preserve=()): 得到版本历史清单信息
   rev_man.remove(selector, comment="", metadata={}, countPurged=True) #删除某个版本 
   # 得到当前工作版本的版本信息，取出来后，在外部维护数据内容
   rev_man.getWorkingVersionData() 

对象的状态
===========================
每一个对象存在一组状态，存放在对象的context.stati属性中

modify: 发布

- modify.default	草稿
- modify.pending	待审
- modify.archived	发布/存档 (只读)
- modify.history_default 普通历史版本
- modify.history_archived 发布的历史版本

visible: 保密

- visible.default	普通
- visible.private	保密

使用状态机IStateMachine，来控制对象状态的变化::

    # 不进行权限检查，直接发布某个文档
    IStateMachine(context).set_state('modify.archived', do_check=False)
    # 设置文件夹为受控
    IStateMachine(context).set_state('folder.control', do_check=False)

其包括的接口有：

- getAllStates()	得到对象的所有状态	
- getState(prefix) 得到某个的状态	
- setState(new_state, do_check=True) 设置状态	
- nextStates(self, prefix) 得到后续状态	

回收站
============

系统所有内容，删除之后，都将进入回收站。

一旦进入回收站，系统会定期对回收站的内容进行清理。删除历史已久的回收站内容::

 # 查看回收站的内容
 # 从回收站收回一个对象
 # 从回收站里面永久删除

