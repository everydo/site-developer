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
   root.locate_by_path('/files/folder_a/folder_b/file_c')

数据库里面的对象，一旦发生移动或者改名，对象的路径就发生变化。这样用路径就不能来永久标识对象。

唯一标识定位
----------------
系统的所有对象，创建后均会注册一个永久的整数，无论以后对象是否移动或者改名，都不会改变::

  int_id = root.object_uid(obj)
  root.locate_by_uid(int_id)  # 通过uid找到对象

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

回收站
============

系统所有内容，删除之后，都将进入回收站。

一旦进入回收站，系统会定期对回收站的内容进行清理。删除历史已久的回收站内容::

 # 查看回收站的内容
 # 从回收站收回一个对象
 # 从回收站里面永久删除

