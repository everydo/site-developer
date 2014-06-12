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

/download 下载
==================================
对于转换生成的PDF、缩略图等文件，需要直接下载，可发起 ``/download`` 请求

参数
------------------
- account: 服务器密匙对应的账户(比如:zopen)
- instance: account下具体的一个站点名，如果不设置，就是default
- server_url: 云查看服务器的地址
- location: 在文件仓库中的相对地址，如果有sourceURL，这个可以不填写
- source_url: 原始文件的下载地址，如果发现没有下载过，云查看会到这里自动去下线
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
- targets: 目标文件的mime类型, 比如::

    application/pdf,text/html

- callback： 转换完成的回调url, 如果转换已经完成，则立刻回调
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

2. 密匙secret可以在易度平台上安装 “云查看管理工具” 活得密匙

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

