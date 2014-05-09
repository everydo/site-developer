---
title: 文件存储服务
description: 文件上传、下载、转换
---

==========================
文件存储服务
==========================


.. contents::
.. sectnum::

文件服务file: 负责文件的上传、下载和转换相关的内容. 接口参照 七牛云存储 实现。

文件的存放路径格式为::

  http://<server_address>/<account_name>/

文件存储服务的概念模型
===========================
账户
  每个公司对应一个账户

文件服务实例
  相当于七九的空间(bucket)，可以设置为 ``公开(public)`` 或者 ``私有(private)``

密匙
  密匙用于生成上传、下载管理凭证。包括公开的access_key和保密的、用于签名的securet_key。

  和七牛不同，每个实例有自己的一个密匙，而不是每个账号2个密匙。

文件的路径
  七牛采用域名来标示所在的服务实例(空间bucket)，我们采用路径区别不同的服务实例::
  
     http://<server_addr>/api/v1/file/get/<account_name>/<instance_name>/<path/to/file>

上传下载
===========

/api/v1/file/download
----------------------------------------------------------------
下载文件, 支持 http_range 进行断点续传

完整的url格式::

   http://<server_addr>/api/v1/file/get/<account_name>/<instance_name>/<path/to/file>

其中：

- account: zopen, 账户名
- instance: default, 站点名

可附加url参数：

- e: 下载过期时间（采用unix时间戳）
- token：下载凭证，私有的实例需要提供下载token才能下载，由access_key和签名2部分组成::
   
     <ACCESS_KEY>:<encoded_sign>

下面是扩展url参数：

- mime(可选): 转换的mime类型
- subfile(可选): 主要压缩包中包含的的文件路径
- permission: 签名授予的压缩包里面文件的权限 download/pdf/preview, 默认是下载download
- app_id: 第三方应用的ID，默认为空即可
- username: 访问用户的名字，仅作记录用
- filename: 下载的时候显示的文件名

HTTP错误返回值:

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

api/v1/file/upload
------------------------------------------
表单上传，编码采用“multipart/form-data”。

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

- "ctx":        块级上传控制信息
- "checksum":   上传块校验码
- "crc32":      完整性进行较验
- "offset":    下一个上传块在切割块中的偏移
- "host":       后续上传接收地址

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

管理接口
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

   {'secret': ''}

/api/v1/admin/set_access_policy
-----------------------------------
设置访问的策略，包括 公开 或者 私有。
清空转换密匙，这样无需签名，就可以进行文档转换了

传入参数：

- account
- instance

输出::

   {'secret': ''}

/api/v1/admin/info
------------------------------
查看实例的全部信息，包括访问策略
