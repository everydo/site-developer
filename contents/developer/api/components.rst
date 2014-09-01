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

   TreeView = require('treeview')

另外在需要加载treeview.css，才能正确显示。

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

人员选择组件
=====================
用来选择某个公司账户的人员和部门、和组::

   OrgView = require('orgview')

初始化组件::

   orgview = new OrgView('members', {multiple: true, selectable:[ou, group, person], })

整个人员选择组件是一个表单输入项，其中members是输入项的名字，显示::

  orgview.render('#left')

选择人员后，人员和组的id存放在members的hidden变量中，也可以通过js获得::

   orgview.get_selected() 

返回选择信息::

  [{'type':'ou', name:'dev', title:'开发部'},  ] 

也可以设置事件响应方法, 选择和去除选择::

  orgview.on('select', func (node) {})

  orgview.on('deselect', func (node) {})

