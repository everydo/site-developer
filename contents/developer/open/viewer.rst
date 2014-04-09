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
现在需要和易度联系，未来会提供自助的密匙管理界面 

浏览器接口
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
- account: 服务器密匙对应的账户(比如:zopen)
- instance: account下具体的一个站点名，如果不设置，就是default
- username: 访问用户的名字，仅作记录用
- download_source: 下载原始文件，这个会影响能否下载压缩包里面的文件，以及能否对mp3直接下载原始文件播放
- signcode: 签名信息. 具体算法见后(如果密匙为空，可省略签名)

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

主动发起转换: /do_transform
------------------------------
可直接在浏览器上发起转换请求。

如果文件准备好，可以预先要求云查看服务器进行转换。可传递的参数包括:

- account: 帐号，在云查看密匙管理中可以得到，如default.zopen.standalone
- instance: 具体的站点好
- location：具体的文件存放位置
- source_url: 如果文件不存在，在哪里下载
- targets: 目标文件的mime类型
- ip: 浏览器的ip地址，如不填写则不做IP检查
- timestamp：失效时间
- app_id: 应用id，默认为空
- username: 用户名
- signcode: 签名, 具体算法见后

返回值见错误码

文档比较: /diff
---------------------
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

开放API
==================
开放API是由应用通过易度的oauth2接口规范发起的api请求。需要使用的应用，需要先到易度运营中心注册，得到开放API的认证密匙。

get_secret: 获取查看密匙
-------------------------------
得到一个转换密匙

传入参数：

- account_name
- instance_name

输出密匙::

   {'secret': ''}

refresh_secret: 更新查看密匙
----------------------------------------
得到一个新密匙

传入参数：

- account_name
- instance_name

输出新的密匙::

   {'secret': ''}

clear_secret: 清空转换密匙
-----------------------------------
清除密匙，这样无需签名，就可以进行文档转换了

传入参数：

- account_name
- instance_name

输出::

   {'secret': ''}

transform: 转换和回调接口
---------------------------------------
可主动发起转换，转换完成，进行回调。

传入参数：

- account_name: 需要转换的账号
- instance_name: 需要转换的站点
- location: 需要转换的文件相对于站点的路径
- targets: 需要专门的目标Mime类型
- callback： 转换完成的回调url, 如果转换已经完成，则立刻回调

remove: 删除文件
--------------------
删除之前转换的文件

附录
==========

签名(signcode)算法
----------------------
使用查看器的程序，如果需要对查看的url进行权限保护，需要传入签名字段。签名的生成算法如下：

1. 得到原始文件在服务端的存放地址(location) :

       /MD5(source_url) + '.' + 文件后缀

   也可以使用其他算法，但是调用查看器的时候，location参数也必须使用这个地址

2. 使用将下面的信息连接，生成md5，这个md5就是signcode

   - location 
   - ip
   - timestamp
   - app_id
   - account
   - instance
   - username 
   - download_source
   - secret

HTTP错误返回值
------------------------

如果HTTP返回码是200，表示正确，正文是具体的转换内容

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

此时范围内容是详细错误原因::

   {"msg": "文件加密" }

