---
title: 文件存储服务
description: 文件上传、下载、转换
---

==========================
文件存储服务
==========================


.. contents::
.. sectnum::

文件服务file: 负责文件的上传、下载. 接口参照 七牛云存储 实现。

文件的存放路径格式为::

  http://<server_address>/<account_name>/

文件存储服务的概念模型
===========================
文件存储和周边模块的关系::

  +----------+
  | 账户管理 |
  +----------+
       |
   运营| 密匙、安全
       V      
  +----------+  文件管理    +-----------+
  | 文件存储 |<------------>|  工作平台 |
  +----------+  上传回调    +-----------+
       ^                         ^
       | 上传下载                |
       |                         |
  +------------+                 |
  | 文件客户端 |-----------------'
  +------------+

主要概念：

账户 account
  每个公司对应一个账户

文件服务实例 instance
  账户管理通过运营接口，可以创建存储空间，相当于七九的空间(bucket)

密匙
  密匙用于生成上传、下载管理凭证。包括公开的access_key和保密的、用于签名的securet_key。

  每个实例有自己的一个密匙

文件的路径
  在url路径中，可区别不同的账户和实例::

     http://<server_addr>/api/v1/file/get/<account_name>/<instance_name>/<path/to/file>

运营接口
=================
运营接口，是有账户中心向存储服务发起的，包括创建、删除、更新运营参数等。

/api/v1/operator/create_instance
--------------------------------------
创建新的实例

参数：

- account_name
- instance_name

/api/v1/operator/update_options
---------------------------------------------
更新运营参数

- account_name:
- instance_name:
- operation_options: 运营参数，比如容量限制、失效时间等

/api/v1/operator/list_options
-----------------------------------
得到运营参数的实际值，比如到期时间、容量限制等

- account_name,
- instance_name,

/api/v1/operator/destroy_instance
---------------------------------------------
注销一个实例，删除所有实例的内容：

- account_name
- instance_name
上传下载
==================

/api/v1/file/download
----------------------------------------------------------------
下载文件, 支持 http_range 进行断点续传

完整的url格式::

  http://<server_addr>/api/v1/file/download/<account_name>/<instance_name>/<path/to/file>

其中：

- account: 账户名，如 ``zopen``
- instance: 站点名，如 ``default``

可附加url参数：

- e: 下载过期时间（采用unix时间戳）
- token：下载凭证，私有的实例需要提供下载token才能下载，由access_key和签名2部分组成::

     <ACCESS_KEY>:<encoded_sign>

下面是扩展url参数：

- filename: 下载的时候显示的文件名
- app_id: 第三方应用的ID，默认为空即可，仅做日志用
- username: 访问用户的名字，仅作日志用

api/v1/file/upload
------------------------------------------
表单上传，编码采用 ``multipart/form-data``

完整的url格式::

   http://<server_addr>/api/v1/file/upload/

包括如下字段：

- token：上传凭证，由如下信息组成::

     <ACCESS_KEY>:<encoded_sign>:<encoded_put_policy>

  其中encoded_put_policy包括：

  - scope： 上传到哪里，格式：'<instance_name>:sunflower.jpg'
  - deadline：上传请求授权的截止时间
  - insertOnly：能否修改已经存在的
  - returnUrl：上传之后，303跳转的地址，会通过 ``upload_ret`` 参数返回returnBody内容
  - returnBody: 需要返回json文本格式
  - callbackUrl：回调的URL，必须返回application/json格式结果
  - callbackBody：回调传递的url query字符串
  - saveKey: key的生成规则
  - fsizeLimit：限制文件上传大小
  - mimeLimit：允许上传的类型

  以及我们扩展的：
 
  - ip: 限定ip地址，如不填写则不做IP检查

- file：文件
- key: 文件的存放路径，包括文件名
- x: 扩展字段, 包括

  - account: zopen, 账户名
  - instance: default, 站点名
  - uid: 12312312, 文件所在文件夹的uid，和path任选一个
  - path: /files/abc.doc, 文件路径，和uid任选一个
  - parent_revision: 12, 上一版本的版本号，用于检查冲突, 如果冲突，则合并失败，必须在下载最新版本解决冲突之后上传。

返回：

- hash：每个文件都有一个hash，Fh8xVqod2MQ1mocfI4S4KpRL6D98，可用于校验
- key：文件名

/api/v1/file/mkblk
------------------------------
为后续分片上传创建一个新的块，同时上传第一片数据.

请求格式::

 POST /mkblk/<blockSize> HTTP/1.1
 Host:           up.qiniu.com
 Content-Type:   application/octet-stream
 Content-Length: <firstChunkSize>
 Authorization:  UpToken <UploadToken>

 <firstChunkBinary>

url路径参数:

- blockSize: 块大小，不超过4MB。

响应json：

- ctx:        块级上传控制信息
- checksum:   上传块校验码
- crc32:      完整性进行较验
- offset:    下一个上传块在切割块中的偏移
- host:       后续上传接收地址

/api/v1/file/bput
---------------------------
上传指定块的一片数据::

  POST /bput/<ctx>/<nextChunkOffset>

url参数：

- ctx：前一次上传返回的块级上传控制信息
- nextChunkOffset：当前片在整个块中的起始偏移

返回参数类似mkblk

/api/v1/file/mkfile
-------------------------------
将上传好的所有数据块按指定顺序合并成一个资源文件::

  POST /mkfile/<fileSize>/key/<encodedKey>/x:user-var/<encodedUserVars>

- fileSize: 资源文件大小
- encodedKey: 进行URL安全的Base64编码后的资源名
- encodedUserVars: 指定自定义变量。

请求正文body：

- <ctxList>：所有创建block的列表::

    <lastCtxOfBlock1>,<lastCtxOfBlock2>,<lastCtxOfBlock3>,...,<lastCtxOfBlockN>

返回: 

- key
- hash

上传回调接口
==========================
文件一旦上传到系统，会向工作平台发起一个回调请求，包括内容:

- account: zopen, 账户名
- instance: default, 站点名
- uid: 12312312, 文件所在文件夹的uid，和path任选一个
- path: /files/abc.doc, 文件路径，和uid任选一个
- parent_revision: 12, 上一版本的版本号，用于检查冲突, 如果冲突，则合并失败，必须在下载最新版本解决冲突之后上传。

发起请求之后，工作平台会返回一个文件操作指令:

文件管理
===============

移动
---------

复制
--------

删除
-------

运营接口
================

密匙和安全
=================
管理接口用于存储服务商的管理后台

/api/v1/admin/get_secret
-------------------------------------------------------------
获取查看密匙, 得到一个转换密匙。注意：这个密匙普通用户无法得到，只有账户管理员才能得到。

传入参数：

- account
- instance

输出密匙::

   {'access_key': '2332Hasdf(2323asdfa33dd',   # 公开，用于标识用户身份
    'secret_key': 'adfkdwe231jxwdw@asfas2d',   # 保密，用于签名
    }

/api/v1/admin/refresh_secret
----------------------------------------
更新查看密匙, 得到一个新密匙

传入参数：

- account
- instance

输出新的密匙::

   {'access_key': '2332Hasdf(2323asdfa33dd',   # 公开，用于标识用户身份
    'secret_key': 'adfkdwe231jxwdw@asfas2d',   # 保密，用于签名
    }

