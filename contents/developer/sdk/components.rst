---
title: 界面组件
description: 一组可重用的前端界面组件
---

======================
界面组件
======================

.. contents::

导航树
===============

首先需要引用这个组件::

   TreeView = require('backbone_treeview')

初始化组件::

   tree = new TreeView({checkable: true, is_static: false})

如同 ``is_static`` 是true，表示静态树，不会动态加载触发 ``load`` 事件。

直接加载数据::
  
   tree.load_nodes({id:'1', 
                    name:'node 1', 
                    icon:'folder', 
                    is_folder:true,
                    data:{'type':'folder',},
                    nodes: {id:'1.1', 
                            name:'node1.1', 
                            is_folder:false,
                            icon:'file',
                            data:{'type':'file'}}
                  },
                  {id:'2',
                   name:'node 2', 
                   icon:'folder', 
                   is_folder:true,
                   data:{type:'shorcut'}
                  })

可以设置加载子树事件，动态加载::

   tree.on('load', function(node){})

如果点击一个节点::

   tree.on('clicknode', function(node){ })

显示树::

   tree.render('#tree-container')

得到选中项, 得到node的集合::

   tree.get_checked()

得到当前激活项, 得到一个node::

   tree.get_activated()

在当前已经加载的所有节点中，找到指定ID的节点::

   tree.get_node(node_id)
   tree.get_node([node_id_1, node_id_2, node_3], function (node) {
                        node.expand()
                  })

node是具体的某个节点对象，有如下功能::

  node.load_nodes({})  # 继续加载子节点
  node.model           # node_view绑定的model信息
  node.expand(function (node) {} ) # 展开，如果没有加载过，会自动触发加载
  node.collapse()      # 折叠
  node.activate()      # 高亮激活

