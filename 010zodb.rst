==================
对象数据库
==================

.. Contents::
.. sectnum::

系统的所有数据，都以对象的形式存放在一个对象数据库(ZODB)中。ZODB数据库是一个NoSQL数据库，数据的存取直接封装在对象操作中，而不是采用SQL语句。

树状对象关系
=====================


对象数据库中的主题对象，是一个树状的层次结构关系，如下：::

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

- context，是当前操作的对象
- container，是当前对象context所在的容器对象，比如文件夹或者数据管理器。

对于任何一个对象，有如下方法得到其所在的容器信息：

- getParent(context)得到对象的父容器对象，也就是container
- getRoot()得到站点根对象

容器对象
==============
容器对象，是可以包含子对象的容器。可以用dict方法操作包含的子对象

- container.values() 得到容器全部包含对象

  注意，如果数据量大，这个可能带来很严重的性能问题。因为所有数据会逐个从数据库加载到内存。如果是数据搜索过滤，应该采用搜索解决，通过索引来查找数据。具体见QuerySet查询接口。

- container.keys() 得到全部包含对象的名字
- orderedFolders() 得到排序后的文件夹
- container.items() 得到全部包含对象的列表 [(name, child_obj)]
- container [name]得到某个包含对象
- del contaner[name]删除某个包含对象

每个对象在容器中有一个唯一的名字，可以用getName得到::

  getName(context)



添加子对象
==============
如果要添加一个对象，可以::

  container[name] = new_obj

注意每个container下的对象name必须唯一，可以使用INameChooser来自动生成name::

  new_name = INameChooser(container).chooseName(name)

对象路径
===============

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
=============

可以使用"IObjectMover"接口移动对象或者改名::

   IObjectMover(context).moveTo(parent, new_name)
   IObjectMover(context).copyTo(parent, new_name)

具体的API包括：

- def moveTo(target, new_name=None): 移动对象到target 这个目录下，如果变量new_name 有值（非None）, 对象就改名为new_name。返回对象移动到target目录后的新名字。 需要注意的是target 是需要(implements) 实现IpasteTarget 这个接口.
- def moveable(): 如果这个对象允许移动， 就返回‘True‘, 否则返回‘False’
- def moveableTo(target, name=None): 如果对象允许移动到target 这个目录就返回‘True‘, 否则返回‘False’


对象的永久标识
=====================

ZODB数据库里面的对象，一旦发生移动或者改名，对象的路径就发生变化。这样用路径就不能来永久标识对象。

事实上，系统的所有对象，创建后，均会在一个全局的对象注册器intids中注册。一旦注册，系统会用一个长整数来永久标识这个对象。无论以后对象是否移动或者改名，都可以通过这个长整数快速找到对象自身。

- uid = intids.getId(obj)
  得到对象长整数标识
- intids.getObject(uid)
  通过长整数标识，找到对象

有了这个长整数标识，可在表单中记录这个标识来传递对象。

对象关系
===================
 
对象之间除了前面介绍的树状包含关系之外，还可以定义各种关系，比如：

- children:比如任务的分解，计划的分解
- attachment：这个主要用于文件的附件
- related :一般关联，比如工作日志和任务之间的关联，文件关联等
- comment_attachment：评注中的附件，和被评注对象之间的关联
- favorit:内容与收藏之间的关联

关系操作，包括：

- 查找关系::

   for obj in relations.findTargets(context, 'attachment'):
       print getName(obj)

- 创建关系::
  
   related = OneToOneRelationship(self, ['attachment'], [obj])
   Relations.add( related )

- 删除关系


PersistentList，PersistentDict
================================================
Python的基础类型包括list和dict，都是mutable的，这2种对象发生变更修改，不会字段通知ZODB更新数据库。这样导致数据修改不能保存。

为了解决这一问题，对于需要保存的数据库中的数据，应该采用PersistentList来代替list，使用PersistentDict代替dict。他们的使用接口其实是一致的。
