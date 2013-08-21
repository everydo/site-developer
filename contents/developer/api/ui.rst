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

               {'name':'pnoderoot',
                'title':'level1_root',
                'url': absoluteURL(self.context, self.request),
                ‘state’:collasped/expanded/loading,
                ‘obj’:
                    'view': '/@@view.html',
                'icon':'',
                'children':
                [
                    {'name':'pnode1',
                    'title':'level1_1',
                    'url': absoluteURL(self.context, self.request),
                    'view': '/@@view.html',
                    'icon':'/@@/file.gif',
                    'children':[],
                    },
                   {'name':'pnode2',
                    'title':'level1_2',
                    'url': absoluteURL(self.context, self.request),
                    'icon':'/@@/file.gif',
                    'children':
                        [
                            {'name':'snode1',
                            'title':'level2_1',
                            'url': absoluteURL(self.context, self.request),
                            'icon':'/@@/file.gif',
                            'children':[],
                            },
                         。。。

node_template用于渲染每个节点，是handlerbar的模版结构

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
