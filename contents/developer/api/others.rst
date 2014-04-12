---
title: 其他接口
description: 小工具
---

.. Contents::
.. sectnum::

界面组件
==================
IStatusMessage 显示状态信息
-------------------------------
	 
“IStatusMessage”,界面上显示状态信息

导航树
------------
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
----------
renderFilePreview

render_sendout_macro

render_sendout_login

render_views_menu

render_batch

render_subscribe_button(context, request)        # 关注按钮

render_notification_portlet(context, request)     # 通知方式面板

render_subscription_portlet(context, request)    # 关注面板

render_comment_portlet(context, request)        # 评注组件

render_favorite_button(context, request)    # 收藏按钮(参数show_text默认True)

render_facetag_portlet(context, request)     # 标签组面板

render_tags(context, request)     # 标签(参数parent默认False)

常用方法
=============


易度工具函数
---------------
- “renderFilePreview”: 文件预览组件
- “osf_crypt”:防泄密外发打包函数
- "document_convert": 文档转换 ::

    - source_obj: 系统内的File对象
    - content：   如果没有source_obj对象，可以直接传文件的内容
    - from_mime:  原文件是什么mime类型, 有source_obj可以不传
    - to_mime：   要转换为什么mime类型
    
    返回: {'main': {'title': title, 'content': content},
           'attachments': [{'title':attach1, 'content':attach_content, 'mime':attach_mime},,,] }

    例子：纯文本转换为html:
         print document_convert(content="<h3>标题1</h3>\n 今天是个好日子！！！", from_mime="text/plain", to_mime="text/html")
         >> {'main':{'content': '<html>\n<head><meta http-equiv="content-type" content="text/html; charset=utf-8"></head>\n<body>\n<h3>\xe6\xa0\x87\xe9\xa2\x981</h3><br> \xe4\xbb\x8a\xe5\xa4\xa9\xe6\x98\xaf\xe4\xb8\xaa\xe5\xa5\xbd\xe6\x97\xa5\xe5\xad\x90\xef\xbc\x81\xef\xbc\x81\xef\xbc\x81</body>\n</html>', 'title': 'index'}, 'attachments': [] }
          
  
