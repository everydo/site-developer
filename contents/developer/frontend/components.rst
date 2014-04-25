---
title: 界面组件
description: 一组可重用的前端界面组件
---

======================
界面组件
======================

导航树
===============

首先需要引用这个组件::

   TreeComponent = require('components/tree')

初始化组件::

   tree = new TreeComponent('#tree', {checkable: true})

直接加载数据::
  
   tree.load_data({id:'1', name:'node 1', icon:'folder', data:{'type':'folder',}, 
                   nodes: {'id':'1.1', 'name':'node1.1', 'icon':'file',
                          data:{'type':'file'}} },
                  {id:'2', name:'node 2', icon:'folder', data:{type:'shorcut'}})

也可以从服务器上加载数据::

   tree.load(url)

可以设置加载子树事件，动态加载::

   tree.on('load', function(model, collection, view){})

也可以相应选中事件::

   tree.on('checked', function(model, collection, view){})

如果点击一个节点::

   tree.on('clicknode', function(model, collection, view){ })

显示树::

   tree.render()

得到选中项::

   tree.get_checked()

得到当前激活项::

   tree.get_activated()

