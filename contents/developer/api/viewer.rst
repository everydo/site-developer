---
title: 文件转换服务
description: 文件上传、下载、转换
---

==========================
文件转换服务
==========================


.. contents::
.. sectnum::

为了文件预览，缩略图、参数提取等，提供文档转换的服务。

需求
=========
易度对文档有专业的文档预览、打印、转换PDF等功能，这些功能其他相关系统也需要，比如OA、项目管理等。

出于安全性的考虑，要求对云查看的预览地址进行权限保护：只有指定的人员，在指定的时间内可以访问

总体方案
====================
云查看服务，是易度云办公的一个应用，可以独立使用，也可以和文档管理系统整合起来一起使用。

如何得到要转换的文件
-------------------------------
云查看实质对原始文件进行格式转换，并提供转换结果的下载服务。转换的必要条件是云查看需要得到原始文件。

原始文件有2种获得方法：

- 上传到易度文档管理系统，会自动进入在云查看

  这要求购买易度文档管理系统

- 告知文件的url地址，由易度云查看自动去排队下载

  这种方法要求云查看有权限直接下载原始文件，集成比较简单，而且可以做到按需转换，无需预先全部导入文档到系统。
  但是对于OA有权限保护的系统，通常需要进行定制处理，允许云查看服务器直接访问原始文件。

如何进行权限保护
------------------------------
云查看服务器会分发给第三方应用查看密匙(secret)，用于对请求下载转换后的文件的URL进行签名，确保调用URL合法。URL合法的标准包括：

- 签名正确
- 请求发生的IP地址匹配(可选)
- 没有超过使用期限(可选)

如何获得一个密匙secret
----------------------------
在易度工作平台软件包中，安装云查看管理工具，可以获得密匙

/download 下载
==================================
对于转换生成的PDF、缩略图等文件，需要直接下载，可发起 ``/download`` 请求

参数
------------------
- account: 服务器密匙对应的账户(比如:zopen)
- instance: account下具体的一个站点名，如果不设置，就是default
- location: 在文件仓库中的相对地址，如果有sourceURL，这个可以不填写
- source_url: 原始文件的下载地址，如果发现没有下载过，云查看会到这里自动去下线
- source_mime(可选): 原始文件的mime类型，如果不输入，根据文件名计算Mime
- mime(可选): 转换的mime类型
- subfile(可选): 比如转换html中的图片，或者某个大小的缩略图
- ip: 浏览器的ip地址，如不填写则不做IP检查
- timestamp: 截止时间的时间戳，如果不填写，则永久可查看
- app_id: 第三方应用的ID，默认为空即可
- username: 访问用户的名字，仅作记录用
- signcode: 签名信息. 具体算法见后(如果密匙为空，可省略签名)
- app_id: 第三方应用的ID，默认为空即可
- username: 访问用户的名字，仅作记录用
- filename: 下载的时候显示的文件名

对于压缩包包含的文件，如果需要转换，可以在location中使用 ``$$$`` 来分隔解压之后的文件::

   /files/ttt.tgz$$$folder/bbb.tgz$$$abc.doc

这个表示将 ``/files/ttt.tgz`` 解压，找到 ``folder/bbb.tgz`` 后再次解压，最后找到abc.doc这个文件。

注意，签名的时候，使用的location，还是原始的 ``/files/ttt.tgz`` 

签名的时候permission参数的取值:

- 如果mime类型是空，则permission为 download
- 如果mime类型为application/pdf, 则permision为pdf
- 其他情况，permission为preview

HTTP返回值
----------------------
如果HTTP返回码是200，表示正确，正文是具体的转换内容

否则，含义如下：

- 400: 签名不正确
- 401: 超时
- 403: 路径无权限
- 404: 无此文件
- 405: 正在转换
- 406: 转换失败
- 407: 正在下载
- 409: 账户不存在

此时范围内容是详细错误原因::

   {"msg": "文件加密" }

/transform 发起文档转换
==============================
转换和回调接口. 可主动发起转换，转换完成，进行回调。

如果文件准备好，可以预先要求云查看服务器进行转换。可传递的参数包括:

- account: 帐号，在云查看密匙管理中可以得到，如zopen
- instance: 具体的站点号，如 default
- location：具体的文件存放位置，可不填
- source_url: 如果文件不存在，下载的url地址
- source_mime(可选): 原始文件的mime类型，如果不输入，根据文件名计算Mime
- targets: 目标文件的mime类型, 比如::

    application/pdf,text/html

- callback： 各种转换完成的回调url, 如果转换已经完成，则立刻回调

  可以设置多个不同的转换回调::

   {"text/plain": "http://server.com/aa", "application/x-shockwave-flash": "http://blabla.com/bla"}

- ip: 浏览器的ip地址，不填写则不做IP检查
- timestamp：失效时间的时间戳，不填表示不失效
- app_id: 应用id，可不填
- username: 用户名, 可不填
- signcode: 签名, 具体算法见后

返回值见错误码

此方法，签名的permission参数值为 ``transform``

