---
title: 其他接口
description: 小工具
---

.. Contents::
.. sectnum::

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
- Soap协议	WSDL.Proxy,

数据解析方面
----------------

- json , json格式的数据解析
- xmlObjectify, xml文件对象化
- minidom，dom解析


