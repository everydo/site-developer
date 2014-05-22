---
title: 数据存储
description: 负责系统数据的存取，可以基于多种方式来存储。数据父子树状组织存放，有唯一的ID标识，支持版本，支持回收站，支持关系，可搜索
---

==================
数据存储
==================

.. Contents::
.. sectnum::

这里介绍系统的最底层，数据存储层。 数据分容器、条目2种，可设置属性，有唯一UID，相互建立关系关系，删除数据自动进入回收站，变更内容支持版本，数据可索引后进行搜索。

容器和条目
================
所有内容在系统中按照树状的层次结构存储，典型的站点结构如下::

    +- 站点根/
    |
    |----+- 栏目/
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

系统中的对象，可简单的抽象为2种对象：

- 容器类对象Container：如网站根、栏目、文件夹、表单容器
- 条目类对象Item：如文件、快捷方式、表单

新增对象
-------------
网站的根是root，他自身是一个容器，在其下面可以创建容器::

   root['conainer1'] = Contianer()
   root['conainer2'] = Contianer()

站点根下面，一般不直接创建条目，在容器里面可增加条目::

   container1['item1'] = Item()

也可再创建子容器::

   container1['sub_container1'] = Container()

访问对象
-----------
容器提供类似dict的访问方法::

   container1 = root['container1']
   container2 = root['container2']
   root.keys()   # ['container1', 'container2']
   root.values(), root.items()
   
容器中的名字
-------------
任何对象在容器中有唯一的名字 ``name`` ::

  container1.name   # 'container1'
  container2.name   # 'container2'
  container1['item1'].name        # 'item1'

找到容器
----------
任何对象可得到其所在的容器 ``parent`` ::

  container1.parent  # root
  item1.parent       # container1
  sub_container1.parent # container1

删除
---------
删除某个包含的内容::

  del root['container2']  # 整个容器删除
  del container1['item1']

排序的容器
--------------
容器类对象都支持对包含内容进行排序(注意：如果容器包含的内容数量大，为提高性能，可对部分内容进行排序)::

  root.set_order(('container2', 'container1'))
  container.ordered_keys()  # ('container2', 'container1')

移动复制
----------
可以对内容进行移动、改名或者复制::

    item1.move_to(cotainer1, 'item_1')  # 改名
    item1.move_to(cotainer2)   # 移动
    sub_container.copy_to(container2, 'new_container') # 复制

标识和定位对象
======================================

路径定位
-----------------
可叠加内容的名字、以及包含该内容的所有容器的名字，形成对象路径，用于定位一个内容::

   root.object_path(item1) # 返回: '/container2/item_1'
   root.object_by_path('/container2/item_1')  # 返回item1

数据库里面的对象，一旦发生移动或者改名，对象的路径就发生变化。这样用路径就不能来永久标识对象。

唯一标识定位
----------------
系统的所有对象，创建后均会注册一个永久的ID，无论以后对象是否移动或者改名，都不会改变::

  uid = root.obejct_uid(obj)
  obj = root.object_by_uid(uid)  # 通过uid找到对象

对象类型: object_type
=============================
约定属性 ``object_type`` 表示对象类型，让不同类型的对象有不同的行为。

容器和条目的object_type分别是 ``(Container, )`` 和 ``(Item, )`` , 系统还可以是如下对象：

应用容器 AppContainer
------------------------------
只有在应用容器里面，才能部署其他的应用，网站根就是一个应用容器。
应用容器里可以存放 表单容器、文件夹和子栏目. 

添加一个子文件夹::

  folder = app_container.add_folder(name, title="计划中心")

添加一个流程容器::

  collection = app_container.add_datacontainer(name='plan', 
                item_schema="zopen.plan:plan",  # 表单的定义
                schema="zopen.plan:plan",   # 数据容器的设置定义
                item_stage="zopen.plan:plan", # 表单的阶段定义 
                item_workflow="zopen.plan:plan", # 流程步骤定义
                title="项目计划")  # 这个参数可选

添加一个子应用容器::

  sub_container = app_container.add_appcontainer(name='plans', 
                                                schema="zopen.plan:default",
                                                title="计划")

注意部署的子应用名字不能重复，可以通过下面的方法选择一个名字(自带加上)::

  app_contaner.choose_name('plans') # 如果重复，则返回 plans-1 / plans-2

应用容器的object_type是 ``('AppContainer', 'Container')``

