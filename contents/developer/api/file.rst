---
title: 文件管理
description: 文件操作和访问相关接口：版本管理、文件夹管理、快捷方式等
---

==========
文件管理
==========

.. Contents::
.. sectnum::

File对象
=====================================
- get_data(): 得到文件的数据
- set_data(data, filename=None, content_type=None)

  设置文件的内容, filename也是用来计算content_type的, 2个二选一就行

文件夹排序接口
=====================
默认文件夹的子文件夹，都支持排序；当子文件夹数量超过30的时候，自动变成非排序文件夹。

- is_ordered() 是否是排序文件夹
- switch_ordered() 将非排序文件夹，转换为排序文件夹，如果不满足条件，可能会抛出ValueError异常
- orderedFolders(): 返回排序后的文件夹集合 
- updateOrder(order): 更新排序

快捷方式
================

TODO

“IRevisionManager”: 版本管理
=====================================

- save(comment='', metadata={}): 存为一个新版本
- retrieve(selector=None, preserve=()): 获得某一个版本
- getHistory(preserve=()): 得到版本历史清单信息
- remove(selector, comment="", metadata={}, countPurged=True): 删除某个版本 
- getWorkingVersionData(): 得到当前工作版本的版本信息，取出来后，在外部维护数据内容


IFolderManager
=====================================
addFile(name, title=u'', description=u'', content_type='text/plain', data='')

添加一个文件

newFile(name, description=u'')

添加一个文件，按模板创建（ppt, doc, xls, pptx, docx, xlsx)

相关方法
=====================================
- “renderFilePreview”: 文件预览组件
- createShortCut(doc, folder, request)：创建快捷方式
