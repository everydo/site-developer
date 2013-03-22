常用方法
=============

.. Contents::
.. sectnum::

标签组
---------------

标签组实现了多维度、多层次、可管理的标签管理。如果要添加一个标签:

ITagsManager(sheet).addTag('完成')

希望同时去除这个标签组中的所在维度其他的标签， 比如"处理中"这样的状态，因为二者不能同存:

ITagsanager(sheet).addTag('完成', exclude=True)

这里使用ITagManager进行标签管理。完整接口为

- listTags(): 得到全部Tags
- setTags(tags): 更新Tags
- addTag(tag, exclude=False):
  添加一个Tag, 如果exclude，则添加的时候， 把FaceTag的同一类的其他标签删除
- delTag(tag): 删除指定Tag
- canEdit(): 是否可以编辑

另外，使用IFaceTagSetting可进行标签设置的管理：

- getFaceTagText(): 得到face tag 文字
- setFaceTagText(text): 
  设置face tag文字，会自动转换的, 典型如下::

   按产品
   -wps
   -游戏
   -天下
   -传奇
   -毒霸
   按部门
   -研发
   -市场

- getFaceTagSetting(): 得到全部的face tag setting::

   [(按产品, (wps, (游戏, (天下, 传奇)), 毒霸)),
    (按部门, (研发, 市场))]



界面
----------
::

 render_subscribe_button(context, request)        # 关注按钮
 render_notification_portlet(context, request)     # 通知方式面板
 render_subscription_portlet(context, request)    # 关注面板
 render_comment_portlet(context, request)        # 评注组件
 render_favorite_button(context, request)    # 收藏按钮(参数show_text默认True)
 render_facetag_portlet(context, request)     # 标签组面板
 render_tags(context, request)     # 标签(参数parent默认False)

 

地址生成
-------------------

- absoluteURL (context, request)

  生成context（当前对象）的可以被访问的URL
  request是必须的变量

- resourceURL('binary.gif','fileimg'):

  静态资源缓存url, 可以在缓存目下的fileimg 子目录中找到一张叫做 binary.gif 的图片。
  有些系统自带的图标/css，需要缓存，可以通过这个得到


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

IStatusMessage
-----------------
	 
“IStatusMessage”,界面上显示状态信息

IObjectLogManager
----------------------
“IObjectLogManager”:,记录日志

可使用方法：::

  addObjectLog(operation,text)
  #operation,操作类型
  #text，日志信息
  listLogs()  #日志列表
  
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
