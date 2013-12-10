---
title: 其他接口
description: 小工具
---

常用方法
=============

.. Contents::
.. sectnum::

请求和响应对象
------------------
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

getOperationOption(context, option_name=None, default=None):
----------------------------------------------------------------------
得到某个运营选项参数, 比如::

    ('sms', '短信数量', '条','item-count','buyamount'),
    ('apps_packages', '软件包数量', '','int','resource'),
    ('flow_records', '数据库记录', '条','int','resource'),
    ('docsdue', '文档使用期限', '月','permonth-time','buyamount'),
    ('docs_quota', '文件存储限额(M)', '','str','resource'),
    ('docs_users', '文档许可用户数', '','int','resource'),

    # 是否企业版判断，必须为True
    ('docs_publish', "文档发布", '', 'bool', 'function'),
    # 是否高级企业版判断
    ('flow_customize', '流程定制', '','bool','function'),
    # 是否开发版
    ('apps_scripting', '允许开发软件包', '','bool','function'),

getDisplayTime(time, format)
-----------------------------------
人性化的时间显示	time 是datetime类型，或者timestampe的浮点数。Format可为：

- localdate: 直接转换为本地时间显示，到天
- localdatetime: 直接转换为本地时间显示，到 年月日时分
- localtime: 直接转换为本地时间显示，到 时分
- deadline: 期限，和当前时间的比较天数差别，这个时候返回2个值的 ('late', '12天前')
- humandate: 人可阅读的天，今天、明天、后天、昨天、前天，或者具体年月日 ('today', '今天')

其他


易度工具函数
---------------
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
          
  
在易度中，出于安全的考虑，对Python的一些常用包做了控制：

Python的一些常用包
--------------------

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

- dict, set, list,buffer, sum, type, base64,

类型转换	
-----------

- str2int
- str2float,

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
