---
title: 文档查看接口
description: 在外部系统中，查看易度的文档
---

=====================
文档查看接口
=====================

.. sectnum::
.. contents::

需求
=========
易度对文档有专业的文档预览、打印、转换PDF等功能，这些功能其他相关系统也需要，比如OA、项目管理等。

出于安全性的考虑，要求对云查看的预览地址进行权限保护：只有指定的人员，在指定的时间内可以访问

总体方案
====================
如何得到原始文件
-------------------------------
云查看实质对原始文件进行格式转换，并提供转换结果的下载服务。转换的必要条件是云查看需要得到原始文件。

原始文件有2种获得方法：

- 告知原始地址，由易度云查看自动去排队下载

  这种方法要求云查看有权限直接下载原始文件，集成比较简单，而且可以做到按需转换，无需预先全部导入文档到系统。
  但是对于OA有权限保护的系统，通常需要进行定制处理，允许云查看服务器直接访问原始文件。

- 主动上传原始文件

  利用易度的文件访问API，上传文件。这个API使用比较复杂，但是可以利用易度做文件存储服务。

如何进行权限保护
------------------------------
OA 和 云查看服务器共享一套密匙(secret)，用于对请求下载转换后的文件的URL进行签名，确保调用URL合法。URL合法的标准包括：

- 签名正确
- 请求发生的IP地址匹配(可选)
- 没有超过使用期限(可选)

如何获得一个密匙secret
----------------------------
有三种可能：

1) 使用空帐号，无密匙secret，这个是默认情况，这个最简单，无需计算签名，当然也不会进行权限检查。

   这种情况，无需服务端开发，最方便集成

2) 对于空帐号，可以在配置文件中设置一个secret。直接使用这个secret进行签名计算

   这个密匙和向管理员申请获得。

   使用这个默认密匙，可以快速启动开发。

3) 对于易度开放平台的用户，如果需要使用云查看，需要使用API申请一个密匙

   - 这个需要通过易度办公平台注册一个应用，得到app_key, app_security
   - 通过oauth2得到一个token
   - 使用这个token，利用我们查看的Open API，得到一个云查看的security

核心API
==================

申请云查看密匙secret
------------------------
这个针对易度开放平台的用户。对于空账户，无需申请。

结合易度开放平台，利用oauth2框架，每个帐号申请自己的secret，接口为::

       get_account_security(refresh=False)

其中：

- 如果refresh是True，则刷新一个新secret
- 密码对应的account是: account_name.vender_name

返回为::

   {"account":"zopen.test", 
    "secret":"23asdfa"}

发起转换
------------------
如果文件准备好，可以预先要求云查看服务器进行转换。可发起如下rpc(http)::

   http://server.com/transform?location=&source_url=&timestamp=&account=&app_id=&signcode=

- location：具体的文件存放位置
- source_url: 如果文件不存在，在哪里下载
- timestamp：失效时间
- account: 帐号，默认为空
- app_id: 应用id，默认为空
- signcode: 签名, 具体算法见后

返回值见错误码

删除文件
--------------------
如果文件发生变化，可以要求云查看服务器删除之前文件，可发起出现rpc(http)::

   http://server.com/remove?location=&timestamp=&account=&app_id=&signcode=

含义同前，返回值见错误码

查看器调用API
--------------------
这个是在浏览器中的js调用::

  <div class="viewer" style="height: 100%"></div>
  <script type="text/javascript"]]>
    var viewer = EdoViewer.createViewer('.viewer', {
        server_url: 'http://viewer.everydo.com',
        location: '/wo/default.zopen.test/files/abc.doc',
        source_url: 'http://192.168.12.111/abc.doc',
        ip: '192.168.1.188', 
        timestamp: 1268901715,
        app_id: '',
        account: '',
        download_source: 1,
        username: 'panjunyong',
        signcode: 'asdf123123asdf12', 
        
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
- account: 服务器密匙对应的账户(zopen.standalone)，默认为空即可
- username: 访问用户的名字，仅作记录用
- download_source: 下载原始文件，这个会影响能否下载压缩包里面的文件，以及能否对mp3直接下载原始文件播放
- signcode: 签名信息. 具体算法见后

注意：如果云查看没有设置secret，则signcode可以为空，此时云查看不会做安全防护

还可以有更多的参数：

- width：宽度
- height：高度
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

签名(signcode)算法
=========================
使用查看器的程序，如果需要对查看的url进行权限保护，需要传入签名字段。签名的生成算法如下：

1. 得到原始文件在服务端的存放地址(location) :

       /files/MD5(sourceURL) + '.' + 文件后缀

   也可以使用其他算法，但是调用查看器的时候，location参数也必须使用这个地址

2. 使用将下面的信息连接，生成md5，这个md5就是signcode

   - location 
   - ip
   - timestamp
   - app_id
   - account
   - username 
   - download_source
   - secret

HTTP错误返回值
=================

如果是200，表示正确

否则，含义如下：

- 400: 签名不正确
- 401: 超时
- 403: 路径无权限
- 404: 无此文件
- 405: 正在转换
- 406: 转换失败
- 407: 正在下载
- 408: ip不匹配
- 409: 账户不存在

