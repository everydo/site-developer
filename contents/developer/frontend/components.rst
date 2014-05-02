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

   tree = new TreeComponent({checkable: true})

直接加载数据::
  
   tree.load_nodes({id:'1', 
                    name:'node 1', 
                    icon:'folder', 
                    data:{'type':'folder',}, 
                    nodes: {id:'1.1', 
                            name:'node1.1', 
                            icon:'file',
                            data:{'type':'file'}} 
                  },
                  {id:'2', 
                   name:'node 2', 
                   icon:'folder', 
                   data:{type:'shorcut'}
                  })

可以设置加载子树事件，动态加载::

   tree.on('load', function(node_view){})

如果点击一个节点::

   tree.on('clicknode', function(node_view){ })

显示树::

   tree.render('#tree-container')

得到选中项, 得到node_view的集合::

   tree.get_checked()

得到当前激活项, 得到一个node_view::

   tree.get_activated()

node_view是具体的某个节点对象，有如下功能::

  node_view.load_nodes({})   # 继续加载子节点
  node_view.model           # node_view绑定的model信息
  node_view.expand()        # 展开
  node_view.collapse()       # 折叠
  node_view.activate()      # 高亮激活