应用容器可以管理子栏目，子栏目可以是一个子应用或者一个软件包里面的脚本::

  app_container.append_tab(sub_container)  # 添加一个应用
  app_container.append_tab('zopen.sales:overview') # 添加一个软件包脚本, 作为视图
  app_container.insert_tab(0, sub_container) # 插入到最前面
  app_container.list_tabs()  # 返回 应用或者脚本名的列表
  app_container.remove_tab(sub_container) # 去除一个列表

文件夹 Folder
-----------------------
文件夹用来存放文件和文件的快捷方式，文件夹还能存放子文件夹::

  sub_folder = folder.add_folder(name)
  new_file = folder.add_file(name, data='', content_type='')
  shortcut = folder.add_shortcut(obj, version_id='')

文件夹的object_type是： ``('Folder', 'Container')``


文件 File
-------------
文件的object_type为 ``('File', 'Item')``

文件是最基础的内容形态，用于存放非结构化的数据，不能包含其他内容::

  my_file.set_data('this is long long text')
  my_file.content_type = 'text/plain'  

可以得到文件::

  my_file.get_data()

快捷方式 ShortCut
---------------------
分为：

- 文件快捷方式, 其object_type为: ``('FileShortCut', 'Item')`` 
- 文件夹快捷方式，object_type: ``('FolderShortCut', 'Item')``

快捷方式可以指向其他的文件或者文件夹::

  shortcut.shortcut_orign

数据容器 DataContainer
-------------------------
数据容器的object_type为： ``('DataContainer', 'Container')`` , 用于存放表单数据项::

  item = collection.add_item(metadata)

数据项 DataItem
-------------------
数据项用来存放结构化的表单数据，是系统的基础内容，不能包含其他内容.

其object_type为： ``('DataItem', 'Item')``


站点对象
------------------
根站点是一个特殊AppContainer

可以查看自身的运行信息::

  root.sys_info()

返回如下信息:

- version: 当前运行版本
- application: 应用名
- account: 比如zopen
- instance: 实例名
- operator: 本站点operator名字
- api_url: 本站点的api访问地址
- oc_api_url: oc的api地址

查看站点的运营选项参数::

    root.operation_options()

可以是如下参数：

- sms: 短信数量
- apps_packages: 软件包数量
- flow_records: 数据库记录
- docsdue: 文档使用期限
- docs_quota: 文件存储限额(M)
- docs_users: 文档许可用户数
- docs_publish: 文档发布
- flow_customize: 流程定制
- apps_scripting: 允许开发软件包


Schema自定义语义
=======================
所有内容对象都可以自定义字段，可以通过 ``schemas`` 进一步了解对象的详细字段，说明对象编辑、显示和存储信息。

应用容器天气查看，可通过 ``schema`` 来进行应用设置天气区域等字段::

  appcontainer.schemas = ('zopen.weather:default', )

数据容器可能是故障跟踪，有故障跟踪的一些设置项需要定义::

  datacontainer.schemas = ('zopen.issutracker:issue', )

具体的一个故障单数据项，则可能是::

  dataitemitem.schemas = ('zopen.issutracker:issue', )

如果这里有多个，表示继承。schema的具体定义和使用，参照 《表单处理》 一节

对象属性
==============================================

基础属性
--------------------------------------
系统的所有对象，都包括一组标准的属性，有系统自动维护，或者有特殊的含义。属性也称作元数据，metadata.

对象一旦加入到仓库，可以查看其创建人、修改人，创建时间、修改时间::

   item.md('creators')
   item.md('contributors')
   item.md('created')
   item.md('modified')

其他的基础属性，还包括::

  obj.md('identifier') 这个也就是文件的编号
  obj.md('expires') 对象的失效时间
  obj.md('effective') 对象的生效时间

可以更改对象的各种属性，如基础标题、描述、分类，表单字段::

   item1.set_md('title', 'Item 1')
   item1.update_md(title = 'Item 1',
                    description = 'this is a sample item',
                    subjects = ('tag1', 'tag2'))

对于非容器类型的内容，比如文件、数据项，可以直接通过切分来访问属性::

  title = item1['title']
  item1['title'] = 'new title'

