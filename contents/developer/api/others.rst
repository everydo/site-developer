---
title: 其他接口
description: 小工具
---

.. Contents::
.. sectnum::

脚本几种模板
===================
空模板
---------
也就是没有任何模板

空白模板
----------
保留css/js的模板

基础basic模板
---------------
- 脚本的标题，作为
- 

标准standard模板
------------------
::

                view.left = html.get('left', '')
                view.right = html.get('right', '')
                view.body = html.get('body', '')
                view.title = html.get('title', '')
                view.description = html.get('description', '')

请求和响应对象
===================
request是一个内置变量，存放了当前浏览器的请求信息。

- ``request.principal.id #当前用户id``
- ``request[field_name] #提交的表单信息``

    如果通过表单来上传文件，表单的action属性里面要给__disable_frs4wsgi=1这个参数，
    然后通过request.get('field_name','')来获取文件的对象进行操作，如果不加该参数，上传
    的文件会自动进入系统的FRS中，不能通过request.get()来取得。

- 其他的CGI信息

request.response是当前请求的返回信息：

- ``request.response.setHeader('Content-Type', 'application/excel') #设置消息头``
- ``request.response.redirect(url) #跳转到另外一个地址``

如果需要得到Session信息，可使用ISession(request)进行操作

地址生成
-------------------

- absoluteURL (context, request)

  生成context（当前对象）的可以被访问的URL
  request是必须的变量

- resource_url('fileimg/binary.gif'):

  静态资源缓存url, 可以在缓存目下的fileimg 子目录中找到一张叫做 binary.gif 的图片。
  有些系统自带的图标/css，需要缓存，可以通过这个得到

  另外，扩展应用的资源地址也类似::

     resource_url('zopen.test/abc.css')
     resource_url('zopen.test/abc.png')

getDisplayTime(time, format)
-----------------------------------
人性化的时间显示	time 是datetime类型，或者timestampe的浮点数。Format可为：

- localdate: 直接转换为本地时间显示，到天
- localdatetime: 直接转换为本地时间显示，到 年月日时分
- localtime: 直接转换为本地时间显示，到 时分
- deadline: 期限，和当前时间的比较天数差别，这个时候返回2个值的 ('late', '12天前')
- humandate: 人可阅读的天，今天、明天、后天、昨天、前天，或者具体年月日 ('today', '今天')


Python的一些常用包
--------------------
在易度中，出于安全的考虑，对Python的一些常用包做了控制：

- 已import的包	
    datetime, calendar, xlwt, StringIO, 

- 可import的包	

    - 'time', 时间
    - 'random', 随机
    - 'hashlib', hach函数
    - 'urllib2', http通信
    - 'Crypto.Cipher', 加密
    - 'array', 阵列
    - 'binascii', binascii转换
    - 'xmlrpclib'：xmlrpc通信

基本函数
------------

- dict, set, list, buffer, sum, type, base64,

Zope组件架构接口	
------------------

- objectProvides,
- getUtility
- Soap协议	WSDL.Proxy,

数据解析方面
----------------

- json , json格式的数据解析
- xmlObjectify, xml文件对象化
- minidom，dom解析

调用一个脚本
-------------------------
call_script('zopen.api.calc', a, b)

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
          
  
流程多查看方式
-----------------------
只需使用特殊的python脚本命名前缀，就可实现流程单的多种查看方式。

对于表单的名字 foobar，命名方式为::

 view_foobar_xxx

其中xxx为真正的脚本名称。

如果需要改变默认的视图，只需要::

 IAppletData(flow_container).default_view = 'xxx_account.xxx_package.view_foobar_xxx'


