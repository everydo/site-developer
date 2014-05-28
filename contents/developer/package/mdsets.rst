---
title: 属性集
description: 自动生成表单、合法性校验，数据存储等
---

==================
表单处理
==================

.. Contents::
.. sectnum::

任何一个对象都可以附加一组属性::

   obj.new_mdset('zopen.archive:archive_info')

可以设置属性::

   obj.mdset('zopen.archive:archive_info')['number'] = 12

其中的 ``zopen.archive:archive_info`` 是属性集的代号，可以在对应的软件包中进行定义

注册
=========
属性集的注册类似表单::

  root.packages.register_mdset('zopen.sales', 
        {name:
         title:, 
         description:, 
         fields:,
         on_update:,
         template:,
         obejct_types})

使用表单
==================
可设置某个容器下的子条目的默认扩展属性::

  container.set_setting('item_mdsets', ['zopen.archive:archive_info', 'zopen.contact:contact'])

一旦设置，各个条目即便没有这些扩展属性，也都显示出来供编辑。

也可以得到一个属性集表单对象::

  form = root.packages.get_mdset( mdset_name )

软件包文件
====================
可以导出导入为一个python文件::

  root.packages.export_mdset('zopen.sales:inquery')

同样可以导入这样一个文件::

  root.packages.import_mdset('zopen.sales:inquery', schema_file_conent)