自定义属性
---------------
可自由设置属性，对于需要在日历上显示的对象，通常有如下属性::

  obj.update_md(responsibles = ('users.panjy', 'users.lei'), # 负责人
                        start = datetime.now(), # 开始时间 
                        end = datetime.now(), 结束时间

对于联系人类型的对象，通常可以有如下表单属性::

  obj.set_md('mail', 'panjy@foobar.com') #邮件
  obj.set_md('mobile', '232121') # 手机

经费相关的属性::

  obj.set_md('amount', 211)

地理相关的属性::

  obj.set_md('longitude', 123123.12312) #经度
  obj.set_md('latitude', 12312.12312) # 纬度

属性集
---------------
为了避免命名冲突，更好的分类组织属性，系统使用属性集(mdset: metadata set)，来扩展一组属性.

创建一个属性集::

  obj.new_mdset('archive')

设置一个新的属性集内容::

  obj.set_mdset('archive', {'number':'DE33212', 'copy':33})
  
活动属性集的内的属性值的存取::

  obj.get_mdset('archive')['number']
  obj.get_mdset('archive')['number'] = 'DD222'

也可以批量更改属性值::

  obj.update_mdset('archive', copy=34, number='ES33')

删除属性集::

  obj.remove_mdset('archive')

查看对象所有属性集::

  obj.list_mdsets()  # 返回： [archive, ]

设置信息
-----------
通常对于容器会有一系列的设置信息，如显示方式、添加子项的设置、关联流程等等.

设置信息是一个名字叫 ``_settings`` 特殊的属性集，存放一些杂碎的设置信息. 由于使用频繁，提供专门的操作接口::

   container.set_setting(field_name, value)
   container.get_setting(field_name, default='blabla', inherit=True)

如果inherit为True，会自动往上找值，直到站点根。

具体包括：

1) 和表单相关的设置::

    datacontainer.set_setting('item_schemas', ('zopen.sales:query',))   # 包含条目的表单定义

2) 流程相关的::

    datacontainer.set_setting('item_workflows', ('zopen.sales:query',)): 容器的工作流定义(list)

3) 和显示相关的设置::

    container.set_setting('default_view', ('@@table_list')) : 显示哪些列
    container.set_setting('table_columns', ('title', 'description')) : 显示哪些列(list)

4) 和属性集相关的设置::

    container.set_setting('item_mdsets', ('archive_archive', 'zopen.contract:contract')) : 表单属性集(list)

5) 和阶段相关的设置::

    container.set_setting('item_stages', ('zopen.sales:query',)): 容器的阶段定义(list)

关系
================

每一个对象都可以和其他的对象建立各种关系.  常用关系类型包括：

- children:比如任务的分解，计划的分解
- attachment：这个主要用于文件的附件
- related :一般关联，比如工作日志和任务之间的关联，文件关联等
- comment_attachment：评注中的附件，和被评注对象之间的关联
- favorit:内容与收藏之间的关联
- "shortcut" 快捷方式

可以查出所有的关系类型::

  doc1.relation_types()  

将doc2设置为doc1的附件（doc1指向doc2的附件关系） ::
  
  doc1.add_relation('attachment', doc2, metadata={}) 

删除上面设置的那条关系::

  doc1.remove_relation('attachment', doc2) 

设置关系的元数据（关系不存在不会建立该关系）::

  doc1.set_relation_metadata('attachment', doc2, {'number':01, 'size':23}) 

得到关系的元数据（关系不存在返回None）::

  doc1.relation_metadata('attachment', doc2) 

查看所有的附件::

  doc1.relation_tagets('attachment')

清除某种或所有的关系::

  doc1.clean_relations(type='attachment')

附件查看主文件::

  doc2.relation_sources('attachment')

版本管理
==================
文件File、数据项Item支持版本管理，可以保存多个版本，每个版本有唯一自增长的ID来标识

任何对象都可以保存历史版本，一旦保存当前对象的版本号发生变化::

   context.save_revision() # TODO

文档每次变更，默认保存为临时版本，临时版本过期会自动清理。

可以降文档定版，一旦定版，版本就是正式版本::

  context.fix_revision(revision_id=None, major_version=None, minor_version=None) # TODO

- 如果不传revision_id，表示对当前的工作版本进行定版
- 如果不传 major_version，继续沿用上一个version_number
- 如果不传 minor_version，自动增长上一个revision_number

可查询工作版本的信息::

  context.get_revision_info(revision_id=None) # TODO

如果revision_id为None，表示工作版本。返回::

   {'revision_id' : 12, # 版本ID
    'major_version' : 1,   # 版本号
    'minor_version' : 0,  # 版次号
    'user' : 'users.panjy',  # 版本修改人
    'timestamp' : 12312312.123,  # 版本修改时间
    'comment' : 'some comments',   # 版本说明
   }

其中如果 major_version 为空，表示没有定版。

查看所有历史版本信息::

   context.list_revisions(include_temp=True) 

返回revision_info的清单

得到一个历史版本::

   context.get_revision(revision_id) # TODO

删除一个版本::

   context.remove_revision(revision_id) # TODO

权限控制
================

系统中可以直接修改权限来进行权限管理，也可以通过修改角色来进行权限管理。

