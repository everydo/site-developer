---
title: 索引和查询
description: 对象数据库和普通的关系数据库不一样，需要手工维护索引，目前还只支持内置的一组索引，不支持自定义索引。
---

.. contents::
.. sectnum::

数据索引和搜索
============================================


内置的索引
-------------------------------

系统现在支持三种索引类型：
- 文本索引TextIndex：可对文字进行全文搜索
- 单值索引ValueIndex：可比较大小和排序
- 多值索引SetIndex：可进行anyof/allof搜索

具体的内置索引如下

1） 系统自动维护的索引

- 'creators':SetIndex, # 创建人
- 'subjects':SetIndex, # 这个是关键字，或者标签
- 'created':ValueIndex, # 创建时间
- 'modified':ValueIndex, #修改时间
- 'effective':ValueIndex,#生效时间
- 'expires':ValueIndex,#失效时间
- 'contributors':SetIndex,#贡献者
- 'identifier':ValueIndex,#编号
- 'searchable_text':TextIndex, # 可全文搜索的内容，这个是自动维护的
- 'stati':SetIndex,#审核状态
- 'object_provides':SetIndex, # 对象提供的接口


  - 文档：zopen.content.interfaces.IFile / zopen.content.interfaces.IImage / zopen.content.interfaces.IDocument
  - 快捷方式：zopen.shortcut.interfaces.IShortCut
  - 文件夹：zopen.content.interfaces.IFolder
  - 表单：zopen.model.interfaces.IDataItem, 每个具体类系的表单 dataitem.xxx.xxx.xxx （如：dataitem.zopen.archive.borrow）
  - 表单容器：zopen.model.interfaces.IDataManager, 具体的类型 datamanager.xxx.xxx.xxx
  - 容器: zopen.apps.interfaces.ISupperApplet, 具体的类型 package.xxx.xxx


- 'path':SetIndex,  # 路径，值是父对象的集合
- 'size':ValueIndex, #尺寸大小的索引
- 'total_size':ValueIndex, #总大小的索引,目前用于文件:文件大小+版本大小

2）表单定义可根据字段名，自动索引的

- 'title':TextIndex,    # 标题
- 'responsibles':SetIndex,#负责人

  如果把扩展应用的表单字段设置为responsibles, 可进行搜索

- 'start':ValueIndex,# 开始时间
- 'end':ValueIndex, # 结束时间
- 'amount':ValueIndex, #总量

- 'reviewer':SetIndex, #检查人

3) 未来会去除的

- 'responsible':ValueIndex,#负责人 , 这个未来会去除
- 'level':ValueIndex, # 级别

- 和查看权限相关的2个内部索引，不会直接使用，默认只搜索有查看权限的内容。设置QuerySet(restricted=False)可搜索所有内容

  - 'allowed_principals':SetIndex, # 可查看的人
  - 'disallowed_principals':SetIndex, # 禁止查看的人

建立索引
--------------------------

系统不会自动建立和更新索引。IObjectIndexer：对象索引接口，用于修改后重建索引

建立索引，recursive是否递归::

  IObjectIndexer(obj).indexObject(recursive=False)

对fields字段重建索引,recursive是否递归::

  IObjectIndexer(obj).reindexObject(recursive=False, fields=[])

重建权限索引,recursive是否递归::

  IObjectIndexer(obj).reindexSecurity(recursive=True)


搜索接口
----------------------------------------------

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

result是一个list，len(result)可得到结果的数量。

遍历搜索结果:::

  for obj in result:
    
    do something

结果分页
-------------------------------

当你需要显示的东西（results） 太多了，一个页面放不下的时候，可以使用Batch.

下面例子，可以让results 每页只显示20个::

  # view.py
  batch = Batch(results, start=request.get(‘b_start’, 0), size=20)
  batch_html = renderBatch(context, request, batch)
  # view.pt
  <div tal:replace="structure batch_html"></div>

搜索结果集的slice操作注意
-----------------------------------
搜索结果results，如果直接使用slice操作，比如::

 results[:5]
 results[0]

需要判断每个对象是否为空, 因为有可能索引存在，但是对象不存在.

但是for 循环则不会有问题，因为内部已经过滤掉了

