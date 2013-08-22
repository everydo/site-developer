---
title: 界面组件
---

==================
界面组件
==================

导航树
========
render_navtree(navtree_data, node_template=None)

其中navtree_data是导航数的数据，node_template是导航数的渲染模版. 

navtree_data的例子如下::

             [              
               {
                'title': 'level1_root',
                'url': absoluteURL(self.context, self.request),
                'icon': '',
                'children':
                  [
                    {
                     'title': 'level1_1',
                     'url': absoluteURL(self.context, self.request),
                     'icon': '',
                     'children':[],
                    },
                    {
                     'title': 'level1_2',
                     'url': absoluteURL(self.context, self.request),
                     'icon': '',
                     'children':
                        [
                          {
                           'title':'level2_1',
                           'url': absoluteURL(self.context, self.request),
                           'icon':'',
                           'children':[],
                          },
                        ]
                     }
                   ]
                 }
               ]

结构示例

    * level1_root

      + level1_1

      + level1_2

        - level2_1

数据随模板指定，其中有几个内置变量用于导航树的节点。

内置变量

1. children 值为None,不会出现展开图标。没有key表示用于Ajax展开情况。

#. classes 为节点的class赋值

#. expanded 节点展开，默认不展开

#. attributes 节点属性设置，例如 'attributes': "param1='true' param2='false'"

node_template用于渲染每个节点内容，采用handlerbar的模版

node_template的例子如下::

   <a href="{{url}}{{view}}" title="{{title}}"><img src="{{icon}}"> {{title}}</a>

其他
================
renderFilePreview

render_subscribe_button

render_subscription_portlet

render_notification_portlet

render_comment_portlet

render_sendout_macro

render_sendout_login

render_favorite_button

render_facetag_portlet

render_tags

render_views_menu

render_batch