角色
--------
系统支持如下角色，角色ID为字符串类型, 可以枚举系统对象所有的角色::

  obj.allowed_roles

不同对象使用的角色不同，系统全部角色包括：

- 'Manager' 管理员
- 'Editor' 编辑人
- 'Owner' 拥有者
- 'Collaborator' 添加人
- 'Creator': 文件夹创建人
- 'ContainerCreator': 子栏目/容器创建人
- 'Responsible' 负责人
- 'Subscriber' 订阅人
- 'Accessor' 访问者
- 'Reader5'
- 'Reader4'
- 'Reader3'
- 'Reader2'
- 'Reader1'
- 'PrivateReader5' 超级查看人
- 'PrivateReader4' 仅仅文件授权的时候用，不随保密变化
- 'PrivateReader3' 仅仅文件授权的时候用，不随保密变化
- 'PrivateReader2' 仅仅文件授权的时候用，不随保密变化
- 'PrivateReader1' 仅仅文件授权的时候用，不随保密变化

授权
--------------
在obj对象上，授予用户某个角色::

  obj.grant_role(role_id, pid)

同上，禁止角色::

  obj.deny_role(role_id, pid)

同上，取消角色::

  obj.unset_role(role_id, pid)

检查权限
-------------
检查当前用户对某对象是否有某种权限，可使用 ``permit`` 方法::

  obj.check_permission(permission_id)

如果有该权限即返回True，反之返回False

系统中常用权限，权限ID为字符串类型，下文中权限ID将用permisson_id来代替。

- 'Public'：公开，任何人都可以访问
- 'ManageContent'：管理
- 'View'：查看的权限
- 'Access'：容器/栏目访问的权限
- 'Edit'：编辑的权限
- 'Add'：添加文件、流程单
- 'AddFolder': 添加文件夹
- 'AddContainer': 添加容器(子栏目)
- 'Logined': 是否登录

'Access'和'View'的区别，需要进入文件夹(Access)，但是不希望查看文件夹包含的文档(View)。

读取权限设置
---------------
根据角色来获取obj对象上拥有该角色的用户ID::

  obj.role_principals(role_id)

得到某个用户在obj上的所有角色::

  obj.principal_roles(user_id)

得到上层以及全局的授权信息::

  obj.inherited_role_principals(role_id)

得到某个用户在上层继承的角色::

  obj.inherited_principal_roles(user_id)

对象的状态
===========================
每一个对象存在一组状态，存放在对象的 ``stati`` 属性中::

   'visible.default' in context.stati

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
    context.set_state('modify.archived', do_check=False)
    # 设置文件夹为受控，需要检查权限
    context.set_state('folder.control', do_check=True)

也可以得到某个状态::

    context.get_state('visible') # 得到可见状态	

标签组
============

标签组实现了多维度、多层次、可管理的分类管理. 

设置标签组
-------------
标签组在容器(文件夹、数据容器、应用容器)上设置，可得到标签组设置::

  container.list_facetags() # TODO

输出为::

  [{'group': '按产品',
    'required':true,
    'single':true,
    'tags': [{'name':'wps'},
             {'name':'游戏'},
             {'name':'天下'},
             {'name':'传奇'},
             {'name':'毒霸'}
   ]},

   {'group': '按部门'
    'required':true,
    'single':true,
    'tags': [{'name':'研发', 
              'tags':[{'name':'产品'}, 
                      {'name':'测试'},
                      {'name':'软件'},
                      {'name':'硬件'},
                     ]
             },
             {'name':'市场'},
            ]
   }]

可以设置::

  container.set_facetags(facetag_setting) # TODO

也可以导出为文本形式的标签组，用于编辑::

  container.export_facetags() # TODO

或者导入::

  container.import_facetags() # TODO

标签组存在必选和单选控制，可以校验::

  container.check_facetags(tags) # 返回: {'required':[], 'single':[]}

标签组设置可以继承上层设置, 可以通过这个变量来控制::

  container.inherit_facetags = True # TODO

标签维护
-------------
如果要添加一个标签::

  context.add_tag('完成') # TODO

如果这个标签所在的标签组是单选的，会自动去除其他的标签。

注意，标签存放在名字叫做 ``subjects`` 的属性中，可以直接维护::

  context.md('subjects')
  context.set_md('subjects', ['完成', '部门'])

回收站
============

系统所有内容，删除之后，都将进入回收站。

一旦进入回收站，系统会定期对回收站的内容进行清理。删除历史已久的回收站内容::

 # 查看回收站的内容
 # 从回收站收回一个对象
 # 从回收站里面永久删除