/diff: 文档比较
======================
直接比较2个文档的差异，可传递的参数包括：

- location1: 第一个比较对象的站点路径
- location2: 第二个比较对象的站点路径
- ip: 浏览器的ip地址，如不填写则不做IP检查
- timestamp: 截止时间的时间戳，如果不填写，则永久可查看
- app_id: 第三方应用的ID，默认为空即可
- account: 所属账户
- instance: 所属实例，默认default
- username: 用户名
- signcode: 签名信息, 签名算法见后，其中location使用location1 + location2计算

此方法，签名的permission参数值为 ``diff``

签名算法
==================
使用将下面的信息连接，生成md5，这个md5就是signcode

- location
- ip
- timestamp
- app_id
- account
- instance
- username
- perimission: preview / pdf / download
- secret

注意：

1. 如果只有source_url，没有传入location，上述签名中的location应该按照下面的算法填入::

     /MD5(source_url) + '.' + 文件后缀

2. 密匙secret可以在易度平台上安装 “云查看管理工具” 得到密匙

管理接口
=================
管理接口用于存储服务商的管理后台 , 下面的接口基于OAuth API

/api/v1/admin/get_secret
-------------------------------------------------------------
获取查看密匙, 得到一个转换密匙。注意：这个密匙普通用户无法得到，只有账户管理员才能得到。

传入参数：

- account
- instance

输出密匙::

    'secret_key': 'adfkdwe231jxwdw@asfas2d',   # 保密，用于签名

/api/v1/admin/refresh_secret
----------------------------------------
更新查看密匙, 得到一个新密匙

传入参数：

- account
- instance

输出新的密匙::

   {'secret': ''}

/api/v1/admin/set_access_policy
-----------------------------------
设置访问的策略，包括 公开 或者 私有。
清空转换密匙，这样无需签名，就可以进行文档转换了

传入参数：

- account
- instance
- policy: 可以为private, 或public

/api/v1/admin/info
------------------------------
查看实例的全部信息，包括访问策略


使用查看器查看
===============

下载链接自动云查看
-------------------------------
如果希望让您的站点快速支持下载文件的在线查看，在部署云查看标准版后，只需在您的网站加入如下代码即可::

    <script src="http://your.server.ip/static/api.js"></script>
    <script type="text/javascript">
        cloudview('http://your.server.ip/', account="zopen", instance="default");
    </script>

添加后，您的站点中的文档下载链接(所有后缀为.doc/.docx/.pdf/.zip/ppt等的连接)，会自动转换为云查看链接，从而实现文档的云查看。

嵌入页面的查看器
--------------------
这个是在浏览器中的js调用::

  <div class="viewer" style="height: 100%"></div>
  <script type="text/javascript"]]>
    var viewer = EdoViewer.createViewer('.viewer', {
        server_url: 'http://viewer.everydo.com',
        location: '/asdfa2312132233abc.doc',
        source_url: 'http://192.168.12.111/abc.doc',
        ip: '192.168.1.188', 
        timestamp: 1268901715,
        app_id: '',
        account: 'zopen',
        instance: 'default',
        username: 'panjunyong',
        preview_signcode: 'asdf123123asdf12', 
        pdf_signcode: 'asdf123123asdf12', 
        download_signcode: 'asdf123123asdf12', 
    });
    viewer.load();
  </script>

其中：

- server_url: 云查看服务器的地址
- location: 在文件仓库中的相对地址，如果有sourceURL，这个可以不填写
- source_url: 原始文件的下载地址，如果发现没有下载过，云查看会到这里自动去下线
- ip: 浏览器的ip地址，如不填写则不做IP检查
- timestamp: 截止时间的时间戳，如果不填写，则永久可查看
- app_id: 第三方应用的ID，默认为空即可
- account: 服务器密匙对应的账户(比如:zopen)
- instance: account下具体的一个站点名，如果不设置，就是default
- username: 访问用户的名字，仅作记录用
- preview_signcode: 允许在线查看的签名. 具体算法见后(如果密匙为空，可省略签名)
- pdf_signcode: 允许pdf下载的签名. 具体算法见后(如果密匙为空，可省略签名)
- download_signcode: 下载原始文件的签名

注意：如果云查看没有设置secret，则signcode可以为空，此时云查看不会做安全防护

还可以有更多的参数：

- width：宽度
- height：高度
- bgcolor: 查看器边框背景的颜色，比如'#ffffff'
- allow_print：是否允许打印
- allow_copy：是否允许复制
- waterprint_text: 水印文字
- waterprint_size: 水印字体大小
- waterprint_alpha: 水印透明度
- waterprint_color：水印颜色
- waterprint_x: x方向位置
- waterprint_y: y方向位置
- waterprint_rotation: 方向旋转(从 0 到 180 的值表示顺时针方向旋转；从 0 到 -180 的值表示逆时针方向旋转)
- loading_info: 文档正在加载的提示
- converting_info: 文档正在转换的提示
- timeout_info: 文档转换超时的提示

