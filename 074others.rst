.. Contents::
.. sectnum::

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

ItinyTable
-------------

动态表格数据查询

- ``queryRows(**kw)``: 列出满足指定条件的数据行,kw指定列的条件
- sum(expr): expr 是一个表达式, 比如 "price * num"


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
          
    