让表单其他字段可以搜索
---------------------------
用户自定义的流程单字段、扩展属性，现在易度还不支持任意自定义搜索。但是：

1. 字段内容自动会全文搜索的
2. 对于流程单, 如果将字段的名称和上面索引的名称保持相同，也可以自动索引
3. 配合标签组的功能，可以实现部分的自定义搜索。

   对有些需要特殊搜索的，可在表单保存的触发脚本中，手工编写脚本，将扩展属性加入到DublinCore的subjects中，即可进行搜索

搜索日志
----------------------------------
搜索日志使用IIndexer接口，有以下外部API:

- list_parts() # 列出所有可用的数据库
- get_last_part() # 得到最后一个在使用的数据库
- add_document(part_name, index, uid=None, data=None, flush=True) # 添加一个索引
- replace_document(part_name, uid, index, data=None, flush=True) # 替换一个索引
- delete_document(part_name, uids, flush=True) # 删除一个索引
- search(parts=None, query=None, orderby=None, start=None, stop=None) # 搜索

看个例子，搜索24小时内，admin用户下载操作记录, 按时间递减排序:::

 import time
 # 构建查询条件
 query = []
 # 限制是下载操作
 query.append(['operation', u'download', ''])
 # 限制用户是admin
 query.append(['displayname', u'admin', ''])
 # 限制是24小时内的日志
 now = time.time()
 before_one_days = now - 24*3600
 query.append(['timestamp', [float(before_one_days), float(now)], 'range'])
 # 搜索, 按时间递减排序
 # query 如果不给，就搜索全部的日志
 site = getSite()
 results = IIndexer(site).search(query=query, orderby='-timestamp')

操作是一个列表，包含’操作ID‘， ’内容‘， ’操作类型‘，

操作类型有’anyof‘, ’allof‘, ’parse‘, ‘range’，‘’  四种
 - ‘’，内容必须是Unicode类型
 - ‘parse’， 内容必须是Unicode类型，操作ID必须是列表，内容的值是模糊匹配
 - ‘anyof’, 内容必须是列表，代表这个操作的值可以是这些内容任意一个
 - ‘allof’, 内容必须是列表，代表这个操作的值必须匹配所有的内容
 - ‘range’，搜索时间相关的时候使用，内容必须是列表，且应该只有两个值，表示开始时间和结束时间

目前可以搜索的操作分别是:::

 download downloadPDF : 下载 和 下载PDF 操作
 upload created newPlan newProject newFlow   : 5种创建操作
 save editoutside editTask editPlan editProject newrevision : 6种编辑操作
 rename renameProject : 2种重命名操作
 copy move : 拷贝 和 移动 操作
 removed delete : 2种删除操作
 subscription : 订阅操作
 comment : 评论操作
 print ： 打印操作
 sendSMS sendSmsReport ： 发送短信 和 发送短信报告 操作
 sendout : 外发操作
 assign : 分配权限操作
 pending published return private free activeProject holdProject closeProject：改变状态操作
 login logout : 登录 和 登出 操作
 
 
标签组
============

标签组实现了多维度、多层次、可管理的标签管理。如果要添加一个标签:

ITagsManager(sheet).addTag('完成')

希望同时去除这个标签组中的所在维度其他的标签， 比如"处理中"这样的状态，因为二者不能同存:

ITagsanager(sheet).addTag('完成', exclude=True)

这里使用ITagManager进行标签管理。完整接口为

- listTags(): 得到全部Tags
- setTags(tags): 更新Tags
- addTag(tag, exclude=False):
  添加一个Tag, 如果exclude，则添加的时候， 把FaceTag的同一类的其他标签删除
- delTag(tag): 删除指定Tag
- canEdit(): 是否可以编辑

另外，使用IFaceTagSetting可进行标签设置的管理：

- getFaceTagText(): 得到face tag 文字
- setFaceTagText(text): 
  设置face tag文字，会自动转换的, 典型如下::

   按产品
   -wps
   -游戏
   -天下
   -传奇
   -毒霸
   按部门
   -研发
   -市场

- getFaceTagSetting(): 得到全部的face tag setting::

   [(按产品, (wps, (游戏, (天下, 传奇)), 毒霸)),
    (按部门, (研发, 市场))]


