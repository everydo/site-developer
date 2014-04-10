---
title: 数据访问
description: 系统的所有对象，都父子树状组织存放，有唯一的ID标识，可相互建立关系，并能够管理一组属性。
---

==================
数据访问
==================

.. Contents::
.. sectnum::


树状对象关系
=====================

对象数据库中的主题对象，是一个树状的层次结构关系，如下::

    + 站点根
    |-----+容器（文件夹、项目、部门）
    |    |----+子容器1
    |    |   |--文件1
    |    |   |--文件2
    |    |----+子文件夹2
    | 	  |   |--….
    |-----+数据管理器
    |    |-- 流程单1
    |    |-- 流程单2

可以看到对象是父子包含关系。总体分为能包含其他对象的容器类型的对象，以及不能包含其他对象的非容器对象。

脚本中，可使用2个重要的内置变量：

- context: 是当前操作的对象
- container: 是当前对象context所在的容器对象，比如文件夹或者数据管理器。

对于任何一个对象，有如下方法得到其所在的容器信息：

- getParent(context)得到对象的父容器对象，也就是container
- getRoot()得到站点根对象

容器类对象
----------------
文件夹、流程、项目、部门、工作组等，都是容器类对象。

容器对象，是可以包含子对象的容器。可以用dict方法操作包含的子对象:

- container.values() 得到容器全部包含对象

  注意，如果数据量大，这个可能带来很严重的性能问题。因为所有数据会逐个从数据库加载到内存。如果是数据搜索过滤，应该采用搜索解决，通过索引来查找数据。具体见QuerySet查询接口。

- container.keys() 得到全部包含对象的名字
- container.items() 得到全部包含对象的列表 [(name, child_obj)]
- container [name]得到某个包含对象
- del contaner[name]删除某个包含对象

每个对象在容器中有一个唯一的名字，可以用getName得到::

  getName(context)

添加子对象
--------------
如果要添加一个对象，可以::

  container[name] = new_obj

注意每个container下的对象name必须唯一，可以使用INameChooser来自动生成name::

  new_name = INameChooser(container).chooseName(name)

对象路径
---------------
易度每个请求url，都是RESTful，由资源和操作2部分组成，比如::

 http://mycompanysite.com/files/folder_a/folder_b/@@view.html
 http://mycompanysite.com/files/folder_a/folder_b/@@@account.package.script

最后一个@@或@@@是一个视图标识符(表示可爱的眼睛)，将url分割为2部分：

- http://mycompanysite.com/files/folder_a/folder_b

  这个直接定位到一个资源，是资源的地址，这个资源就是context；站点域名后的每级名字，就是对象所在逐层父容器的名字。所以，要理解当前的context是什么，就要去看运行脚本的请求url是什么即可。

- view.html或account.package.script

  这个表示对资源的展示方法，是一个视图。

需要指出的例外情况：

- 如果url可能不包括@@，这表示使用了默认视图@@index.html
- 在流程单的添加表单，由于对象还没添加，按照前面的规则，context应该是数据管理器，但是从统一和简化的角度考虑，我们将context强制指定为None（表示正在创建），而用下面的container来表示数据管理器。

对象移动、复制
---------------------
可以使用"IObjectMover"接口移动对象或者改名::

    IObjectMover(context).moveTo(parent, new_name)
    IObjectMover(context).copyTo(parent, new_name)

具体的API包括：

- def moveTo(target, new_name=None): 移动对象到target 这个目录下，如果变量new_name 有值（非None）, 对象就改名为new_name。返回对象移动到target目录后的新名字。 需要注意的是target 是需要(implements) 实现IpasteTarget 这个接口.
- def moveable(): 如果这个对象允许移动， 就返回‘True‘, 否则返回‘False’
- def moveableTo(target, name=None): 如果对象允许移动到target 这个目录就返回‘True‘, 否则返回‘False’

对象的永久标识，以及快捷地址
======================================

ZODB数据库里面的对象，一旦发生移动或者改名，对象的路径就发生变化。这样用路径就不能来永久标识对象。

事实上，系统的所有对象，创建后，均会在一个全局的对象注册器intids中注册。一旦注册，系统会用一个长整数来永久标识这个对象。无论以后对象是否移动或者改名，都可以通过这个长整数快速找到对象自身::

  #通过长整数标识，找到对象
  intids.getObject(uid)

  #得到对象长整数标识
  uid = intids.getId(obj)

有了这个长整数标识，可在表单中记录这个标识来传递对象。

同时，我们也可以快速定位到这个对象::

   http://example.com/++intid++12312312

接口：对象的类型
====================

不同的对象，通过接口来标识其类型，比如文件、帖子、文件夹、批注等。

系统包括如下接口::

  接口	        说明	         完整标识
  IFile	        文件	         zopen.content.interfaces.IFile
  IFolder	文件夹	         zopen.content.interfaces.IFolder
  IApplet	应用	         zopen.apps.interfaces.IApplet
  IDataManager	数据/流程管理器	 zopen.flow.interfaces.IDataManager

判断一个对象是不是文件，可使用如下语句::

  IFile.providedBy(context)

对象属性/元数据
==============================================

对象元数据
--------------------------------------

系统的所有对象，都包括一组标准的元数据，也就是所谓的都柏林核心元数据（这是一个图书馆元数据国际标准）::

  IMetadata(obj)['title'] 对象的标题
  IMetadata(obj)['description'] 对象的描述信息
  IMetadata(obj)['identifier'] 这个也就是文件的编号
  IMetadata(obj)['creators'] 对象的创建人 注意，这是个list类型的对象
  IMetadata(obj)['created'] 对象的创建时间
  IMetadata(obj)['modified'] 对象的修改时间
  IMetadata(obj)['expires'] 对象的失效时间
  IMetadata(obj)['effective'] 对象的生效时间

对于需要在日历上显示的对象，有如下字段::

  IMetadata(obj)['responsibles'] 负责人
  IMetadata(obj)['start'] 开始时间 
  IMetadata(obj)['end'] 结束时间

对于联系人类型的对象，通常可以有如下字段::

  IMetadata(obj)['email'] 邮件
  IMetadata(obj)['mobile'] 手机

经费相关的字段::

  IMetadata(obj)['amount'] 

数量相关的字段::

  IMetadata(obj)['quantity']

对于地理位置对象，通常有如下字段::

  IMetadata(obj)['longitude'] #经度
  IMetadata(obj)['latitude'] # 纬度

为了避免命名冲突，可以增加前缀，比如::

  # 软件包zopen.abc中定义的prop1属性集所定义的经度
  IMetadata(obj)['zopen.abc.prop1.longitude'] 
  IMetadata(obj)['zopen.abc.prop1.title'] # 类似上面的纬度

使用星号，可以直接读取一组属性集，下面返回zopen.abc.prop1属性集的所有内容（一个字典）::

  IMetadata(obj)['zopen.abc.prop1.*']

如果obj不是容器类型的对象(文件或者表单)，那更简单的写法是::

    obj['title']
    obj['zopen.abc.prop1.title']
    obj['zopen.abc.prop1.*']

关系
=================

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


使用事例
----------------------
将doc2设置为doc1的附件（doc1指向doc2的附件关系） ::
  
  IRelation(doc1).add('attachment', doc2) 

删除上面设置的那条关系::

  IRelation(doc1).remove('attachment', doc2) 

设置关系的元数据（关系不存在不会建立该关系）::

  IRelations(doc1).set_target_metadata('attachment', doc2, {'number':01, 'size':23}) 

得到关系的元数据（关系不存在返回None）::

  IRelations(doc1).get_target_metadata('attachment', doc2) 

